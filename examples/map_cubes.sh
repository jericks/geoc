#!/bin/bash

geoc map cube -o -f map_cube_toner.png -l "layertype=tile type=osm name=stamen-toner"

geoc map cube -o -f map_cube_toner_lite.png -l "layertype=tile type=osm name=stamen-toner-lite"

geoc map cube -o -f map_cube_watercolor.png -l "layertype=tile type=osm name=stamen-watercolor"

geoc map cube -o -f map_cube_terrain.png -l "layertype=tile type=osm name=stamen-terrain"

geoc map cube -o -f map_cube_wikimedia.png -l "layertype=tile type=osm name=wikimedia"

geoc map cube -o -f map_cube_osm.png -l "layertype=tile type=osm name=osm"

geoc map cube -o -f map_cube_usgs_topo.png -l "layertype=tile type=usgs name=usgs-topo"

geoc map cube -o -f map_cube_usgs_shaded.png -l "layertype=tile type=usgs name=usgs-shadedrelief"

geoc map cube -o -f map_cube_usgs_imagery.png -l "layertype=tile type=usgs name=usgs-imagery"

geoc map cube -o -f map_cube_usgs_imagerytopo.png -l "layertype=tile type=usgs name=usgs-imagerytopo"

geoc map cube -o -f map_cube_usgs_hydro.png -l "layertype=tile type=usgs name=usgs-hydro"

