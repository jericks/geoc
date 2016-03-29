#!/bin/bash

echo "Encode:"
geoc geometry geohash encode -i "POINT (45 78)"

echo "Decode:"
geoc geometry geohash decode -i uf8vk6wjr

echo "Bounds:"
geoc geometry geohash bounds -b "120, 30, 120.0001, 30.0001"

echo "Neighbors:"
geoc geometry geohash neighbors -i uf8vk6wjr