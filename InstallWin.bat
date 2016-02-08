call mvn help:evaluate -Dexpression=settings.localRepository | FindStr /V "[INFO]" > temp.txt
set /p VAR=<temp.txt
del temp.txt
set file=\owlapi-bin\owlapi-bin\1\owlapi-bin-1.jar
set repo=%var%%file%
if not exist %repo% (
	cd ./lib
	call mvn install:install-file -Dfile=owlapi-bin.jar -DgroupId=owlapi-bin -DartifactId=owlapi-bin -Dversion=1 -Dpackaging=jar
	cd ../
)
call mvn clean install
pause