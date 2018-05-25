Examples
========

List commands::

    >>> geoc list

Count features in a CSV layer::

    >>> cat states.csv | geoc vector count

Buffer feature from a shapefile::

    >>> geoc vector buffer -i earthquakes.shp -o earthquake_buffers.shp

Get the envelope of a layer and then calculate the buffer::

    >>> cat states.csv | geoc vector envelope | geoc vector buffer -d 0.1   

Crop a raster::

    >>> geoc raster crop -i raster.tif -b "-120,-40,120,40" -o raster_croped.tif

Create 100 random points in a GeoPackage database, get's metadata of that layer, and then finally converts the layer to CSV:::

    >>> geoc vector randompoints -g "0 0 10 10" -n 100 -o test.gpkg -r points100

    >>> geoc vector info -i test.gpkg -l points100

    >>> geoc vector to -i test.gpkg -f csv

.. toctree::
    examples/vector_buffer.rst
    examples/vector_centroid.rst
    examples/vector_convexhull.rst
    examples/vector_envelope.rst
    examples/vector_random.rst
    examples/vector_voronoi.rst
    examples/vector_delaunay.rst
    examples/vector_intersects.rst
    examples/vector_contains.rst
    examples/vector_distancewithin.rst
    examples/map_cube.rst
    examples/tile_geopackage_geodetic.rst
    examples/tile_mbtiles.rst
    examples/raster_reclassify.rst
    examples/raster_vectorize.rst
  
