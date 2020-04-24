geometry geohash encode
=======================

**Name**:

geoc geometry geohash encode

**Description**:

Encode a Geometry as a GeoHash

**Arguments**:

   * -i --input: The input geometry

   * -t --type: The encoding type (string or long). The default is string.

   * -n --number-of-chars: The number of characters. The default is 9.

   * -d --bit-depth: The bit depth. The default is 52.

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc geometry geohash encode -i "POINT (45 78)"