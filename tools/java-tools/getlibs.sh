#!/bin/sh

# Get libraries, docs and examples from impinj server

mkdir Octane SDK

wget https://support.impinj.com/hc/article_attachments/360002599479/Octane_SDK_Java_3_0_0.zip -O libs/Octane SDK/temp.zip
unzip libs/Octane SDK/temp.zip
rm -f libs/Octane SDK/temp.zip

# Copy libraries to project folders

mkdir read-tags/lib
cp -R libs/Octane SDK/lib/. read-tags/lib

wget https://github.com/jlcout/epctagcoder/releases/download/0.0.6/epctagcoder-0.0.6-SNAPSHOT.jar -O read-tags/lib/epctagcoder-0.0.6-SNAPSHOT.jar
