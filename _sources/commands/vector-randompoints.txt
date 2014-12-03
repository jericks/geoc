vector randompoints
===================

**Name**:

geoc vector randompoints

**Description**:

Create a new Layer with randomly placed points

**Arguments**:

   * -n --number: The number of points

   * -p --projection: The projection

   * -g --geometry: The geometry

   * -d --grid: Whether to create random points in grid

   * -c --constrained-to-circle: Whether the points should be constrained to a circle or not

   * -f --gutter-fraction: The size of the gutter between cells

   * -e --geom-fieldname: The geometry field name

   * -u --id-fieldname: The id field name

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * --help : Print the help message



**Example**::

    geoc vector randompoints -n 10 -g "1,1,10,10"