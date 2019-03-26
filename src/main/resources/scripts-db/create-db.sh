#!/bin/bash

#
# Description: Script for create the database for the Wallet Service.
#
# Author: slauriano

db_name=wallet_service_db
db_host=localhost
db_user=root

echo "Creating new database $db_name @ $db_host ..."
mysql -h $db_host -u $db_user -p -e "CREATE DATABASE IF NOT EXISTS $db_name"
echo "Database $db_name created!"