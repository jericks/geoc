#!/bin/bash
echo "Convert SLD to YSLD:"
geoc style sld2ysld -i countries.sld > countries.yml
cat countries.yml

echo "Convert YSLD to SLD:"
geoc style ysld2sld -i countries.yml > countries2.sld
cat countries2.sld
