#!/bin/bash

COMPOSE_FILE=./docker-compose.prod.yml

wait_for_healthy() {
  local SERVICE=$1
  echo ">>> Waiting for $SERVICE to be healthy..."
  while true; do
    STATUS=$(docker inspect --format='{{.State.Health.Status}}' $SERVICE 2>/dev/null)
    if [ "$STATUS" == "healthy" ]; then
      echo ">>> $SERVICE is healthy!"
      break
    fi
    sleep 3
  done
}

if [ "$1" == "first" ]; then
    echo ">>> 첫 배포: 모든 서비스 띄우기"
    docker-compose -f $COMPOSE_FILE up -d --build
    wait_for_healthy budgetSaver-app1
    wait_for_healthy budgetSaver-app2
else
    echo ">>> Rolling update: app1 교체"
    docker compose -f $COMPOSE_FILE up -d --no-deps --build app1
    wait_for_healthy budgetSaver-app1

    echo ">>> Rolling update: app2 교체"
    docker compose -f $COMPOSE_FILE up -d --no-deps --build app2
    wait_for_healthy budgetSaver-app2
fi
