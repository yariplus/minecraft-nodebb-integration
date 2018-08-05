call gradlew.bat :nodebb-integration-common:build
call gradlew.bat :nodebb-integration-bukkit:build
call gradlew.bat :nodebb-integration-sponge:build
call gradlew.bat :nodebb-integration-forge:build
call gradlew.bat :nodebb-integration-common:shadowJar
call gradlew.bat :nodebb-integration-bukkit:shadowJar
call gradlew.bat :nodebb-integration-sponge:shadowJar
call gradlew.bat :nodebb-integration-forge:shadowJar
call gradlew.bat :nodebb-integration-universal:shadowJar
PAUSE 5