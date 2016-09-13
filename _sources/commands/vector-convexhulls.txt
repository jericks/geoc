vector convexhulls
==================

**Name**:

geoc vector convexhulls

**Description**:

Calculate the convex hull of each feature in the input Layer and save them to the output Layer

**Arguments**:

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector convexhulls -i states.shp -o state_convexhulls.shp