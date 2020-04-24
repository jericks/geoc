vector database index create
============================

**Name**:

geoc vector database index create

**Description**:

Create a database index

**Arguments**:

   * -w --database-workspace: The input workspace

   * -l --layer-name: The input workspace

   * -i --index-name: The input workspace

   * -f --field: The input workspace

   * -u --unique: The input workspace

   * --help : Print the help message

   * --web-help : Open help in a browser



**Example**::

    geoc vector database index create -w points.db -i geom_index -l points50 -f the_geom