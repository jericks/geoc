Create Square Graticule Layer
=============================

.. code-block:: bash

  geoc vector graticule square -g -180,-90,180,90 -l 20 -o squares.pbf -r squares

  geoc map draw -f vector_graticule_square.png -b "-180,-90,180,90" \
       -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
       -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
       -l "layertype=layer file=squares.pbf style='stroke=#ab9b7d stroke-width=0.5 fill=#f5deb3 fill-opacity=0.15'"

.. image:: vector_graticule_square.png