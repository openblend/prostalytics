Prostalytics
============

Prostalytics app for KC


Building and installing
=======================

1) Visit [CapeDwarf Downloads page](http://www.jboss.org/capedwarf/downloads) and download latest release bundle

2) Unpack the zip archve

3) Open console and cd to the unzipped firectory:

    cd CapeDwarf_AS7_1.0.0.Beta4

4) Visit [RestEasy Downloads page](http://sourceforge.net/projects/resteasy/files/Resteasy%20JAX-RS/) and download laters release bundle.

5) Unpack the zip archive, and then extract the content of resteasy-jboss-modules.zip file into CapeDwarf_AS7_1.0.0.Beta4/modules/system/layers/base directory.

6) Run capedwarf:

    bin/capedwarf.sh

Ignore “HQ224054: Failed to broadcast connector configs” errors.

7) Go to prostalytics development root and build prostalytics:

    mvn clean install

8) copy the built artifact to deployment folder:

    cp server/target/prostalytics.war /opt/CapeDwarf_AS7_1.0.0.Beta4/standalone/deployments/

9) Visit: [http://localhost:8080/prostalytics/browse.jsf](http://localhost:8080/prostalytics/browse.jsf)
