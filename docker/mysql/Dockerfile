FROM mysql:5.5
# Add a database
ENV MYSQL_DATABASE jdbcDB
# Add the content of the sql-scripts/ directory to your image
# All scripts in docker-entrypoint-initdb.d/ are automatically
# executed during container startup
COPY ../../sql-scripts /docker-entrypoint-initdb.d/