#!/bin/bash

rm envelope.db.*

geoc vector envelope -i naturalearth.gpkg -l places -o envelope.db -r envelope
geoc vector envelopes -i naturalearth.gpkg -l countries -o envelope.db -r envelopes

geoc map draw -f vector_envelope.png -b "-180,-90,180,90" \
    -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer dbtype=h2 database=envelope.db layername=envelope style='stroke=#0066ff stroke-width=0.5 no-fill'" \
    -l "layertype=layer dbtype=h2 database=envelope.db layername=envelopes style='stroke=#ab9b7d stroke-width=0.5 fill=#f5deb3 fill-opacity=0.15'"


