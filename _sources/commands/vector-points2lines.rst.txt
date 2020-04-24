vector points2lines
===================

**Name**:

geoc vector points2lines

**Description**:

Convert points to lines

**Arguments**:

   * -s --sort-field: The Field to sort the field

   * -g --group-field: The Field used create separate Lines

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector points2lines -i points.shp -o lines.shp -s id -g group