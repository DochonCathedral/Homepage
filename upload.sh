#!/bin/bash

curl -X POST https://content.dropboxapi.com/2/files/upload --header "Authorization: Bearer $MY_BEARER" --header "Dropbox-API-Arg: {\"path\": \"/Homepage/app.jar\",\"mode\": \"overwrite\"}" --header "Content-Type: application/octet-stream" --data-binary @./build/libs/homepage-0.0.1-SNAPSHOT.jar

sshpass -p $SSH_PW scp -o StrictHostKeyChecking=no ./build/libs/homepage-0.0.1-SNAPSHOT.jar app@$HOST_IP\:/home/app/app.jar
echo "[Before stopping homepage..]"
sshpass -p $SSH_PW ssh -o StrictHostKeyChecking=no app@$HOST_IP "sudo systemctl status homepage.service"
sshpass -p $SSH_PW ssh -o StrictHostKeyChecking=no app@$HOST_IP "sudo systemctl stop homepage.service"
sleep 10
echo "[After stopping homepage..]"
sshpass -p $SSH_PW ssh -o StrictHostKeyChecking=no app@$HOST_IP "sudo systemctl status homepage.service"
sshpass -p $SSH_PW ssh -o StrictHostKeyChecking=no app@$HOST_IP "sudo systemctl start homepage.service"
echo "[Starting homepage..]"