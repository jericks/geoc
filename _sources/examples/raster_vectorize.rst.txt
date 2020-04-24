Create Polygons from a Raster
=============================

.. code-block:: bash

    #!/bin/sh

    geoc raster reclassify -i pc.tif -o pc_reclass.tif \
        -r 0-0=1 -r 0-50=2 -r 50-200=3 \
        -r 200-1000=5 -r 1000-1500=4 -r 1500-4000=6

    geoc raster polygon -e -i pc_reclass.tif -o pc_reclass_poly.shp

    geoc vector uniquevaluesstyle -i pc_reclass_poly.shp -f value \
        -c BoldLandUse > pc_reclass_poly.sld

    geoc map draw -f pc_reclass_poly.png \
        -l "layertype=layer file=pc_reclass_poly.shp style=pc_reclass_poly.sld"

.. image:: raster_vectorize.png