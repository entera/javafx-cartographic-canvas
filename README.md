# javafx-cartographic-canvas

Clone project and geodata repositories:

~~~
$ git clone https://github.com/hastebrot/javafx-cartographic-canvas
$ git clone https://github.com/hastebrot/geodata-eu-us-world
~~~

Build and run the application ([Install Oracle JDK on Ubuntu 16.04](https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04)):

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
- [X] 2. Calculate hastebrot.cartocanvas.util.bounds of all shapefiles
- [X] 3. Render all geometries on canvas
- [ ] 4. Re-render after window resize and mouse interaction
- [ ] 5. Orchestrate all components

## Components

- `app.kt`:
  - APP: WorkspaceApp
  - VIEW: WorkspaceView

- `loader.kt`:
  - VIEW: ShapefileListerView
  - VIEW: ShapefileLoaderView
  - SERVICE: ShapefileLoaderService
  - MODEL: ShapefileLister
 
- `info.kt`:
  - VIEW: ShapefileInfoView
  - VIEW: ShapefileBoundsView
  - SERVICE: ShapefileInfoService
  - MODEL: ShapefileInfo

- `map.kt`:
  - VIEW: MapCanvasView
  - SERVICE: ScaleService / ViewportService
  - SERVICE: RenderService
  - MODEL: Scale
  - MODEL: Bounds
  - MODEL: Style

- `nav.kt`:
  - SERVICE: WindowService
  - SERVICE: MouseService

## Tools

- cd: class diagram
- sd: sequence diagram
