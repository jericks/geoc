#!/bin/bash
geoc vector envelope -i naturalearth.gpkg -l places -o envelope.db -r envelope
geoc vector envelopes -i naturalearth.gpkg -l countries -o envelope.db -r envelopes

geoc vector defaultstyle --color "#0066FF" -o 0.15 -g linestring > envelope.sld
geoc vector defaultstyle --color navy -o 0.15 -g polygon > envelopes.sld

geoc map draw -f vector_envelope.png -b "-180,-90,180,90" \
    -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer dbtype=h2 database=envelope.db layername=envelope style=envelope.sld" \
    -l "layertype=layer dbtype=h2 database=envelope.db layername=envelopes style=envelopes.sld"


