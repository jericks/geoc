vector dissolveintersecting
===========================

**Name**:

geoc vector dissolveintersecting

**Description**:

Dissolve the intersecting Features of a Layer.

**Arguments**:

   * -d --id-field: The id field name

   * -c --count-field: The count field name

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector dissolveintersecting -i polys -o polys_dissolved -d ID -c COUNT