.. geoc documentation master file, created by
   sphinx-quickstart on Tue Nov 25 20:56:27 2014.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

geoc Commandline Application
============================

geoc is a geospatial command line application that follows the unix philosophy.  Each command does one thing well (buffer a layer, crop a raster) by reading a vector layer as a CSV text stream or a raster layer as an ASCII grid, processing the layer or raster, and then writing out the vector layer as a CSV or a raster layer as an ASCII grid.  Individual commands can be chained together with unix pipes. 

geoc is very much under development (command names may change).  Originally is was developed as a complement to `geometry commands <http://jericks.github.io/geometrycommands/index.html>`_ and to stress test `GeoScript Groovy <http://geoscript.org/>`_. The commands have not been optimized for large datasets.

geoc is built on the shoulders of giants: `GeoTools <http://geotools.org>`_ and the `Java Topology Suite <http://tsusiatsoftware.net/jts/main.html>`_.  geoc just provides a command line application that wraps the herculean effort that the developers of these two libraries have undertaken.

`PDF <pdf/geoc.pdf>`_

Contents:

.. toctree::
   :maxdepth: 2

   keyfeatures.rst
   datasources.rst
   examples.rst
   commands.rst
   build.rst
   help.rst

Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

