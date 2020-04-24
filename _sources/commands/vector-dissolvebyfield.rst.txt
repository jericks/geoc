vector dissolvebyfield
======================

**Name**:

geoc vector dissolvebyfield

**Description**:

Dissolve the Features of a Layer by a Field.

**Arguments**:

   * -f --field: The field name

   * -d --id-field: The id field name

   * -c --count-field: The count field name

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector dissolvebyfield -i states.shp -o states_subregions.shp -f SUB_REGION