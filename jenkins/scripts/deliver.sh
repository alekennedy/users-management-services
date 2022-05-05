#!/usr/bin/env bash

echo 'The following Maven command installs your Maven-built Java application'
echo 'into the local Maven repository, which will ultimately be stored in'
echo 'Jenkins''s local Maven repository (and the "maven-repository" Docker data'
echo 'volume).'
mv target/ArticuloManagement-0.0.1-SNAPSHOT.jar /var/jenkins-builds/articulos-services
