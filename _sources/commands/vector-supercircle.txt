vector supercircle
==================

**Name**:

geoc vector supercircle

**Description**:

Create a super circle shape around each feature of the input Layer

**Arguments**:

   * -e --power: The power

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

    geoc vector supercircle -i polys.properties -o supercircles.shp -p 40 -e 0.4