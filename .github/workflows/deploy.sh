#!/bin/bash

COMPOSE_FILE=docker/docker-compose.prod.yml

if [ "$1" == "first" ]; then
    echo ">>> 첫 배포: 모든 서비스 띄우기"
    docker-compose -f $COMPOSE_FILE up -d --build
else
    echo ">>> 재배포: Spring 서버만 무중단 교체"
    docker-compose -f $COMPOSE_FILE up -d --no-deps --build app1
    docker-compose -f $COMPOSE_FILE up -d --no-deps --build app2
fi
