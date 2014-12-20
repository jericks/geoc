vector rotate
=============

**Name**:

geoc vector rotate

**Description**:

Rotate Features in a Layer

**Arguments**:

   * -t --theta: The angle of rotation in radians

   * -s --sine: The sine of the angle of rotation in radians

   * -c --cosine: The cosine of the angle of rotation in radians

   * -x --x-coord: The x coordinate of the rotation point

   * -y --y-coord: The y coordinate of the rotation point

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector rotate -i polys.properties -o rotated_polys.shp -t 0.785 -x "getX(centroid(geom))" -y "getY(centroid(geom))"