echo "Publishing konfig"
chmod +x gradlew
echo "Publishing konfig"
./gradlew :konfig:publishToMavenLocal || exit
echo "Publishing konfig-plugin"
./gradlew :konfig-plugin:publishToMavenLocal || exit
echo "Finished publishing konfig"