                              BatchDO README
                                  03/02/2011

This README details the BatchDO 2 plugin which automates the workflow for the creation of digital objects, and transfers barcodes placed in the "Instance Type" field into the "Barcode fields". It designed as a RDE (Rapid Data Entry) plugin which so it's functionality is access through the RDE drop-down menu. 

USING PLUGIN

Generating Shootlist - 

Import Shootlist – mapping file automatically generated

Configuring ID generator



SETTING UP DEVELOPMENT ENVIRONMENT

This assumes you already have basic knowledge of how to compile and run Java programs using your IDE of choice.  Once the source have been imported into an IDE project, add all the jar files in the lib folder of the Archivists Toolkit installation folder. Doing so should take care of any dependency issues. Please note that in order to edit the user interface source code files, JFormDesigner is needed. This can be done by hand, but it will be a lot more difficult.


FILE LAYOUT DESCRIPTION

"makep" - This is a simple script that packages up the class and xml files in a
zip file that the JPF (Java Plugin Framework) can load into the AT. It will need to be modified
to work in your particular development environment.  The output directory where your IDE places
the class files need to be specified, and the location of the plugin folder of the AT also needs
to be set.

"plugin.xml" - Describes information about the plugin and associated libraries for the JPF.

"src" - Contains the source code and all associated files. In this case, associated files are
the GUI description files (*.jfd) used by JFormDesigner. JFormDesigner is needed to modify the
user interface code.  Look at comments in source files to to see what each file does.
