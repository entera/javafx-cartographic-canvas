package hastebrot.cartocanvas

import javafx.scene.control.ListView
import org.geotools.data.FileDataStoreFinder
import org.geotools.data.shapefile.ShapefileDataStore
import tornadofx.Controller
import tornadofx.View
import tornadofx.listview
import tornadofx.observable
import tornadofx.singleAssign
import tornadofx.stackpane
import java.io.File

//-------------------------------------------------------------------------------------------------
// CLASSES.
//-------------------------------------------------------------------------------------------------

class ShapefileListerView : View() {
    val prefs by inject<WorkspaceApp.WorkspacePrefs>()

    val loader by inject<ShapefileLoaderController>()
    var shapefileListView by singleAssign<ListView<String>>()

    override val root = stackpane {
        shapefileListView = listview<String>()
    }

    override fun onDock() {
        val shapefilesDir = loader.findShapefilesDir(prefs.currentDir, prefs.sourceDir)
        val shapefileFiles = loader.listShapefileFiles(shapefilesDir)

        val shapefiles = shapefileFiles
            .map { loader.loadShapefile(it) }

        shapefileListView.items = shapefiles
            .map { it.dataStore.featureSource.name.localPart }
            .observable()

        shapefiles
            .forEach { printShapefileInfo(it) }

        shapefiles
            .map { it.dataStore.featureSource }
            .forEach { printFeatureSourceInfo(it) }
    }
}

class ShapefileLoaderController : Controller() {
    private val SHAPEFILE_EXT = "shp"

    fun findShapefilesDir(currentDir: File,
                          sourceDir: File): File {
        return currentDir
            .canonicalFile
            .resolveParent(sourceDir)!!
    }

    fun listShapefileFiles(shapefilesDir: File): List<File> {
        return shapefilesDir
            .walk()
            .filter { it.extension == SHAPEFILE_EXT }
            .toList()
    }

    fun loadShapefile(shapefileFile: File): Shapefile {
        val dataStore = FileDataStoreFinder.getDataStore(shapefileFile)
        return Shapefile(shapefileFile, dataStore as ShapefileDataStore)
    }
}

data class Shapefile(val file: File,
                     val dataStore: ShapefileDataStore)

//-------------------------------------------------------------------------------------------------
// PRIVATE FUNCTIONS.
//-------------------------------------------------------------------------------------------------

private fun File.resolveParent(file: File): File? {
    return parentDirectories()
        .map { it.resolve(file) }
        .find { it.isDirectory }
}

private fun File.parentDirectories() =
    generateSequence(absoluteFile) { it.parentFile }
