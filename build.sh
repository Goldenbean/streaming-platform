#!/usr/bin/env bash
mvn clean package

scp streaming-server/target/streaming-server-1.0.0-SNAPSHOT.jar admin@stream-2:~/stream-server/

ssh admin@stream-2 'cd ~/stream-server;sh start.sh'
