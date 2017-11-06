style uniquevaluesfromtext
==========================

**Name**:

geoc style uniquevaluesfromtext

**Description**:

Create a Style from reading values in the unique values format

**Arguments**:

   * -f --field: The field

   * -g --geometry-type: The geometry type (point, linestring, polygon)

   * -i --input: The input file or url

   * -t --type: The output type (sld or ysld)

   * -o --output: The output file

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc style uniquevaluesfromtext -f unit -g Polygon -i units.txt -o units.sld