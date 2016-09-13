vector project
==============

**Name**:

geoc vector project

**Description**:

Project the input Layer to another Projection and save it as the output Layer.

**Arguments**:

   * -s --source-projection: The source projection

   * -t --target-projection: The target projection

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector project -i states.shp -o states_2927.shp -t "EPSG:2927"