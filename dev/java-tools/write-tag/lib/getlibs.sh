#!/bin/bash

# Pull Impinj Octane SDK
wget https://support.impinj.com/hc/article_attachments/360002599479/Octane_SDK_Java_3_0_0.zip -O temp.zip
unzip temp.zip -d octane-sdk-docs 
mv octane-sdk-docs/lib/OctaneSDKJava-3.0.0.0-jar-with-dependencies.jar .
mv octane-sdk-docs/lib/OctaneSDKJava-3.0.0.0.jar .
rm -f temp.zip