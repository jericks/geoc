raster display
==============

**Name**:

geoc raster display

**Description**:

Display a Raster in a simple viewer

**Arguments**:

   * -w --width: The width

   * -h --height: The height

   * -s --sld-file: The sld file

   * -b --bounds: The bounds

   * -m --base-map: The base map (can be a OSM tile set like stamen-toner, stamen-toner-lite, stamen-watercolor, mapquest-street, mapquest-satellite, shapefile, or Groovy script that returns Layers)

   * -g --background-color: The background color

   * -i --input-raster: The input raster

   * -l --input-raster-name: The input raster name

   * -p --input-projection: The input projection

   * --help : Print the help message



**Example**::

    geoc raster display -i raster.tif -w 400 -h 400