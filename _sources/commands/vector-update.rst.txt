vector update
=============

**Name**:

geoc vector update

**Description**:

Update one Layer with another Layer

**Arguments**:

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector update -i states.shp -k clip_layer.shp -o states_update.shp