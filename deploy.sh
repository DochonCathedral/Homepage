#!/bin/bash

sshpass -p $SSH_PW scp -o StrictHostKeyChecking=no ./build/libs/homepage-0.0.1-SNAPSHOT.jar app@$HOST_IP\:/home/app/app.jar
echo ""
echo "[Before stopping homepage..]"
sshpass -p $SSH_PW ssh -o StrictHostKeyChecking=no app@$HOST_IP "sudo systemctl status homepage.service"
sshpass -p $SSH_PW ssh -o StrictHostKeyChecking=no app@$HOST_IP "sudo systemctl stop homepage.service"
sleep 10

echo ""
echo "[After stopping homepage..]"
sshpass -p $SSH_PW ssh -o StrictHostKeyChecking=no app@$HOST_IP "sudo systemctl status homepage.service
"
echo ""
echo "[Starting homepage..]"
sshpass -p $SSH_PW ssh -o StrictHostKeyChecking=no app@$HOST_IP "sudo systemctl start homepage.service"