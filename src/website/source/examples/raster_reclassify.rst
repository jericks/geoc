Reclassify a Raster
===================

.. code-block:: bash

    #!/bin/sh

    geoc raster reclassify -i pc.tif -o pc_reclass.tif \
        -r 0-0=1 -r 0-50=2 -r 50-200=3 \
        -r 200-1000=5 -r 1000-1500=4 -r 1500-4000=6

    geoc raster style colormap \
        -v 1=#FFFACD -v 2=#F0E68C -v 3=#DAA520 \
        -v 4=#FF4500 -v 5=#800000 -v 6=#F5FFFA > pc_reclass.sld

    geoc map draw -f pc_reclass.png -l "layertype=raster source=pc_reclass.tif style=pc_reclass.sld"


.. image:: raster_reclassify.png