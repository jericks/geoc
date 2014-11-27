Data Sources
============

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
