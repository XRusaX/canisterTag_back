#!/bin/bash
APPNAME="disinfection"
PASSWDDB="HTEST"

mysql -u root -p -e "CREATE USER ${APPNAME}@'%' IDENTIFIED BY '${PASSWDDB}';"
mysql -u root -p -e "GRANT ALL PRIVILEGES ON ${APPNAME}.* TO '${APPNAME}'@'%';"
mysql -u root -p -e "FLUSH PRIVILEGES;"

mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS ${APPNAME} CHARACTER SET utf8 COLLATE utf8_bin;"
