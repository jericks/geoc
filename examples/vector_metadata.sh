#!/bin/bash
echo 'Count # Features'
geoc vector count -i naturalearth.gpkg -l countries

echo 'Info'
geoc vector info -i naturalearth.gpkg -l countries

echo 'Schema'
geoc vector schema -i naturalearth.gpkg -l countries -p

echo 'Validity'
geoc vector validity -i naturalearth.gpkg -l countries

echo 'Unique Values'
geoc vector uniquevalues -i naturalearth.gpkg -l countries -f TYPE