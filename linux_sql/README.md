# Introduction
This project is a Bash script that provides a convenient way to manage a PostgreSQL database using Docker containers. It allows users, such as developers and system administrators, to perform common operations like starting, stopping, and creating the database container. The script utilizes technologies like Bash scripting and Docker. By leveraging Docker, the script simplifies the process of setting up and managing the database container. It also incorporates other tools like awk, egrep, xargs, and system commands such as date, df, lscpu, vmstat, and psql to gather system information and perform specific tasks. Overall, the project aims to streamline the management of a PostgreSQL database by providing a user-friendly interface and automating container-related tasks.

# Quick Start
```
# Start a psql instance using psql_docker.sh
./scripts/psql_docker.sh start <db_username> <db_password>

# Create tables using ddl.sql
psql -h localhost -U <db_username> -W -f sql/ddl.sql

# Insert hardware specs data into the DB using host_info.sh
./scripts/host_info.sh <db_username> <db_password>

# Insert hardware usage data into the DB using host_usage.sh
./scripts/host_usage.sh <db_username> <db_password>

# Crontab setup (example: run host_usage.sh every minute)
* * * * * /path/to/scripts/host_usage.sh db_username db_password > /dev/null 2>&1
```

# Implemenation
I started with developing the Bash script 'psql_docker' to handle start, stop, and create operations for the PSQL container. Next, I developed the SQL script 'ddl.sql' to be able to create the tables needed for the database. I then created additional Bash scripts ('host_info.sh' and 'host_usage.sh') to collect hardware information and usage data. The last step was to set up the cron job. I used crontab to run the 'host_usage.sh' script every minute and log it into a log file.
## Architecture
Draw a cluster diagram with three Linux hosts, a DB, and agents (use draw.io website). Image must be saved to the `assets` directory.


![linux cluster](/assets/cluster diagram.png)

## Scripts
Shell script description and usage (use markdown code block for script usage)
- ### psql_docker.sh
  This script is used to start and stop a PostgreSQL container using Docker.
  #### Usage:
  ```.scripts/psql_docker.sh [start|stop]```
- ### host_info.sh
  This script collects hardware information from the host system and inserts it into a PostgreSQL database.
  #### Usage:
  ```.scripts/host_info.sh <psql_host> <psql_port> <db_name> <psql_user> <psql_password>```
- ### host_usage.sh
  This script collects hardware usage information from the host system (CPU and memory usage) and inserts it into a PostgreSQL database.
  #### Usage:
  ```.scripts/host_usage.sh <psql_host> <psql_port> <db_name> <psql_user> <psql_password>```
- ### crontab
  To automate the data collection process, you can set up cron jobs to execute the host_usage.sh script at regular intervals.
  #### Usage:
  ```
  crontab -e

  * * * * * /path/to/host_usage.sh psql_host psql_port db_name psql_user psql_password >> /path/to/logfile.log 2>&1
  ```




## Database Modeling
- host_info

| Column Name      	| Data Type   	|
|:-----------------  |:--------------	|
| id               	| SERIAL      	|
| hostname         	| VARCHAR(50) 	|
| cpu_number       	| INTEGER     	|
| cpu_architecture 	| VARCHAR(20) 	|
| cpu_model        	| VARCHAR(50) 	|
| cpu_mhz          	| INTEGER     	|
| l2_cache         	| INTEGER     	|
| total_mem        	| INTEGER     	|
| timestamp        	| TIMESTAMP   	|

- host_usage

| Column Name      	| Data Type   	|
|:-----------------  |:-------------	|
| timestamp       	| TIMESTAMP   	|
| host_id         	| INTEGER   	  |
| memory_free     	| INTEGER     	|
| cpu_idle        	| NUMERIC   	  |
| cpu_kernel      	| NUMERIC   	  |
| disk_io         	| INTEGER     	|
| disk_available  	| INTEGER     	|

# Test
To test my DDL scripts, I would use PSQL CLI and execute queries to view the tables. As a result, I could query specific rows out of the database and could debug why I was not able to query the host_id in the host_usage.sh script.

# Deployment
I used GitHub and git CLI to manage version control of the app, Docker to host the database, and crontab to run scheduled commands (run host_usage.sh every minute).

# Improvements
Write at least three things you want to improve 
e.g. 
- handle hardware updates 
- blah
- blah
