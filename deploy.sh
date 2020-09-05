#!/bin/bash

VERSION=$1
CLIENT=$2

# Checkout
git clone https://github.com/destroflyer/birds.git
if [ -n "$VERSION" ]; then
  git checkout $VERSION
fi

# Build
mvn clean install

# Deploy
rm -rf ${CLIENT}*
mv assets ${CLIENT}
mv target/birds-0.0.1.jar ${CLIENT}Birds.jar
curl https://destrostudios.com:8080/apps/3/updateFiles