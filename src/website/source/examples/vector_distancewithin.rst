Select Features within a distance of other Features
===================================================

.. code-block:: bash

    #!/bin/bash

    geoc vector randompoints -g -180,-90,180,90 -n 1000 -o points.shp

    geoc vector randompoints -g -180,-90,180,90 -n 10 -o otherPoints.shp

    geoc vector distancewithin -i points.shp -d 12 -k otherPoints.shp > pointsNearOthers.csv

    geoc vector defaultstyle -g point --color yellow > otherPoints.sld

    cat pointsNearOthers.csv | geoc vector defaultstyle --color red -o 0.75 > pointsNearOthers.sld

    geoc map draw -f vector_distancewithin.png -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
        -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
        -l "layertype=layer file=points.shp" \
        -l "layertype=layer file=otherPoints.shp" \
        -l "layertype=layer file=pointsNearOthers.csv style=pointsNearOthers.sld"
        
.. image:: vector_distancewithin.png
