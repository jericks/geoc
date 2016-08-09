.. image:: https://travis-ci.org/jericks/geoc.svg?branch=master
    :target: https://travis-ci.org/jericks/geoc

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

Here is a longer example that create 100 random points in a GeoPackage database, get's metadata of that layer, and then finally converts the layer to CSV:::

    >>> geoc vector randompoints -g "0 0 10 10" -n 100 -o test.gpkg -r points100

    >>> geoc vector info -i test.gpkg -l points100

    >>> geoc vector to -i test.gpkg -f csv

Data Sources
------------
By default, vector commands read and write CSV using WKT for geometry fields and raster commands read and write ASCII grids.
But, geoc can read and write any supported GeoTools DataStore or CoverageStore by using Connection Strings. GeoTools uses
connection maps to connect to DataStore's.  geoc connection strings are these connection maps where the key/value pairs are
separated by an '=' sign and multiple key/value pairs are separated by a white space.  Values can be single quoted.

Vector
------
**PostGIS**

    dbtype=postgis database=postgres host=localhost port=5432 user=postgres passwd=postgres

**MySQL**

    dbtype=mysql database=layers host=localhost port=5432 user=me passwd=s$cr$t

**H2**

    test.db

    dbtype=h2 database=test.db

    dbtype=h2 host=localhost port=5432 schema=public user=me password=s$cr$t

    dbtype=h2 jndiReferenceName=layers schema=public

**Shapefile**

    url=data/states.shp

    data/states.shp

**Memory**

    memory

**Properties**

    data/states.properties

    directory=data/properties

**GeoPackage**

    layers.gpkg

    database=layers.gpkg dbtype=geopkg user=me passwd=s$cr$t

**Geobuf**

    layer.pbf

    file=layers precision=6 dimension=2

**Spatialite**

    layers.sqlite

    dbtype=spatialite database=layers.sqlite

**OGR**

    DatasourceName=states.shp DriverName='ESRI Shapefile' namespace=shp

**WFS**

    http://geoserver.org/wfs?request=getcapabilities

Raster
------

Raster sources are currently all file based.

    data/earth.tif

    world.png

Tile
----

**pyramid**

    Several tile layers can take a pyramid attribute.  You can use one of several well known pyramid names:

    * globalmercator
    * mercator
    * globalmercatorbottomleft
    * globalgeodetic
    * geodetic

    or use a file that contains pyramid metadata in csv, xml, or json format.


**mbtiles**

    type=mbtiles file=states.mbtiles

    type=mbtiles file=states.mbtiles name=states description='The united states'

    states.mbtiles


**geopackage**

    type=geopackage file=states.gpkg name=states pyramid=globalmercator

    states.gpkg

**tms**

    type=tms file=/Users/you/tms format=jpeg

    type=tms file=/Users/you/tms format=png name=tms pyramid=geodetic

**osm**

    type=osm url=http://a.tile.openstreetmap.org

    type=osm urls=http://a.tile.openstreetmap.org,http://b.tile.openstreetmap.org

**utfgrid**

    type=utfgrid file=/Users/me/tiles/states

**vectortiles**

    type=vectortiles name=states file=/Users/me/tiles/states format=mvt pyramid=GlobalMercator

    type=vectortiles name=states url=http://vectortiles.org format=pbf pyramid=GlobalGeodetic

Map Layer
---------

Map layer strings contain a layertype, layername, layerprojection, and style properties.

**layertype**

    * layer
    * raster
    * tile

For layer layertype, you can use the same key value pairs used to specify a Workspace.

For raster layertype, you specify a source=file key value pair.

For tile layertype, you use the same key value pairs used to specify a tile layer.

**layername**

    The name of the layer

**style**

    A SLD or CSS File

**Examples**

    layertype=layer dbtype=geopkg database=/Users/user/Desktop/countries.gpkg layername=countries style=/Users/user/Desktop/countries.sld

    layertype=layer file=/Users/user/Desktop/geoc/polygons.csv layername=polygons style=/Users/user/Desktop/geoc/polygons.sld

    layertype=layer file=/Users/user/Desktop/geoc/points.properties style=/Users/user/Desktop/geoc/points.sld

    layertype=layer file=/Users/user/Projects/geoc/src/test/resources/polygons.shp

    layertype=layer directory=/Users/user/Projects/geoc/src/test/resources/points.properties layername=points

    layertype=raster source=rasters/earth.tif

    layertype=tile file=world.mbtiles

    layertype=tile type=geopackage file=states.gpkg

Installation
------------
Just download the latest `release <https://github.com/jericks/geoc/releases>`_ and put the geoc/bin directory on your path. geoc also requires Java 8.

If you want to use the OGR Workspace, you need to install the GDAL/OGR native library compiled with JNI support and then set the GEOC_GDAL_HOME variable.

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

