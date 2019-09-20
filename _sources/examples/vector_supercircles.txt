Create supercirlces for each Feature in a Layer
===============================================

.. code-block:: bash

  geoc vector supercircle -i naturalearth.gpkg -l countries -e 0.45 -p 20 > supercircles.csv

  geoc vector supercircle -i naturalearth.gpkg -l countries -g "centroid(the_geom)" -w 5 -h 5 -e 0.65 -p 40 > supercircles2.csv

  geoc vector envelopes -i naturalearth.gpkg -l countries > envelopes.csv

  geoc vector defaultstyle --color navy -o 0.15 -g polygon > supercircles.sld

  geoc vector defaultstyle --color red -o 0.15 -g polygon > supercircles2.sld

  geoc vector defaultstyle --color wheat -o 0.15 -g polygon > envelopes.sld

  geoc map draw -f vector_supercircles.png -b "-180,-90,180,90" \
    -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
    -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
    -l "layertype=layer file=envelopes.csv style=envelopes.sld" \
    -l "layertype=layer file=supercircles.csv style=supercircles.sld" \
    -l "layertype=layer file=supercircles2.csv style=supercircles2.sld"

.. image:: vector_supercircles.png