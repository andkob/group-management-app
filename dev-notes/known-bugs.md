## Frontend
* when loading the webpage, the dashboard renders twice
this could be due to the flags in the state controller

* schedule count is wrong because it happens before the schedules
are loaded into the database. This is temporary until we have a persistant
database on the disk

## Backend