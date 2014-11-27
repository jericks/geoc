vector count featuresInfeature
==============================

**Name**:

geom vector count featuresInfeature

**Description**:

Count the number of features in a feature

**Arguments**:

   * -f --count-fieldname: The name for the count Field

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector grid -g "0,0,10,10" -x 10 -y 10 -o grid.shp

geoc vector randompoints -g "0 0 10 10" -n 100 -o points.shp

geoc vector count featuresinfeature -i grid.shp -k points.shp -o grid_count.shp