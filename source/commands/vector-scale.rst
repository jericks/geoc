vector scale
============

**Name**:

geoc vector scale

**Description**:

Scale Feature in a Layer

**Arguments**:

   * -x --x-distance: The x distance

   * -y --y-distance: The y distance

   * -c --x-coord: The x coordinate

   * -d --y-coord: The y coordinate

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector scale -i polys.properties -o scaled_polys.shp -x 5 -y 5 -c "getX(centroid(geom))" -d "getY(centroid(geom))"