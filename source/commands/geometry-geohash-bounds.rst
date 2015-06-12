geometry geohash bounds
=======================

**Name**:

geoc geometry geohash bounds

**Description**:

Calculate the geohashes for the given bounds

**Arguments**:

   * -b --bounds: The input geometry

   * -t --type: The encoding type (string or long). The default is string.

   * -n --number-of-chars: The number of characters. The default is 9.

   * -d --bit-depth: The bit depth. The default is 52.

   * --help : Print the help message



**Example**::

    geoc geometry geohash bounds -b "120, 30, 120.0001, 30.0001"