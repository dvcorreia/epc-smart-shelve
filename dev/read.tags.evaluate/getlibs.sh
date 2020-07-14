#!/bin/bash
# Pull Impinj Octane SDK
wget https://support.impinj.com/hc/article_attachments/360002599479/Octane_SDK_Java_3_0_0.zip -O temp.zip
mkdir doc
unzip temp.zip -d doc/octane-sdk
mkdir src/main/resources
mv doc/octane-sdk/lib/OctaneSDKJava-3.0.0.0-jar-with-dependencies.jar src/main/resources/OctaneSDKJava-3.0.0.0-jar-with-dependencies.jar
rm -f temp.zip
