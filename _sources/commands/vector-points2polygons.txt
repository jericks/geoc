vector points2polygons
======================

**Name**:

geoc vector points2polygons

**Description**:

Convert points to polygons

**Arguments**:

   * -s --sort-field: The Field to sort the field

   * -g --group-field: The Field used create separate Lines

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector points2polygons -i points.shp -o polygons.shp -s id -g group