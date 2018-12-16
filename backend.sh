#!/usr/bin/env bash
docker stop backend || true
docker rm backend || true
docker build -t yf-backend .
docker run -d -p 4848:4848 -p 8080:8080 -p 8181:8181 -p 9009:9009 -p 29009:29009 --name backend yf-backend