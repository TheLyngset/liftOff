#!/bin/bash
source /home/debian/isobaricGrib/edrisobaric/venv/bin/activate
# Get the current hour
current_hour=$(date +%H)+2 

# Outputs date and time for debugging
date '+Day of Running: %F%nTime of Running: %H:%M:%S'

# An array of valid hours
hours=(0 3 6 9 12 15 18 21)

# Find the last passed valid hour
for hour in ${hours[@]}
do
    if [[ $current_hour -lt $hour ]]
    then
        break
    fi
    passed_hour=$hour
done
now=$(printf "%02d" $(( (10#$passed_hour))))
next_3_hour=$(printf "%02d" $(( ($passed_hour + 3) % 24 )))
next_9_hour=$(printf "%02d" $(( ($passed_hour + 6) % 24 )))
next_3_date=$(date +"%Y-%m-%d")
next_9_date=$(date +"%Y-%m-%d")
if [[ 10#$next_3_hour -lt 10#$passed_hour ]]
then
    next_3_date=$(date -d"$next_3_date + 1 day" +%Y-%m-%d)
fi
if [[ 10#$next_3_hour -eq 10#00 ]]
then
    next_3_hour=03
fi
if [[ 10#$next_9_hour -lt 10#$passed_hour ]]
then
    next_9_date=$(date -d"$next_9_date + 1 day" +%Y-%m-%d)  
fi
if [[ 10#$next_9_hour -eq 10#00 ]]
then
    next_9_hour=03
fi


python3 /home/debian/isobaricGrib/edrisobaric/app_now/app.py --bind_host 0.0.0.0&
python3 /home/debian/isobaricGrib/edrisobaric/app_in_3/app.py --bind_host 0.0.0.0 --time "${next_3_date}T${next_3_hour}:00:00Z"&
python3 /home/debian/isobaricGrib/edrisobaric/app_in_6/app.py --bind_host 0.0.0.0 --time "${next_9_date}T${next_9_hour}:00:00Z"
wait
