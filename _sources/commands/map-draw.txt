map draw
========

**Name**:

geoc map draw

**Description**:

Draw a Map

**Arguments**:

   * -l --layer: The map layer

   * -i --layer-file: The input layer file

   * -f --file: The output image file

   * -t --type: The type of document

   * -w --width: The width

   * -h --height: The height

   * -b --bounds: The bounds

   * -g --background-color: The background color

   * -p --projection: The projection

   * --help : Print the help message



**Example**::

    geoc map draw -l "layertype=layer dbtype=geopkg database=data/countries.gpkg layername=countries style=data/countries.sld" -l "/layertype=layer file=data/points.csv layername=points style=data/points.sld"