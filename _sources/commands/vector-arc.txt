vector arc
==========

**Name**:

geoc vector arc

**Description**:

Create a arc shape around each feature of the input Layer

**Arguments**:

   * -s --start-angle: The start angle

   * -e --end-angle: The end angle

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



**Example**::

    geoc vector arc -i states.shp -o states_arc.shp -p 100 -s 45 -e 90