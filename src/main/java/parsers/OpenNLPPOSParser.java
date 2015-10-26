package parsers;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import constants.StringConstants;
import utils.NLPFileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Open NLP-based POS-tagger.
 *
 * @author sfedosov
 */
public class OpenNLPPOSParser extends AbstractParser {

    @Override
    public void run(final String inputFile) throws IOException {
        final Path outputFileName = Paths.get(inputFile).getParent().resolve(
                inputFile + "_" + StringConstants.OPEN_NLP_POS_OUTPUT_FILE);
        NLPFileUtils.prepareOutputFile(outputFileName);
        final List<String> lines = NLPFileUtils.readAllLines(inputFile);
        final InputStream inputStreamModel = getClass().getResourceAsStream(StringConstants.POS_MAXENT_TAGGER_LOCATION);
        final POSTaggerME tagger = new POSTaggerME(new POSModel(inputStreamModel));
        inputStreamModel.close();
        final List<String[]> result = new ArrayList<>();
        for (String line : lines) {
            String[] tokens = WhitespaceTokenizer.INSTANCE.tokenize(line);
            String[] tags = tagger.tag(tokens);
            POSSample posTaggedSentence = new POSSample(tokens, tags);
            result.add(new String[]{line, posTaggedSentence.toString(), filterSentence(posTaggedSentence)});
        }
        NLPFileUtils.writeAllLines(outputFileName, result, new String[]{});
    }

    private String filterSentence(final POSSample posTaggedSentence) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < posTaggedSentence.getSentence().length; i++) {
            if (allowedTags.contains(posTaggedSentence.getTags()[i])) {
                result.append(posTaggedSentence.getSentence()[i]).append(" ");
            }
        }
        return result.toString();
    }

}
