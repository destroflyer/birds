#!/bin/bash

git clone https://github.com/destroflyer/birds.git
if [ -n "$1" ]; then
  git checkout $1
fi

mvn clean install