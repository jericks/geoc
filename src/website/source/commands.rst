Commands
========

To see all commands use the list subcommand::

    geoc list

To see the version of geoc that you are using use the version subcommand::

    geoc version

You can easily combine commands together using the unix pipe operator::

    geoc vector randompoints -n 10 -g "-180,-90,180,90"  | geoc vector buffer -d 5

To avoid starting up Java two separate times, use the pipe subcommand::

    geoc pipe -c "vector randompoints -n 10 -g '-180,-90,180,90' | vector buffer -d 5"

You can also use the shell subcommand to start an interactive geoc session::

    geoc shell
    geoc> vector randompoints -n 10 -g '-180,-90,180,90'

If a command has alot of arguments you can use the argument file syntax.

Save the arguments to a separate file as args.txt::

    -g
    -180,-90,180,90
    -n
    10

And then use it with the geoc command::

    geoc vector randompoints @args.txt

.. toctree::
    commands/list.rst
    commands/version.rst
    commands/pipe.rst
    commands/shell.rst
    commands/filter-cql2xml.rst
    commands/geometry-convert.rst
    commands/geometry-dd2pt.rst
    commands/geometry-geohash-bounds.rst
    commands/geometry-geohash-decode.rst
    commands/geometry-geohash-encode.rst
    commands/geometry-geohash-neighbors.rst
    commands/geometry-greatcirclearc.rst
    commands/geometry-offset.rst
    commands/geometry-orthodromicdistance.rst
    commands/geometry-plot.rst
    commands/geometry-pt2dd.rst
    commands/map-cube.rst
    commands/map-draw.rst
    commands/proj-envelope.rst
    commands/proj-wkt.rst
    commands/raster-abs.rst
    commands/raster-add-constant.rst
    commands/raster-add.rst
    commands/raster-animatedgif.rst
    commands/raster-contour.rst
    commands/raster-convolve.rst
    commands/raster-crop-with-geometry.rst
    commands/raster-crop-with-layer.rst
    commands/raster-crop.rst
    commands/raster-display.rst
    commands/raster-divide-constant.rst
    commands/raster-divide.rst
    commands/raster-draw.rst
    commands/raster-envelope.rst
    commands/raster-exp.rst
    commands/raster-extractfootprint.rst
    commands/raster-get-value.rst
    commands/raster-info.rst
    commands/raster-invert.rst
    commands/raster-log.rst
    commands/raster-mapalgebra.rst
    commands/raster-mosaic.rst
    commands/raster-multiply-constant.rst
    commands/raster-multiply.rst
    commands/raster-normalize.rst
    commands/raster-point.rst
    commands/raster-polygon.rst
    commands/raster-project.rst
    commands/raster-projection.rst
    commands/raster-reclassify.rst
    commands/raster-resample.rst
    commands/raster-scale.rst
    commands/raster-shadedrelief.rst
    commands/raster-size.rst
    commands/raster-style-channel-selection.rst
    commands/raster-style-colormap.rst
    commands/raster-style-contrast-enhancement.rst
    commands/raster-style-default.rst
    commands/raster-style-shadedrelief.rst
    commands/raster-stylize.rst
    commands/raster-subtract-constant.rst
    commands/raster-subtract.rst
    commands/raster-to.rst
    commands/raster-worldfile.rst
    commands/style-create.rst
    commands/style-css2sld.rst
    commands/style-repository-copy.rst
    commands/style-repository-delete.rst
    commands/style-repository-get.rst
    commands/style-repository-list.rst
    commands/style-repository-save.rst
    commands/style-sld2ysld.rst
    commands/style-uniquevaluesfromtext.rst
    commands/style-ysld2sld.rst
    commands/tile-delete.rst
    commands/tile-generate.rst
    commands/tile-get-bounds.rst
    commands/tile-list-tiles.rst
    commands/tile-pyramid.rst
    commands/tile-stitch-raster.rst
    commands/tile-stitch-vector.rst
    commands/tile-vector-grid.rst
    commands/vector-add.rst
    commands/vector-addareafield.rst
    commands/vector-addfields.rst
    commands/vector-addidfield.rst
    commands/vector-addlengthfield.rst
    commands/vector-addxyfields.rst
    commands/vector-append.rst
    commands/vector-arc.rst
    commands/vector-arcpolygon.rst
    commands/vector-barnessurface.rst
    commands/vector-buffer.rst
    commands/vector-centroid.rst
    commands/vector-clip.rst
    commands/vector-compareschemas.rst
    commands/vector-contains.rst
    commands/vector-convexhull.rst
    commands/vector-convexhulls.rst
    commands/vector-coordinates.rst
    commands/vector-copy.rst
    commands/vector-count-featuresInfeature.rst
    commands/vector-count.rst
    commands/vector-create.rst
    commands/vector-database-index-create.rst
    commands/vector-database-index-delete.rst
    commands/vector-database-index-list.rst
    commands/vector-database-remove.rst
    commands/vector-database-select.rst
    commands/vector-database-sql.rst
    commands/vector-datastorelist.rst
    commands/vector-datastoreparams.rst
    commands/vector-defaultstyle.rst
    commands/vector-delaunay.rst
    commands/vector-delete.rst
    commands/vector-densify.rst
    commands/vector-display.rst
    commands/vector-dissolvebyfield.rst
    commands/vector-dissolveintersecting.rst
    commands/vector-distancewithin.rst
    commands/vector-draw.rst
    commands/vector-dump-shapefiles.rst
    commands/vector-ellipse.rst
    commands/vector-envelope.rst
    commands/vector-envelopes.rst
    commands/vector-erase.rst
    commands/vector-filter.rst
    commands/vector-fix.rst
    commands/vector-from.rst
    commands/vector-geomr.rst
    commands/vector-geomw.rst
    commands/vector-gradientstyle.rst
    commands/vector-graticule-hexagon.rst
    commands/vector-graticule-line.rst
    commands/vector-graticule-oval.rst
    commands/vector-graticule-rectangle.rst
    commands/vector-graticule-square.rst
    commands/vector-grid.rst
    commands/vector-heatmap.rst
    commands/vector-identity.rst
    commands/vector-info.rst
    commands/vector-interiorpoint.rst
    commands/vector-intersection.rst
    commands/vector-intersects.rst
    commands/vector-join-attribute.rst
    commands/vector-join-spatial.rst
    commands/vector-largestemptycircle.rst
    commands/vector-list-layers.rst
    commands/vector-merge.rst
    commands/vector-mincircle.rst
    commands/vector-mincircles.rst
    commands/vector-minrect.rst
    commands/vector-minrects.rst
    commands/vector-multiple2single.rst
    commands/vector-octagonalenvelope.rst
    commands/vector-octagonalenvelopes.rst
    commands/vector-page.rst
    commands/vector-points2lines.rst
    commands/vector-points2polygons.rst
    commands/vector-pointsalongline.rst
    commands/vector-pointstacker.rst
    commands/vector-project.rst
    commands/vector-randompoints.rst
    commands/vector-raster-values.rst
    commands/vector-raster.rst
    commands/vector-rectangle.rst
    commands/vector-reflect.rst
    commands/vector-remove-layer.rst
    commands/vector-removefields.rst
    commands/vector-rotate.rst
    commands/vector-scale.rst
    commands/vector-schema.rst
    commands/vector-shear.rst
    commands/vector-simplify.rst
    commands/vector-sinestar.rst
    commands/vector-single2multiple.rst
    commands/vector-smooth.rst
    commands/vector-snap-points2lines.rst
    commands/vector-sort.rst
    commands/vector-splitbyfield.rst
    commands/vector-splitbylayer.rst
    commands/vector-squircle.rst
    commands/vector-subset.rst
    commands/vector-supercircle.rst
    commands/vector-symdifference.rst
    commands/vector-to.rst
    commands/vector-transform.rst
    commands/vector-translate.rst
    commands/vector-union.rst
    commands/vector-uniquevalues.rst
    commands/vector-uniquevaluesstyle.rst
    commands/vector-update.rst
    commands/vector-updatefield.rst
    commands/vector-validity.rst
    commands/vector-voronoi.rst


