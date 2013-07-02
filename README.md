Prostalytics
============

Prostalytics app for KC


Building and installing
=======================

1) Visit [CapeDwarf Downloads page](http://www.jboss.org/capedwarf/downloads) and download latest release bundle

2) Unpack the zip archve

3) Open console and cd to the unzipped firectory:

    cd CapeDwarf_AS7_1.0.0.Beta4

4) Run capedwarf:

    bin/capedwarf.sh

Ignore “HQ224054: Failed to broadcast connector configs” errors.

5) Go to prostalytics development root and build prostalytics:

    mvn clean install

6) copy the built artifact to deployment folder:

    cp server/target/prostalytics-server-1.0.0-SNAPSHOT.war /Users/marko/devel/temp/CapeDwarf_AS7_1.0.0.Beta4/standalone/deployments/

7) Visit: [http://localhost:8080/prostalytics-server-1.0.0-SNAPSHOT/browse.jsf](http://localhost:8080/prostalytics-server-1.0.0-SNAPSHOT/browse.jsf)
