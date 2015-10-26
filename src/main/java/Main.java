import com.google.common.base.Preconditions;
import parsers.*;
import constants.ParserType;
import constants.TaggingType;

import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * @author sfedosov
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.toString());

    /**
     * @param args 0 - type of NLP parser ("onlp" - OpenNLP, "snlp" - StanfordNLP)
     *             1 - type of tagging "pos" for POS-tagging or "coref" for coreference resolution parsing
     *             2 - input filename with some phrases, absolute path
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException(
                    "This program requires three input arguments:\n" +
                            "0 - type of NLP parser (\"onlp\" - OpenNLP, \"snlp\" - StanfordNLP)\n" +
                            "1 - type of parsing: \"pos\" for POS-tagging, \"coref\" for coreference resolution parsing\n" +
                            "2 - input filename with some phrases, absolute path");
        }
        final ParserType parserType = ParserType.valueOf(args[0]);
        final TaggingType taggingType = TaggingType.valueOf(args[1]);
        final String inputFilePath = args[2];
        Preconditions.checkArgument(!inputFilePath.isEmpty(), "Input file path can't be empty!");
        try {
            final AbstractParser parser = getParser(parserType, taggingType);
            logger.info("Started " + parser.getClass().getName());
            parser.run(inputFilePath);
            logger.info("Parsing completed. Check the new file in directory: " + Paths.get(inputFilePath).getParent());
        } catch (Exception e) {
            logger.severe("Error while parsing your content: " + e);
        }
    }

    private static AbstractParser getParser(ParserType parserType, TaggingType taggingType) {
        AbstractParser abstractParser;
        switch (parserType) {
            case onlp:
                switch (taggingType) {
                    case coref:
                        abstractParser = new OpenNLPCoreferenceResolutionParser();
                        break;
                    case pos:
                        abstractParser = new OpenNLPPOSParser();
                        break;
                    default:
                        throw new IllegalArgumentException("Wrong 2nd argument! Should be \"pos\" for POS-tagging, \"coref\"");
                }
                break;
            case snlp:
                switch (taggingType) {
                    case coref:
                        abstractParser = new StanfordNLPCoreferenceResolutionParser();
                        break;
                    case pos:
                        abstractParser = new StanfordNLPPOSParser();
                        break;
                    default:
                        throw new IllegalArgumentException("Wrong 2nd argument! Should be \"pos\" for POS-tagging, \"coref\"");
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong 1st argument! Should be \"opennlp\" or \"stanfordnlp\"");
        }
        return abstractParser;
    }

}
