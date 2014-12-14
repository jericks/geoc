vector addxyfields
==================

**Name**:

geoc vector addxyfields

**Description**:

Add a XY Fields

**Arguments**:

   * -x --x-fieldname: The name for the X Field

   * -y --y-fieldname: The name for the Y Field

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector addxyfields -i points.shp -o points_xy.shp -x X_COL -y Y_COL