vector graticule line
=====================

**Name**:

geoc vector graticule line

**Description**:

Create line graticules

**Arguments**:

   * -g --geometry: The geometry

   * -s --spacing: The spacing (defaults to -1)

   * -l --line-definition: Each line definition has comma delimited orientation (vertical or horizontal), level, and spacing)

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector graticule line -g -180,-90,180,90 -l "vertical,2,10" -l "vertical,1,2" -l "horizontal,2,10" -l "horizontal,1,2"