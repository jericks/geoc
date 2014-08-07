geoc
====
geoc is a geospatial command line application that follows the unix philosophy.  Each command does one thing well (buffer a layer, crop a raster) by reading a vector layer as a CSV text stream or a raster layer as an ASCII grid, processing the layer or raster, and then writing out the vector layer as a CSV or a raster layer as an ASCII grid.  Individual commands can be chained together with unix pipes. 

geoc is very much under development (command names may change).  Originally is was developed as a complement to `geometry commands <http://jericks.github.io/geometrycommands/index.html>`_ and to stress test `GeoScript Groovy <http://geoscript.org/>`_. The commands have not been optimized for large datasets.

geoc is built on the shoulders of giants: `GeoTools <http://geotools.org>`_ and the `Java Topology Suite <http://tsusiatsoftware.net/jts/main.html>`_.  geoc just provides a command line application that wraps the herculean effort that the developers of these two libraries have undertaken.

Key features
------------
1. Git style commands.  One command (geoc) and many subcommands (which can be more than one word).
2. By default geoc reads and writes vector layers as CSV and raster layers as ASCII grids.
3. But geoc can read and write to any supported `GeoTools <http://geotools.org>`_ DataStore (Shapefiles, PostGIS, H2) or CoverageStore (GeoTIFF, WorldImage, GTOPO).
4. Commands are looked up using Java's Service Provider Interface (SPI) so the framework is extensible.
5. Where appropriate, values are expressions (literal, properties, or CQL with functions)
6. Uses `GeoScript Groovy <http://geoscript.org/>`_ for extremely terse code.

Examples
--------

List commands::

    geoc list

Count features in a CSV layer::

    cat states.csv | geoc vector count

Buffer feature from a shapefile::

    geoc vector buffer -i earthquakes.shp -o earthquake_buffers.shp

Get the envelope of a layer and then calculate the buffer::

    cat states.csv | geoc vector envelope | geoc vector buffer -d 0.1   

Crop a raster::

    geoc raster crop -i raster.tif -b "-120,-40,120,40" -o raster_croped.tif

Build
-----
Building geoc is very easy but you will need Java 7 and Maven 3.

Check it out::

    git checkout https://github.com/jericks/geoc.git

Build it::

    cd geoc
    mvn clean install

License
-------
geoc is open source and licensed under the MIT License.

