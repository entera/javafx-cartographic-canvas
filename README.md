# javafx-cartographic-canvas

Clone repositories:

~~~
$ git clone https://github.com/hastebrot/javafx-cartographic-canvas
$ git clone https://github.com/hastebrot/geodata-eu-us-world
~~~

Build and run application:

~~~
$ cd javafx-cartographic-canvas
$ export JAVA_HOME="/usr/lib/jvm/java-8-oracle"
$ ./gradlew :projects/core:run
~~~

## Guides

- Kotlin Programming Language: https://kotlinlang.org/docs/reference/
- JavaFX UI Toolkit: http://docs.oracle.com/javase/8/javase-clienttechnologies.htm
- TornadoFX Lightweight Framework: https://edvin.gitbooks.io/tornadofx-guide/content/ 
- RxJavaFX Reactive Extensions: https://thomasnield.gitbooks.io/rxjavafx-guide/

## Tasks

- [X] 1. Load shapefiles within directory structure
- [X] 2. Calculate bounds of all shapefiles
- [X] 3. Render all geometries on canvas
- [o] 4. Re-render after user interaction
  - [X] On window resize
  - [ ] On mouse interaction
- [ ] 5. Orchestrate all components

