package parsers;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.coref.DefaultLinker;
import opennlp.tools.coref.Linker;
import opennlp.tools.coref.LinkerMode;
import opennlp.tools.coref.mention.DefaultParse;
import opennlp.tools.coref.mention.Mention;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import constants.StringConstants;
import utils.NLPFileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Open NLP-based coreference resolution parser.
 *
 * @author sfedosov
 */

public class OpenNLPCoreferenceResolutionParser extends AbstractParser {

    {
        System.setProperty("WNSEARCHDIR", getClass().getResource(StringConstants.WORD_NET_DICT_LOCATION).getPath());
    }

    @Override
    public void run(String inputFile) throws IOException {
        final Linker linker = new DefaultLinker(
                getClass().getResource(StringConstants.COREF_RES_MODELS_LOCATION).getPath(), LinkerMode.TEST);
        final Collection<String> collection = NLPFileUtils.readAllLines(inputFile);
        final String[] sentences = collection.toArray(new String[collection.size()]);
        final Path outputFileName = Paths.get(inputFile).getParent().resolve(
                inputFile + "_" + StringConstants.OPEN_NLP_COREF_OUTPUT_FILE);
        NLPFileUtils.prepareOutputFile(outputFileName);
        final List<String[]> result = new ArrayList<>();
        for (int i = 0; i < sentences.length; i++) {
            final List<Mention> document = new ArrayList<>();
            // generate the sentence parse tree
            final Parse[] parse = ParserTool.parseLine(
                    sentences[i],
                    ParserFactory.create(
                            new ParserModel(
                                    getClass().getResourceAsStream(StringConstants.PARSER_CHUNKER_LOCATION))), 1);
            final DefaultParse parseWrapper = new DefaultParse(parse[0], i);
            final Mention[] extents = linker.getMentionFinder().getMentions(parseWrapper);
            //Note: taken from TreebankParser source...
            for (Mention extent : extents) {
                // construct parses for mentions which don't have constituents
                if (extent.getParse() == null) {
                    final Parse snp = new Parse(parse[0].getText(), extent.getSpan(), "NML", 1.0, 0);
                    parse[0].insert(snp);
                    // setting a new Parse for the current extent
                    extent.setParse(new DefaultParse(snp, i));
                }
            }
            document.addAll(Arrays.asList(extents));
            result.add(new String[]{sentences[i],
                    Arrays.toString(linker.getEntities(document.toArray(new Mention[document.size()])))});
        }
        NLPFileUtils.writeAllLines(outputFileName, result, new String[]{"INITIAL PHRASE", "COREFERENCE RESOLUTION"});
    }
}
