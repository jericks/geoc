geometry offset
===============

**Name**:

geoc geometry offset

**Description**:

Create a Geometry offset from the input Geometry

**Arguments**:

   * -i --input: The input geometry

   * -d --offset: The offset distance

   * -s --quadrant-segements: The number of quadrant segments (defaults to 8)

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc geometry offset -i "LINESTRING (10 0, 10 10)" -d 5 -s 8