package hastebrot.cartocanvas

import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.View
import tornadofx.Workspace
import tornadofx.stackpane

//-------------------------------------------------------------------------------------------------
// MAIN FUNCTION.
//-------------------------------------------------------------------------------------------------

fun main(args: Array<String>) {
    Application.launch(WorkspaceApp::class.java)
}

class WorkspaceApp : App(Workspace::class) {

    //---------------------------------------------------------------------------------------------
    // PRIVATE PROPERTIES.
    //---------------------------------------------------------------------------------------------

    private val sceneDimension = Dimension2D(800.0, 600.0)

    //---------------------------------------------------------------------------------------------
    // FUNCTIONS.
    //---------------------------------------------------------------------------------------------

    override fun createPrimaryScene(view: UIComponent): Scene {
        return Scene(view.root, sceneDimension.width, sceneDimension.height).apply {
            Platform.runLater {
                registerEscapeKeyHandler(this)
//                window.centerOnScreen()
            }
        }
    }

    override fun onBeforeShow(view: UIComponent) {
        workspace.dock<WorkspaceView>()
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE FUNCTIONS.
    //---------------------------------------------------------------------------------------------

    private fun registerEscapeKeyHandler(scene: Scene) {
//        scene.addEventHandler(KeyEvent.KEY_RELEASED) {
        scene.setOnKeyReleased {
            when {
                it.code == KeyCode.ESCAPE -> scene.window.hide()
            }
        }
    }
}

class WorkspaceView : View("Workspace") {
    override val root = stackpane {}
}

