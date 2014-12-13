vector create
=============

**Name**:

geoc vector create

**Description**:

Create a new Layer

**Arguments**:

   * -f --field: A Field in the format 'name=type'

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * --help : Print the help message



**Example**::

    geoc vector create -o mypoints.shp -f "the_geom=POINT EPSG:4326" -f "id=int" -f "name=string"