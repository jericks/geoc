vector uniquevaluesstyle
========================

**Name**:

geoc vector uniquevaluesstyle

**Description**:

Create an SLD document where each unique value in the Layer is a rule.

**Arguments**:

   * -f --field: The field name

   * -c --colors: The color brewer palette name or a list of colors (space delimited)

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector uniquevaluesstyle -i states.shp -f STATE_ABBR -c "Greens"