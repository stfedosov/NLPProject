package constants;

/**
 * @author sfedosov
 */
public class StringConstants {
    public static final String[] ALLOWED_TAGS = new String[]{"NNP", "NNS", "NN", "JJ"};
    public static final String POS_MAXENT_TAGGER_LOCATION = "/pos_models/en-pos-maxent.bin";
    public static final String OPEN_NLP_POS_OUTPUT_FILE = "open_nlp_pos_parsed.csv";
    public static final String STANFORD_NLP_POS_OUTPUT_FILE = "stanford_nlp_pos_parsed.csv";
    public static final String STANFORD_NLP_COREF_OUTPUT_FILE = "stanford_nlp_coref_parsed.csv";
    public static final String OPEN_NLP_COREF_OUTPUT_FILE = "open_nlp_coref_parsed.csv";
    public static final String COREF_RES_MODELS_LOCATION = "/coref";
    public static final String PARSER_CHUNKER_LOCATION = "/chunking_model/en-parser-chunking.bin";
    public static final String WORD_NET_DICT_LOCATION = "/word_net_dict";
}
