#!/bin/bash
# Remove old files to make a clean generation

if [ -d "./libs" ]; then \
	echo "libs exists"; \
else \
	echo "created libs directory"; \
	mkdir libs; \
fi

rm -rf -i libs/*
rm -rf -i read-tags/lib/*


# Pull Impinj Octane SDK
if [ -d "./libs/octane-sdk" ]; then \
	echo "octake-sdk exists"; \
else \
	echo "Created dir for Octane SDK"; \
	mkdir libs/octane-sdk; \
fi

wget https://support.impinj.com/hc/article_attachments/360002599479/Octane_SDK_Java_3_0_0.zip -O libs/octane-sdk/temp.zip
unzip libs/octane-sdk/temp.zip -d libs/octane-sdk
rm -f libs/octane-sdk/temp.zip

# Pull EPC tag coder SDK
if [ -d "./libs/epc-tag-coder" ]; then \
	echo "epc-tag-coder exists"; \
else \
	echo "Created dir for EPC tag coder"; \
	mkdir libs/epc-tag-coder; \
fi
wget https://github.com/jlcout/epctagcoder/releases/download/0.0.6/epctagcoder-0.0.6-SNAPSHOT.jar \
-O libs/epc-tag-coder/epctagcoder-0.0.6-SNAPSHOT.jar


# Copy libraries to project folders
if [ -d "./read-tags/lib" ]; then \
	echo "lib already exist in read-tags project" \
else \
	echo "Created lib dir for read-tags"; \
	mkdir read-tags/lib; \
fi

cp -R libs/octane-sdk/lib/. read-tags/lib
cp -R libs/epc-tag-coder/. read-tags/lib



	


