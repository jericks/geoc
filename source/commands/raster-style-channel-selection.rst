raster style channel selection
==============================

**Name**:

geoc raster style channel selection

**Description**:

Create a channel selection Raster SLD

**Arguments**:

   * -r --red: The red channel name

   * -g --green: The green channel name

   * -b --blue: The blue channel name

   * -y --gray: The gray channel name

   * -o --opacity: The opacity

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc raster style channel selection -r "red,histogram,0.5" -g "green,normalize,0.25" -b "green,histogram,0.33"