package parsers;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import constants.StringConstants;
import utils.NLPFileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Stanford NLP-based coreference resolution parser.
 *
 * @author sfedosov
 */
public class StanfordNLPCoreferenceResolutionParser extends AbstractParser {

    @Override
    public void run(String inputFile) throws IOException {
        Path outputFileName = Paths.get(inputFile).getParent().resolve(
                inputFile + "_" + StringConstants.STANFORD_NLP_COREF_OUTPUT_FILE);
        NLPFileUtils.prepareOutputFile(outputFileName);
        List<String> lines = NLPFileUtils.readAllLines(inputFile);

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        List<String[]> result = new ArrayList<>();
        for (String line : lines) {
            Annotation document = new Annotation(line);
            pipeline.annotate(document);
            // This is the coreference link graph
            // Each chain stores a set of mentions that link to each other,
            // along with a method for getting the most representative mention
            // Both sentence and token offsets start at 1!
            Map<Integer, CorefChain> graph =
                    document.get(CorefChainAnnotation.class);
            result.add(new String[]{line, String.valueOf(extractSpans(graph))});
        }
        NLPFileUtils.writeAllLines(outputFileName, result, new String[]{"INITIAL PHRASE", "COREFERENCE RESOLUTION"});
    }

    private List<String> extractSpans(Map<Integer, CorefChain> graph) {
        List<String> spans = new ArrayList<>();
        for (Integer id : graph.keySet()) {
            CorefChain chain = graph.get(id);
            StringBuilder sb = new StringBuilder("[");
            for (CorefMention cmen : chain.getMentionsInTextualOrder()) {
                sb.append(cmen.mentionSpan).append(" ");
            }
            spans.add(sb.append("]").toString().trim());
        }
        return spans;
    }
}
