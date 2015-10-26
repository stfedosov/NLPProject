# NLPProject
OpenNLP &amp; StanfordNLP-based text taggers

# 1 step
mvn clean install

# 2 step
sh bin/run.sh snlp pos /path/to/file/with/phrases

Application input arguments:

0 - type of NLP parser ("onlp" - OpenNLP, "snlp" - StanfordNLP)

1 - type of tagging "pos" for POS-tagging or "coref" for coreference resolution parsing

2 - input filename with some phrases, absolute path
