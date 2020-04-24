Create Interior Points from a Vector Layer
==========================================

.. code-block:: bash

    geoc vector interiorpoint -i naturalearth.gpkg -l countries -o countries_interiorpoints.shp

    geoc vector defaultstyle --color navy -o 0.75 -i countries_interiorpoints.shp > countries_interiorpoints.sld

    geoc map draw -f vector_interiorpoint.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
        -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
        -l "layertype=layer file=countries_interiorpoints.shp style=countries_interiorpoints.sld"

.. image:: vector_interiorpoint.png
