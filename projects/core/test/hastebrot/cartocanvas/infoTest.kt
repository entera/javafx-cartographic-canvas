package hastebrot.cartocanvas

import com.vividsolutions.jts.geom.Geometry
import com.winterbe.expekt.expect
import org.geotools.data.collection.CollectionFeatureSource
import org.geotools.feature.DefaultFeatureCollection
import org.geotools.feature.simple.SimpleFeatureBuilder
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import org.geotools.geometry.jts.ReferencedEnvelope
import org.geotools.geometry.jts.WKTReader2
import org.geotools.referencing.crs.DefaultEngineeringCRS
import org.geotools.referencing.crs.DefaultGeographicCRS
import org.junit.jupiter.api.Test
import java.math.RoundingMode.CEILING
import java.math.RoundingMode.FLOOR

class InfoTest {
    @Test
    fun should_format_double() {
        expect(0.12.format(3, FLOOR)).to.equal("0.12")
        expect(0.123456.format(3, FLOOR)).to.equal("0.123")
        expect(0.123456.format(3, CEILING)).to.equal("0.124")
        expect((123.456789).format(3, FLOOR)).to.equal("123.456")
        expect((-123.456789).format(3, FLOOR)).to.equal("-123.457")
        expect((-123.456789).format()).to.equal("-123.456789")
    }

    @Test
    fun should_format_envelope() {
        val envelope = ReferencedEnvelope(-1.222222, 10.222222, 100.222222, 1000.222222,
            DefaultGeographicCRS.WGS84)
        expect(envelope.format()).to.equal("extent(-1.2223, 10.2223, 100.2222, 1000.2223)")
        expect(envelope.crs?.format()).to.equal("crs(WGS84(DD), EAST_NORTH)")
    }

    @Test
    fun should_format_feature_source() {
        val source = CollectionFeatureSource(
            DefaultFeatureCollection(
                null,
                SimpleFeatureTypeBuilder().apply {
                    name = "schema"
                    namespaceURI = null
                    crs = DefaultEngineeringCRS.GENERIC_2D
                    defaultGeometry = "geom"
                    add("geom", Geometry::class.java)
                    add("string", String::class.java)
                    add("integer", Int::class.java)
                }.buildFeatureType()
            ).apply {
                add(SimpleFeatureBuilder(schema).apply {
                    set("geom", WKTReader2().read("POINT(1 2)"))
                }.buildFeature(null))
                add(SimpleFeatureBuilder(schema).apply {
                    set("geom", WKTReader2().read("LINESTRING(1 2, 3 4)"))
                }.buildFeature(null))
            }
        )

        expect(source.format()).to.equal("dataset(schema, features = 2, attrs = 3)")
        expect(source.schema.format()).to.equal(
            "attributes((geom, Geometry), (string, String), (integer, int))")
        expect(source.features.format()).to.equal(
            "points(3) types((LineString, 1), (Point, 1))")
    }

}
