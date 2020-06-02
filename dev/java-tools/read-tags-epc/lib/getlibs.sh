#!/bin/bash

# Pull Impinj Octane SDK
wget https://support.impinj.com/hc/article_attachments/360002599479/Octane_SDK_Java_3_0_0.zip -O temp.zip
unzip temp.zip -d octane-sdk-docs 
mv octane-sdk-docs/lib/OctaneSDKJava-3.0.0.0-jar-with-dependencies.jar .
mv octane-sdk-docs/lib/OctaneSDKJava-3.0.0.0.jar .
rm -f temp.zip

# Pull TDT Fosstrak
wget https://oss.sonatype.org/content/repositories/public/org/fosstrak/tdt/tdt/1.0.0/tdt-1.0.0-with-dependencies.jar

# Get TDT schemes
wget https://github.com/Fosstrak/fosstrak-tdt/archive/tdt-1.0.0.zip -O temp.zip
unzip temp.zip -d tdt-source
rm -f temp.zip