package parsers;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import constants.StringConstants;
import utils.NLPFileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Stanford NLP-based POS-tagger.
 *
 * @author sfedosov
 */
public class StanfordNLPPOSParser extends AbstractParser {

    @Override
    public void run(final String inputFile) throws IOException {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos");
        Path outputFile = Paths.get(inputFile).getParent().resolve(
                inputFile + "_" + StringConstants.STANFORD_NLP_POS_OUTPUT_FILE);
        NLPFileUtils.prepareOutputFile(outputFile);
        List<String> lines = NLPFileUtils.readAllLines(inputFile);

        List<String[]> result = new ArrayList<>();

        // creates a StanfordCoreNLP object, with POS tagging
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        for (String line : lines) {
            // create an empty Annotation just with the given text
            Annotation document = new Annotation(line);
            // run all Annotators on this text
            pipeline.annotate(document);
            // these are all the sentences in this document
            // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
            List<CoreMap> sentences = document.get(SentencesAnnotation.class);
            StringBuilder taggedString = new StringBuilder();
            StringBuilder filteredString = new StringBuilder();
            for (CoreMap sentence : sentences) {
                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                    String word = token.get(TextAnnotation.class);
                    String posTag = token.get(PartOfSpeechAnnotation.class);
                    if (allowedTags.contains(posTag)) {
                        filteredString.append(word).append(" ");
                    }
                    taggedString.append(word).append("_").append(posTag).append(" ");
                }
            }
            result.add(new String[]{line, taggedString.toString(), filteredString.toString()});
        }
        NLPFileUtils.writeAllLines(outputFile, result, new String[]{""});
    }
}
