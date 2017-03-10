# ivyDEextension
This project is an extension to the eclipse-plugin [Apache IvyDE 2.2.0.final-201311091524-RELEASE](https://ant.apache.org/ivy/ivyde/) from the [Apache Software Foundation](https://www.apache.org/).

It is build on top of the latest ApacheIvyDE release (2.2.0.final) and extends its functionallity by providing the possibility to manage credentials - needed to retrieve artifacts from protected repositories - using the eclipse secure storage instead of setting them in plaintext within the ANT-buildscripts or ivySettings.

## Compatability
This project tends to be compatible with the same versions as the source plugin is. Click [here](http://ant.apache.org/ivy/ivyde/history/trunk/compatibility.html) for further information. Compatabilities different to the ApacheIvyDE 2.2.0.final are listed below.

#### Eclipse
The minimal required Eclipse version is Eclipse 4.2 (Juno).

#### JVM
At least Java 1.6 is needed.

## Installation
The installation procedure didn't change. Please click [here](http://ant.apache.org/ivy/ivyde/history/trunk/install.html) for further information.

## Features
The Apache IvyDE-extension contains all the features of the 2.2.0.final version. In addition an additional preference page is provided (Window > Preferences > Ivy > Security) where a user can manage its ivy credentials inside the eclipse secure storage. This credentials are exported to the internal ivy-credential store and retrieved from there in case of a resolve/retrieve.
