vector updatefield
==================

**Name**:

geoc vector updatefield

**Description**:

Update the values of a Layer's Field

**Arguments**:

   * -d --field: The Field name

   * -v --value: The value

   * -f --filter: The CQL Filter

   * -s --script: Whether the value is a script or not

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector updatefield -i states_xy.shp -f INCLUDE -s -v "return f.geom.centroid.x" -d x