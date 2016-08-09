Data Sources
============

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
