#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

vmstat_mb=$(vmstat --unit M)
lscpu_out=$(lscpu)
hostname=$(hostname -f)

cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}')
cpu_model=$(echo "$lscpu_out" | awk '/^Model name:/ {$1=""; print substr($0,2)}')
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | awk '{print $3}')
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | sed 's/K$//')
total_mem=$(echo "$vmstat_mb" --unit M | tail -1 | awk '{print $4}')

timestamp=$(vmstat -t | tail -1 | awk '{print $18, $19}')



insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, timestamp, total_mem) VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $l2_cache, '$timestamp', $total_mem)"

export PGPASSWORD=$psql_password

psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?