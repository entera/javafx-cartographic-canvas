package hastebrot.cartocanvas.util

import com.sun.javafx.application.PlatformImpl
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

fun main(args: Array<String>) {
    val primaryStage = setupStage { stage ->
        stage.title = "hello javafx"
        stage.scene = Scene(StackPane(Label("hello javafx")), 200.0, 200.0)
        stage.show()
    }

    setupStage({ primaryStage }) { stage ->
        stage.scene = Scene(StackPane(Label("hello stage")), 200.0, 100.0)
    }
}

//fun main(args: Array<String>) {
//    val stage = setupStage()
//    platformRun<Unit> { HelloApplication().start(stage) }
//    println("first")
//}
//
//class HelloApplication : Application() {
//    override fun start(stage: Stage) {
//        stage.scene = Scene(StackPane(Label("hello javafx")), 200.0, 200.0)
//        stage.show()
//        println("second")
//    }
//}

fun setupStage(supplyStage: () -> Stage = { Stage() },
               consumeStage: (Stage) -> Unit = {}): Stage {
//    PlatformImpl.setImplicitExit(false)

    return PlatformKt.startup<Stage> { future ->
        val stage = supplyStage()
        consumeStage(stage)
        future.complete(stage)
    }.get()
}

//fun setupStageTwo(supplyStage: () -> Stage = { Stage() },
//                  consumeStage: (Stage) -> Unit = {}): Stage {
////    PlatformImpl.setImplicitExit(false)
//
//    val stage = PlatformKt.startup<Stage> { future ->
//        future.complete(supplyStage())
//    }.get()
//
//    return PlatformKt.execute<Stage> { future ->
//        consumeStage(stage)
//        future.complete(stage)
//    }.get()
//}

private object PlatformKt {
    fun <T> startup(action: (CompletableFuture<T>) -> Unit): Future<T> {
        return withCompletableFuture { future ->
            PlatformImpl.startup {
                action(future)
            }
        }
    }

    fun <T> execute(action: (CompletableFuture<T>) -> Unit): Future<T> {
        return withCompletableFuture { future ->
            PlatformImpl.runLater {
                action(future)
            }
        }
    }
}

private fun <T> withCompletableFuture(action: (CompletableFuture<T>) -> Unit): Future<T> {
    return with(CompletableFuture<T>()) {
        action(this); this
    }
}
