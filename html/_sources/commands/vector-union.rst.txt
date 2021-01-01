vector union
============

**Name**:

geoc vector union

**Description**:

Union one Layer with another Layer

**Arguments**:

   * -p --postfix-all: Whether to postfix all field names (true) or not (false). If true, all Fields from the this current Schema will have '1' at the end of their name while the other Schema's Fields will have '2'.

   * -d --include-duplicates: Whether or not to include duplicate fields names. Defaults to false. If a duplicate is found a '2' will be added.

   * -m --maxfieldname-length: The maximum new Field name length (mostly to support shapefiles where Field names can't be longer than 10 characters

   * -f --first-postfix: The postfix for fields from the first Layer

   * -s --second-postfix: The postfix for fields from the second Layer

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector union -i states.shp -k clip_layer.shp -o states_union.shp