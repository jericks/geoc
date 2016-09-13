vector draw
===========

**Name**:

geoc vector draw

**Description**:

Draw a Layer to an Image, PDF, or SVG Document

**Arguments**:

   * -f --file: The output file

   * -t --type: The type of document

   * -w --width: The width

   * -h --height: The height

   * -s --sld-file: The sld file

   * -b --bounds: The bounds

   * -m --layer: The map layer

   * -g --background-color: The background color

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector draw -i states.shp -f image.png -w 600 -h 400 && open image.png