Create a Voronoi Diagram
========================

.. code-block:: bash

  geoc vector centroid -i naturalearth.gpkg -l countries | geoc vector voronoi -o countries_voronoi.shp

  geoc vector defaultstyle --color navy -o 0.15 -i countries_voronoi.shp > countries_voronoi.sld

  geoc map draw -f vector_voronoi.png -b "-180,-90,180,90" \
      -l "layertype=layer file=naturalearth.gpkg layername=ocean style=ocean.sld" \
      -l "layertype=layer file=naturalearth.gpkg layername=countries style=countries.sld" \
      -l "layertype=layer file=countries_voronoi.shp layername=countries_voronoi style=countries_voronoi.sld"

.. image:: vector_voronoi.png