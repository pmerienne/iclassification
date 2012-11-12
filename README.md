# What's iclassification?

TODO

# Requirements

This application is compiled with JDK6. The launch scripts set up JVM options that may only be available since latest JDK6 versions.
If you use an old JDK6, please consider upgrading, or edit the scripts for removing the unsupported options.
If you use an JDK < 6, you have to upgrade.

You must have a mongodb 2.0+ instance running on localhost port 27017. Mongodb must have database named iclassification with an admin user (username : 'admin', password : 'admin')

This application use opencv for image segmentation. Opencv must be installed (instruction on 
Install http://www.samontab.com/web/2012/06/installing-opencv-2-4-1-ubuntu-12-04-lts/)

# Getting started

You can download iclassification as a distribution bundle (.tar.gz or .zip) <a href="https://github.com/pmerienne/iclassification/downloads">here</a>. 
Unpack the binary and put the extracted directory into a convenient location.

Launch iclassification using the following command :

bin/startup.sh

# Configure iclassification

You can configure mongodb access in the property file : resources/mongodb.properties

Server (Jetty) configuration are located in the resources/jetty.xml file
