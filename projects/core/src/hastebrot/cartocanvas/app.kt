package hastebrot.cartocanvas

import javafx.application.Application
import javafx.geometry.Orientation
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.scene.input.KeyCode
import tornadofx.App
import tornadofx.Controller
import tornadofx.Stylesheet
import tornadofx.UIComponent
import tornadofx.View
import tornadofx.Workspace
import tornadofx.property
import tornadofx.px
import tornadofx.splitpane
import tornadofx.stackpane
import java.io.File

//-------------------------------------------------------------------------------------------------
// MAIN FUNCTION.
//-------------------------------------------------------------------------------------------------

fun main(args: Array<String>) {
    Application.launch(WorkspaceApp::class.java)
}

//-------------------------------------------------------------------------------------------------
// CLASSES.
//-------------------------------------------------------------------------------------------------

class WorkspaceApp : App(Workspace::class, WorkspaceStyles::class) {

    //---------------------------------------------------------------------------------------------
    // FUNCTIONS.
    //---------------------------------------------------------------------------------------------

    override fun createPrimaryScene(view: UIComponent): Scene {
        return Scene(view.root).apply {
            registerEscapeKeyHandler(this)
        }
    }

    override fun onBeforeShow(view: UIComponent) {
        workspace.dock<WorkspaceView>()
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE FUNCTIONS.
    //---------------------------------------------------------------------------------------------

    private fun registerEscapeKeyHandler(scene: Scene) {
        scene.setOnKeyReleased {
            when (it.code) {
                KeyCode.ESCAPE -> scene.window.hide()
                else -> Unit
            }
        }
    }

    //---------------------------------------------------------------------------------------------
    // CLASSES.
    //---------------------------------------------------------------------------------------------

    class WorkspaceView : View("Workspace") {
        val prefs by inject<WorkspacePrefs>()

        override val root = stackpane {
            splitpane {
                setDividerPositions(0.33)
                orientation = Orientation.HORIZONTAL
                stackpane {
                    SplitPane.setResizableWithParent(this, false)
                    add(ShapefileListerView::class)
                }
                stackpane { }
            }
        }
    }

    class WorkspaceStyles : Stylesheet() {
        init {
            root {
                prefWidth = 800.px
                prefHeight = 600.px
            }
        }
    }

    class WorkspacePrefs : Controller() {
        var currentDir by property(File("."))
        var sourceDir by property(File("geodata-eu-us-world"))
    }

}
