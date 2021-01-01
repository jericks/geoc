vector simplify
===============

**Name**:

geoc vector simplify

**Description**:

Simplify the features of the input Layer and save them to the output Layer

**Arguments**:

   * -a --algorithm: The simplify algorithm (DouglasPeucker - dp or TopologyPreserving - tp)

   * -d --distance: The distance tolerance

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector simplify -i states.shp -o states_simplified.shp -a DouglasPeucker -d 100