echo "Building konfig"
chmod +x gradlew
./gradlew wrapper
echo "Building konfig"
./gradlew :konfig:build || exit
echo "Building konfig-plugin"
./gradlew :konfig-plugin:build || exit
echo "Finished building konfig"