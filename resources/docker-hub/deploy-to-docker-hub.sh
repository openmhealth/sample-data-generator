#!/bin/bash

BASEDIR=$(pwd)
TAG=$(git describe --exact-match)

if [[ ! ${TAG} =~ ^v[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
   echo "The tag ${TAG} isn't a valid version tag."
   exit
fi

VERSION=${TAG#v}

cd ${BASEDIR}/backend/docker
docker build -t "openmhealth/omh-sample-data-generator:latest" .
docker build -t "openmhealth/omh-sample-data-generator:${VERSION}" .
docker push "openmhealth/omh-sample-data-generator:latest"
docker push "openmhealth/omh-sample-data-generator:${VERSION}"
