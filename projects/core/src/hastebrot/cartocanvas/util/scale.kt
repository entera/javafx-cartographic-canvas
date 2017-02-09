package hastebrot.cartocanvas.util

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Envelope
import javafx.geometry.BoundingBox
import javafx.geometry.Bounds
import javafx.geometry.Dimension2D
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import org.geotools.geometry.jts.ReferencedEnvelope
import org.geotools.referencing.crs.DefaultGeographicCRS

fun main(args: Array<String>) {
    val envelope = ReferencedEnvelope(-180.0, 180.0, -90.0, 90.0, DefaultGeographicCRS.WGS84)
    val rectangle = rectOf(0.0, 0.0, 800.0, 500.0)
    val coordinate = Coordinate(0.0, 0.0)

    val scaleX = scale().model(envelope.minY, envelope.maxY).view(0.0, rectangle.width)
    val scaleY = scale().model(envelope.minX, envelope.maxX).view(rectangle.height, 0.0)
    println(Point2D(scaleX(coordinate.y), scaleY(coordinate.x)))

    val scaleX_ = scale().model(Axis.Y, envelope.toRect()).view(Axis.X, rectangle)
    val scaleY_ = scale().model(Axis.X, envelope.toRect()).view(Axis.Y_INVERSE, rectangle)
    println(Point2D(scaleX_(Axis.Y, coordinate.toPoint()), scaleY_(Axis.X, coordinate.toPoint())))
}

fun scale() = Scale()

data class Scale(val model: ClosedRange<Double> = 0.0..1.0,
                 val view: ClosedRange<Double> = 0.0..1.0) {
    operator fun invoke(value: Double) = value
        .let { model.reverseInterpolate(it) }
        .let { view.interpolate(it) }
    operator fun invoke(axis: Axis, point: Point2D) = invoke(axisValue(axis, point))

    fun model(range: ClosedRange<Double>) = copy(model = range)
    fun model(start: Double, end: Double) = model(start..end)
    fun model(axis: Axis, rect: Rectangle2D) = model(axisRange(axis, rect))

    fun view(range: ClosedRange<Double>) = copy(view = range)
    fun view(start: Double, end: Double) = view(start..end)
    fun view(axis: Axis, rect: Rectangle2D) = view(axisRange(axis, rect))
}

enum class Axis {
    X,
    Y,
    X_INVERSE,
    Y_INVERSE
}

fun axisValue(axis: Axis, point: Point2D): Double = when (axis) {
    Axis.X -> point.x
    Axis.Y -> point.y
    Axis.X_INVERSE -> -point.x
    Axis.Y_INVERSE -> -point.y
}

fun axisRange(axis: Axis, bounds: Rectangle2D): ClosedRange<Double> = when (axis) {
    Axis.X -> bounds.minX..bounds.maxX
    Axis.Y -> bounds.minY..bounds.maxY
    Axis.X_INVERSE -> bounds.maxX..bounds.minX
    Axis.Y_INVERSE -> bounds.maxY..bounds.minY
}

fun Coordinate.toPoint() = pointOf(x, y)
fun Dimension2D.toRect() = rectOf(0.0, 0.0, width, height)
fun Envelope.toRect() = rectOf(minX, minY, width, height)
fun ReferencedEnvelope.toRect() = rectOf(minX, minY, width, height)

//fun sharpRound(value: Double) = Math.samples.round(value) - 0.5
//fun sharpRound(value: Double) = value

fun pointOf(x: Double, y: Double) = Point2D(x, y)

fun rectOf(minX: Double, minY: Double,
           width: Double, height: Double) = Rectangle2D(minX, minY, width, height)

fun boundsOf(minX: Double, minY: Double,
             width: Double, height: Double): Bounds =
    BoundingBox(minX, minY, width, height)

fun ClosedRange<Double>.interpolate(factor: Double): Double =
    start + ((endInclusive - start) * factor)

fun ClosedRange<Double>.reverseInterpolate(value: Double): Double =
    (value - start) / (endInclusive - start)

fun Bounds.interpolate(factor: Point2D): Point2D = Point2D(
    (minX..maxX).interpolate(factor.x),
    (minY..maxY).interpolate(factor.y)
)
