#!/bin/bash
./gradlew curseforge
curl -i -H "X-API-Key: $MODSIO_API_KEY" -F body="{\"version\":{\"name\":\"$(basename $THE_JAR | sed "s/JAPTA-1.10.2-\(.*\).jar/\1/g")\",\"minecraft\":\"1.12.2\"},\"filename\": \"$(basename $THE_JAR)\"}" -F file=@${THE_JAR}  https://mods.io/mods/1214/versions/create.json
