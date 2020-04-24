vector removefields
===================

**Name**:

geoc vector removefields

**Description**:

Remove one or more Fields from the input Layer to create the output Layer

**Arguments**:

   * -f --field: A Field name

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector removefields -i states.shp -o states_temp.shp -f description -f name -f boundedBy