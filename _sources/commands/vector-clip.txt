vector clip
===========

**Name**:

geoc vector clip

**Description**:

Clip the input Layer by the other Layer to produce the output Layer.

**Arguments**:

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector clip -i states.shp -k area_of_interest.shp -o states_clipped.shp