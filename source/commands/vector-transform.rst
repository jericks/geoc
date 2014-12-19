vector transform
================

**Name**:

geoc vector transform

**Description**:

Transform the values of the input Layer using Expression and Functions

**Arguments**:

   * -d --definition: A transform definition 'field=expression'

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector transform -i point.properties -i points_transformed.shp -d "the_geom=buffer(the_geom,10)" -d "name=strToUpperCase(name)" -d "distance=distance * 10"