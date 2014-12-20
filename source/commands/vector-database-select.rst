vector database select
======================

**Name**:

geoc vector database select

**Description**:

Get a Layer from a Database using a SELECT statement

**Arguments**:

   * -w --database-workspace: The input workspace

   * -l --layer-name: The input layer

   * -s --sql: The input layer

   * -g --geometry-field: The geometry field (name|type|projection)

   * -p --primary-key-field: The primary key field names

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * --help : Print the help message



**Example**::

    geoc vector database select -w h2.db -l centroids -o centroids.properties -s "SELECT ST_CENTROID("the_geom") as "the_geom", "id" FROM "polygons"" -g the_geom|Point