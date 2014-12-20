vector heatmap
==============

**Name**:

geoc vector heatmap

**Description**:

Create a heatmap of the input layer

**Arguments**:

   * -r --radius-pixels: The radius of the density kernel in pixels

   * -a --weight-field: The name of the weight field

   * -p --pixels-per-cell: The resolution of the computed grid

   * -b --bounds: The output bounds

   * -w --width: The output width

   * -h --height: The output height

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * -o --output-raster: The output raster

   * -f --output-raster-format: The output raster format

   * --help : Print the help message



**Example**::

    geoc vector heatmap -i earthquakes.properties -o heatmap.tif -r 50 -w 800 -h 800