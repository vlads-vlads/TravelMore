# Travel More
## quickstart
Download and install ```MySQL Server``` (take note of root user's password) as well as ```MySQL Workbench``` or ```MySQL Shell``` (Workbench is recommended for beginners)
### setup database and user
#### In MySQL Workbench
- Click the ```[+]``` icon near the ```MySQL Connections``` label.
- Define the ```Connection Name:``` and leave everything else default.
- Click on the created connection (it will prompt you for the root user's password)
- Click ```File``` > ```New Query Tab```
- Execute the following commands (creates a database and user with all privileges to database):
```
CREATE SCHEMA IF NOT EXISTS `traveldb`;
CREATE USER 'travel_db_user'@'localhost' IDENTIFIED BY 'travel_db_password';
GRANT ALL PRIVILEGES ON traveldb.* TO 'travel_db_user'@'localhost' WITH GRANT OPTION;
```
#### In MySQL Shell
```
\connect root@localhost:3306
```
```
create database `traveldb`;
create user 'travel_db_user'@'localhost' identified by 'travel_db_password';
grant all on traveldb.* to 'travel_db_user'@'localhost';
```