#!/bin/bash
# Pull Impinj LTK XML code
wget https://support.impinj.com/hc/en-us/article_attachments/203173708/LtkXml-9-21-15.zip -O temp.zip
mkdir doc
unzip temp.zip -d doc/
mkdir src/main/resources
cp doc/ltk/java/ltkjava-with-dependencies.jar src/main/resources/ltkjava-with-dependencies.jar
cp doc/ltk/java/ltkxml.jar src/main/resources/ltkxml.jar

rm -f temp.zip
rm -rf doc/ltk/cs
