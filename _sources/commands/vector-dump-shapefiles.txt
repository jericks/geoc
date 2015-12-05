vector dump shapefiles
======================

**Name**:

geoc vector dump shapefiles

**Description**:

Create shapefiles from the input Layer

**Arguments**:

   * -o --output-directory: The output directory

   * -s --max-shp-size: The maximum shp size

   * -d --max-dbf-size: The maximum dbf size

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    cat pointsAndPolygons.csv | geoc vector dump shapfiles -o shapefiles