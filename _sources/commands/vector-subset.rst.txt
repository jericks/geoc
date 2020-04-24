vector subset
=============

**Name**:

geoc vector subset

**Description**:

Extract a subset of Features from the input Layer

**Arguments**:

   * -f --filter: The CQL Filter

   * -s --sort: The sort field

   * -m --max: The maximum number of Features to include

   * -t --start: The index of the Feature to start at

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector subset -i earthquakes.shp -s "date ASC" -s "title ASC" -t 5 -m 10 -o ten_earthquakes.shp