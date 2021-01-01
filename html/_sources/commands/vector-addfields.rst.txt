vector addfields
================

**Name**:

geoc vector addfields

**Description**:

Add one or more Fields to the input Layer to create the output Layer

**Arguments**:

   * -f --field: A Field in the format 'name=type'

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector addfields -i states.shp -o states_xy.shp -f x=double -f y=double