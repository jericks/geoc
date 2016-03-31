#!/bin/bash
echo "Commands:"
geoc list

echo ""
echo "Commands with descriptions:"
geoc list -d

echo ""
echo "Vector Commands:"
geoc list | grep vector

echo ""
echo "Voronoi Command:"
geoc list -d | grep voronoi