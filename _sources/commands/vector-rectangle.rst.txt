vector rectangle
================

**Name**:

geoc vector rectangle

**Description**:

Create a rectangle shape around each feature of the input Layer

**Arguments**:

   * -g --geometry: The geometry expression

   * -w --width: The width of the bounds

   * -h --height: The height of the bounds

   * -p --num-points: The number of points

   * -a --rotation: The angle of rotation

   * -u --unit: The unit can either be degrees(d) or radians(r). The default is degrees.

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector rectangle -i states.shp -o states_rects.shp -g "centroid(geom)" -w 10000 -h 20000