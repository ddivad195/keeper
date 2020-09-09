#!/bin/bash
docker build -t keeper:latest -f ./Dockerfile --no-cache .
cmd="docker run -e BOT_TOKEN='$1' -v $2:/data keeper:latest"
eval $cmd