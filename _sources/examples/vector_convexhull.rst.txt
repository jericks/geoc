Create Convex Hulls around a Vector Layer
=========================================

.. code-block:: bash

    #!/bin/bash
    geoc vector centroid -i naturalearth.gpkg -l countries | geoc vector convexhull -o countries_convexhull.shp
    geoc vector convexhulls -i naturalearth.gpkg -l countries -o countries_convexhulls.shp

    geoc vector defaultstyle --color "#0066FF" -o 0.15 -g line > countries_convexhull.sld
    geoc vector defaultstyle --color navy -o 0.15 -g polygon > countries_convexhulls.sld

    geoc map draw -f vector_convexhull.png -b "-180,-90,180,90" \
        -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
        -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
        -l "layertype=layer file=countries_convexhull.shp" \
        -l "layertype=layer file=countries_convexhulls.shp"

        
.. image:: vector_convexhull.png
