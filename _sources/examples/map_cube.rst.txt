Create a Map Cube
=================

.. code-block:: bash

    geoc map cube -f map_cube.png -t -o -i 'Natural Earth' \
        -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
        -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \

.. image:: map_cube.png