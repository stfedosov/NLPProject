# NLPProject
OpenNLP &amp; StanfordNLP-based text taggers

This application can read your string data (a column of phrases are preferable) and process it with using POS-tagger/Coreference resolution parser. In the end of processing this program creates a corresponding ".csv" file with parsing results. 

# 1 step
mvn clean install

# 2 step
sh bin/run.sh snlp pos /path/to/file/with/phrases

Application input arguments:

0 - type of NLP parser ("onlp" - OpenNLP, "snlp" - StanfordNLP)

1 - type of tagging "pos" for POS-tagging or "coref" for coreference resolution parsing

2 - input filename with some phrases, absolute path

# Technical details

1. Application requires Java 1.8
2. Don't forget to update your maven settings.xml file with
<repository>
    <id>MavenCentral</id>
    <name>Maven repository</name>
    <url>http://repo1.maven.org/maven2</url>
 </repository>
