package hastebrot.cartocanvas

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import tornadofx.App
import tornadofx.Stylesheet
import tornadofx.UIComponent
import tornadofx.View
import tornadofx.Workspace
import tornadofx.px
import tornadofx.stackpane

//-------------------------------------------------------------------------------------------------
// MAIN FUNCTION.
//-------------------------------------------------------------------------------------------------

fun main(args: Array<String>) {
    Application.launch(WorkspaceApp::class.java)
}

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
        override val root = stackpane {}
    }

    class WorkspaceStyles : Stylesheet() {
        init {
            root {
                prefWidth = 800.px
                prefHeight = 600.px
            }
        }
    }
}

