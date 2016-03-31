#!/bin/bash
echo "GeoTools DataStores:"
geoc vector datastorelist

echo ""
echo  "PostGIS Params:"
geoc vector datastoreparams -n PostGIS

echo ""
echo "GeoPackage Params:"
geoc vector datastoreparams -n GeoPackage