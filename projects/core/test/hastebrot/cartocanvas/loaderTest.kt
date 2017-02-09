package hastebrot.cartocanvas

import hastebrot.cartocanvas.util.setupStage
import javafx.scene.Scene
import tornadofx.Scope
import tornadofx.find

//-------------------------------------------------------------------------------------------------
// MAIN FUNCTION.
//-------------------------------------------------------------------------------------------------

fun main(args: Array<String>) {
    setupStage { stage ->
        val scope = Scope()
        val view = find<ShapefileListerView>(scope)
        val scene = Scene(view.root, 400.0, 400.0)
        stage.scene = scene
        stage.show()
    }
}
