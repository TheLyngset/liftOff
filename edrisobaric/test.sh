#!/bin/bash


today=$(date +"%Y-%m-%d")
tomorrow=$(date -d"$today + 1 day" +%Y-%m-%d)
echo "tomorrow:$tomorrow"
