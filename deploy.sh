#!/bin/bash

# Checkout
git clone https://github.com/destroflyer/birds.git
if [ -n "$1" ]; then
  git checkout $1
fi

# Build
mvn clean install

# Deploy
rm -rf ${2}*
mv assets ${2}
mv target/birds-0.0.1.jar ${2}Birds.jar
curl https://destrostudios.com:8080/apps/3/updateFiles