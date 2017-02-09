package hastebrot.cartocanvas

import com.vividsolutions.jts.geom.Geometry
import org.geotools.data.simple.SimpleFeatureCollection
import org.geotools.data.simple.SimpleFeatureSource
import org.geotools.feature.FeatureIterator
import org.geotools.geometry.jts.ReferencedEnvelope
import org.geotools.referencing.CRS
import org.opengis.feature.Feature
import org.opengis.feature.simple.SimpleFeature
import org.opengis.feature.simple.SimpleFeatureType
import org.opengis.referencing.crs.CoordinateReferenceSystem
import java.math.RoundingMode
import java.math.RoundingMode.CEILING
import java.math.RoundingMode.FLOOR
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

//-------------------------------------------------------------------------------------------------
// PROPERTIES.
//-------------------------------------------------------------------------------------------------

val ReferencedEnvelope.crs: CoordinateReferenceSystem?
    get() = coordinateReferenceSystem

//-------------------------------------------------------------------------------------------------
// FUNCTIONS.
//-------------------------------------------------------------------------------------------------

//DataUtilities.fileToURL()

fun printFeatureSourceInfo(featureSource: SimpleFeatureSource) {
    println(featureSource.format())
    println(featureSource.schema.format())
    println(featureSource.features.format())
    println(featureSource.features.bounds.format())
    println(featureSource.features.bounds.crs?.format())
    println("-")
}

fun printShapefileInfo(shapefile: Shapefile) {
    println("file name: " + shapefile.file.name)
    println("file path: " + shapefile.file.absolutePath)
    println("file size: " + shapefile.file.length()
        .let { "${it.formatBytes()} ($it bytes)" })
    println("file exists: " + shapefile.file.exists())
    println("file readable: " + shapefile.file.canRead())
    println("datastore entries: " + shapefile.dataStore.names)
    println("datastore charset: " + shapefile.dataStore.charset)
    println("-")
}

fun SimpleFeatureSource.format(): String {
    val name = name.localPart
    val numOfFeatures = features.size()
    val numOfAttributes = schema.attributeCount
    return "dataset($name, features = $numOfFeatures, attrs = $numOfAttributes)"
}

fun SimpleFeatureType.format(): String {
    val attributes = attributeDescriptors
        .map { it.name.localPart to it.type.binding.simpleName }
    return "attributes(${attributes.joinToString(", ")})"
}

fun SimpleFeatureCollection.format(): String {
    val geometryPoints = useFeatures { features ->
        features.map { it.defaultGeometry as Geometry }
            .sumBy { it.numPoints }
    }
    val geometryTypes = useFeatures { features ->
        features.map { it.defaultGeometry as Geometry }
            .groupBy { it.geometryType }
            .map { it.key to it.value.size }
    }
    return "points($geometryPoints) types(${geometryTypes.joinToString(", ")})"
}

fun CoordinateReferenceSystem.format(): String {
    val name = name
    val axis = CRS.getAxisOrder(this)
    return "crs($name, $axis)"
}

fun ReferencedEnvelope.format(maximumDigits: Int = 4): String {
    val minX = minX.format(maximumDigits, FLOOR)
    val maxX = maxX.format(maximumDigits, CEILING)
    val minY = minY.format(maximumDigits, FLOOR)
    val maxY = maxY.format(maximumDigits, CEILING)
    return "extent($minX, $maxX, $minY, $maxY)"
}

fun Double.format(maximumDigits: Int = Int.MAX_VALUE,
                  roundingMode: RoundingMode = RoundingMode.HALF_EVEN): String {
    return DecimalFormat("0").let {
        it.decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.ENGLISH)
        it.maximumFractionDigits = maximumDigits
        it.roundingMode = roundingMode
        it.format(this)
    }
}

inline fun <R> SimpleFeatureCollection.useFeatures(block: (Sequence<SimpleFeature>) -> R) =
    features().use { block(it.featureSequence()) }

fun <F : Feature> FeatureIterator<F>.featureSequence() = object : Iterator<F> {
    override fun next() = this@featureSequence.next()
    override fun hasNext() = this@featureSequence.hasNext()
}.asSequence()

//fun <F : Feature> FeatureIterator<F>.featureSequence() = FeatureSequence(this)
//    .constrainOnce()
//
//private class FeatureSequence<out F : Feature>(
//    private val iterator: FeatureIterator<F>
//) : Sequence<F> {
//    override fun iterator(): Iterator<F> {
//        return object : Iterator<F> {
//            override fun next() = iterator.next()
//            override fun hasNext() = iterator.hasNext()
//        }
//    }
//}

private fun Long.formatBytes(byteUnit: ByteUnit = ByteUnit.DECIMAL): String {
    fun logdiv(num: Double, denom: Double) = Math.log(num) / Math.log(denom)
    fun powdiv(num: Double, base: Double, exp: Double) = num / Math.pow(base, exp)

    val index = logdiv(this.toDouble(), byteUnit.size.toDouble()).toInt()
    val value = powdiv(this.toDouble(), byteUnit.size.toDouble(), index.toDouble())
    return "${DecimalFormat("#,##0.#").format(value)} ${byteUnit.names[index]}"
}

private enum class ByteUnit(val size: Int,
                            val names: Array<String>) {
    DECIMAL(1000, arrayOf("B", "kB", "MB", "GB", "TB", "PB", "EB")),
    BINARY(1024, arrayOf("B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"))
}
