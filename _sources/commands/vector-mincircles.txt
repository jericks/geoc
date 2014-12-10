vector mincircles
=================

**Name**:

geoc vector mincircles

**Description**:

Calculate the minimum bounding circles of each feature in the input Layer and save them to the output Layer

**Arguments**:

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector mincircles -i states.shp -o state_mincircles.shp