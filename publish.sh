echo "Publishing konfig"
chmod +x gradlew
echo "Publishing konfig"
./gradlew :konfig:publish || exit
echo "Publishing konfig-plugin"
./gradlew :konfig-plugin:publish || exit
echo "Finished publishing konfig"