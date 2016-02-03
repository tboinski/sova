mvn help:evaluate -Dexpression=settings.localRepository | grep -v '[INFO]'  > temp.txt
set /p VAR=<temp.txt
del temp.txt
set file=\owlapi-bin
set repo=%var%%file%
if not exist %repo% mvn install:install-file -Dfile=owlapi-bin.jar -DgroupId=owlapi-bin -DartifactId=owlapi-bin -Dversion=1 -Dpackaging=jar



