vector snap points2lines
========================

**Name**:

geoc vector snap points2lines

**Description**:

Snap points to their nearest line

**Arguments**:

   * -d --search-distance: The distance to search for the closest line

   * -s --snapped-fieldname: The name for the snapped Field

   * -k --other-workspace: The other workspace

   * -y --other-layer: The other layer

   * -o --output-workspace: The output workspace

   * -r --output-layer: The output layer

   * -i --input-workspace: The input workspace

   * -l --input-layer: The input layer

   * --help : Print the help message



**Example**::

    geoc vector snap points2lines -i points.shp -k lines.shp -o snapped.shp -d 2