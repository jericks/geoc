vector join attribute
=====================

**Name**:

geoc vector join attribute

**Description**:

Perform a attribute join between a Layers and a table.

**Arguments**:

   * -s --table-source: The table source

   * -t --table-name: The table name

   * -y --layer-field: The input layer field name

   * -j --table-field: The other layer field name

   * -n --field: The join field names to include in the output layer

   * -m --only-include-matching: The flag to whether only include matching rows

   * -p --options: The options (for csv separator and quote, for dbf encoding)

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector join attribute -i polygons.shp -s table.csv -o polygons_table.shp -y id -j key -n name -n descriptions