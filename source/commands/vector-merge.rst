vector merge
============

**Name**:

geoc vector merge

**Description**:

Merge two Layers together to create a new Layer

**Arguments**:

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector merge -i states_SUB_REGION_Pacific.shp -k states_SUB_REGION_Mtn.shp -o states_west.shp