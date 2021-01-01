vector geomr
============

**Name**:

geoc vector geomr

**Description**:

Convert a text stream of WKT geometries to a Layer

**Arguments**:

   * -t --text: The text

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    echo "POINT (1 1)" | geom buffer -d 100 | geom random -n 100 | geom dump | geoc vector geomr