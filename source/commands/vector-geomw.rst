vector geomw
============

**Name**:

geoc vector geomw

**Description**:

Convert the input layer to a text stream of WKT geometries that can be read by the geom commands

**Arguments**:

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector geomw -i states.shp | geom combine | geom draw && open image.png