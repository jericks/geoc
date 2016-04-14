#!/bin/bash
rm points.db.*

echo "Generating 10 random points:"
geoc vector randompoints -g -180,-90,180,90 -n 10 -o "type=h2 file=points.db" -r points
geoc vector schema -i "type=h2 file=points.db" -l points -p

echo "Adding ID field:"
geoc vector addidfield -i "type=h2 file=points.db" -l points -o "type=h2 file=points.db" -r points_id -f fid -s 1
geoc vector schema -i "type=h2 file=points.db" -l points_id -p

echo "Adding XY fields:"
geoc vector addxyfields -i "type=h2 file=points.db" -l points_id -o "type=h2 file=points.db" -r points_id_xy -x X_COL -y Y_COL -a centroid
geoc vector schema -i "type=h2 file=points.db" -l points_id_xy -p

echo "Add LABEL field:"
geoc vector addfields -i "type=h2 file=points.db" -l points_id_xy -o "type=h2 file=points.db" -r points_id_xy_flds -f label=string -f value=double
geoc vector schema -i "type=h2 file=points.db" -l points_id_xy_flds -p

echo "Update values:"
geoc vector updatefield -i "type=h2 file=points.db" -l points_id_xy_flds -d label -v "'point label'"
geoc vector updatefield -i "type=h2 file=points.db" -l points_id_xy_flds -d value -v "id * 10"
geoc vector copy -i "type=h2 file=points.db" -l points_id_xy_flds


echo "Create Polygons:"
geoc vector buffer -d 10 -i "type=h2 file=points.db" -l points -o "type=h2 file=points.db" -r polys
geoc vector schema -i "type=h2 file=points.db" -l polys -p

echo "Add Area field:"
geoc vector addareafield -i "type=h2 file=points.db" -l polys -o "type=h2 file=points.db" -r polys_area
geoc vector schema -i "type=h2 file=points.db" -l polys_area -p

echo "Add Length field:"
geoc vector addlengthfield -i "type=h2 file=points.db" -l polys_area -o "type=h2 file=points.db" -r polys_area_len
geoc vector schema -i "type=h2 file=points.db" -l polys_area_ln -p
geoc vector copy -i "type=h2 file=points.db" -l polys_area_len