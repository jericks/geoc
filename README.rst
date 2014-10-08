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
5. Where appropriate, values are expressions (literals, properties, or CQL with functions)
6. Uses `GeoScript Groovy <http://geoscript.org/>`_ for extremely terse code.

Examples
--------

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

Data Sources
------------
By default, vector commands read and write CSV using WKT for geometry fields and raster commands read and write ASCII grids.
But, geoc can read and write any supported GeoTools DataStore or CoverageStore by using Connection Strings. GeoTools uses
connection maps to connect to DataStore's.  geoc connection strings are these connection maps where the key/value pairs are
separated by an '=' sign and multiple key/value pairs are separated by a white space.  Values can be single quoted.
geoc also is smart enough to know that a file ending in '.shp' is a shapefile, a file ending in '.gpkg' is a GeoPackage database.
Here are some examples:

* PostGIS: dbtype=postgis database=postgres host=localhost port=5432 user=postgres passwd=postgres
* H2: dbtype=h2 database=test.db
* Shapefile: "data/states.shp"
* Properties: "data/states.properties"
* GeoPackage: layers.gpkg
* Spatialite: layers.sqlite

Here is a longer example that create 100 random points in a GeoPackage database, get's metadata of that layer, and then finally converts the layer to CSV:::

    >>> geoc vector randompoints -g "0 0 10 10" -n 100 -o test.gpkg -r points100

    >>> geoc vector info -i test.gpkg -l points100

    >>> geoc vector to -i test.gpkg -f csv

Installation
------------
Just download the latest `release <https://github.com/jericks/geoc/release>`_ and put the geoc/bin directory on your path. geoc also requires Java 7.

Getting Help
------------
Each command contains a --help option::

    >>> geoc vector buffer --help
    geoc vector buffer: Buffer the features of the input Layer and save them to the output Layer
    --help                      : Print the help message
    -c (--capstyle) VAL         : The cap style
    -d (--distance) VAL         : The buffer distance
    -i (--input-workspace) VAL  : The input workspace
    -l (--input-layer) VAL      : The input layer
    -o (--output-workspace) VAL : The output workspace
    -q (--quadrantsegments) N   : The number of quadrant segments
    -r (--output-layer) VAL     : The output layer
    -s (--singlesided)          : Whether buffer should be single sided or not

There is also a man page for each subcommand::

    >>> man geoc-vector-buffer
    geoc-vector-buffer(1)                                    geoc-vector-buffer(1)

    NAME
           geoc vector buffer

    DESCRIPTION
           Buffer  the  features  of  the  input Layer and save them to the output
           Layer

    USAGE
           geoc vector randompoints -n 10 -g "1,1,10,10" | geoc vector  buffer  -d
           10

    OPTIONS
           -d --distance: The buffer distance

           -q --quadrantsegments: The number of quadrant segments

           -s --singlesided: Whether buffer should be single sided or not

           -c --capstyle: The cap style

           -o --output-workspace: The output workspace

Finally, there is a bash completion script which makes using geoc with bash much easier.

Install it is your .bash_profile::
    
    source /Users/You/geoc/shell/geoc_bash_comp

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

