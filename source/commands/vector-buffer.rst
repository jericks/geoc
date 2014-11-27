vector buffer
=============

**Name**:

geom vector buffer

**Description**:

Buffer the features of the input Layer and save them to the output Layer

**Arguments**:

   * -d --distance: The buffer distance

   * -q --quadrantsegments: The number of quadrant segments

   * -s --singlesided: Whether buffer should be single sided or not

   * -c --capstyle: The cap style

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector randompoints -n 10 -g "1,1,10,10" | geoc vector buffer -d 10