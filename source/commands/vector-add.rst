vector add
==========

**Name**:

geoc vector add

**Description**:

Add a Feature to an existing Layer

**Arguments**:

   * -v --value: A value 'field=value'

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector add -i mypoints.shp -v "id=1" -v "the_geom=POINT(1 1)" -v "name=House"