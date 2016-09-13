vector delete
=============

**Name**:

geoc vector delete

**Description**:

Delete features from a Layer in place

**Arguments**:

   * -f --filter: The CQL Filter

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector delete -i states.shp -f "STATE_NAME = 'Washington'" -i states_no_wash.shp