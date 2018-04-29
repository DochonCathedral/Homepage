#!/bin/bash

curl -X POST https://content.dropboxapi.com/2/files/upload --header "Authorization: Bearer $MY_BEARER" --header "Dropbox-API-Arg: {\"path\": \"/Homepage/app.jar\",\"mode\": \"add\",\"autorename\": true,\"mute\": false}" --header "Content-Type: application/octet-stream" --data-binary ./build/libs/homepage-0.0.1-SNAPSHOT.jar