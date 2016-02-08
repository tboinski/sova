VAR=$(mvn help:evaluate -Dexpression=settings.localRepository | grep -v '[INFO]')
file="$VAR\owlapi-bin"
if [ ! -d "$file" ]; then
	cd ./lib
	mvn install:install-file -Dfile=owlapi-bin.jar -DgroupId=owlapi-bin -DartifactId=owlapi-bin -Dversion=1 -Dpackaging=jar
	cd ../
fi
mvn clean install