Key Features
============

1. Git style commands.  One command (geoc) and many subcommands (which can be more than one word).

2. By default geoc reads and writes vector layers as CSV and raster layers as ASCII grids.

3. But geoc can read and write to any supported `GeoTools <http://geotools.org>`_ DataStore (Shapefiles, PostGIS, H2) or CoverageStore (GeoTIFF, WorldImage, GTOPO).

4. Commands are looked up using Java's Service Provider Interface (SPI) so the framework is extensible.
   
5. Where appropriate, values are expressions (literals, properties, or CQL with functions)

6. Uses `GeoScript Groovy <http://geoscript.org/>`_ for extremely terse code.
