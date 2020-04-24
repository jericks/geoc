vector splitbylayer
===================

**Name**:

geoc vector splitbylayer

**Description**:

Split a Layer into separate Layers based on the Feature from another Layer

**Arguments**:

   * -s --split-workspace: The input workspace

   * -p --split-layer: The input layer

   * -f --field: The field name

   * -o --output-workspace: The output workspace

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector splitbylayer -i states.shp -s states_grid.shp -o statesgrid -f col_row