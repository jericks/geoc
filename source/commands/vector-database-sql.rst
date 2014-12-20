vector database sql
===================

**Name**:

geoc vector database sql

**Description**:

Execute SQL commands against a Database Workspace

**Arguments**:

   * -w --database-workspace: The input workspace

   * -s --sql: The input layer

   * --help : Print the help message



**Example**::

    geoc vector database sql -w h2.db -s "insert into "points" ("id", "the_geom", "name") values (1, ST_GeomFromText('POINT(1 1)', 4326), 'point 1')"