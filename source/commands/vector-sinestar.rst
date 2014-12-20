vector sinestar
===============

**Name**:

geoc vector sinestar

**Description**:

Create a sinestar shape around each feature of the input Layer

**Arguments**:

   * -n --number-of-arms: The number of arms

   * -e --arm-length-ratio: The arm length ratio

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

    geoc vector sinestar -i states.shp -o states_sinestar.shp -n 10 -e 0.75 -p 100