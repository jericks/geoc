package org.geocommands

import geoscript.geom.Bounds
import geoscript.geom.Point
import geoscript.layer.Layer
import geoscript.layer.Renderable
import geoscript.layer.io.CsvReader
import geoscript.proj.Projection
import geoscript.render.Map
import geoscript.style.io.SLDReader
import geoscript.workspace.GeoPackage
import geoscript.workspace.Workspace
import org.apache.commons.text.similarity.LevenshteinDistance
import org.junit.jupiter.api.io.TempDir
import static org.junit.jupiter.api.Assertions.*

/**
 * A base class for Unit Tests.
 * @author Jared Erickson
 */
class BaseTest {

    @TempDir
    File folder

    public static String NEW_LINE = System.getProperty("line.separator")

    void assertStringsEqual(String expected, String actual) {
        assertStringsEqual(expected, actual, false)
    }

    void assertStringsEqual(String expected, String actual, boolean trim, boolean stripXmlNamespaces = false) {
        StringReader expectedReader = new StringReader(expected)
        StringReader actualReader = new StringReader(actual)
        List<String> expectedLines = expectedReader.readLines()
        List<String> actualLines = actualReader.readLines()
        assertEquals(expectedLines.size(), actualLines.size())
        expectedLines.eachWithIndex { String exp, int i ->
            String act = actualLines[i]
            if (trim) {
                exp = exp.trim()
                act = act.trim()
            }
            if (stripXmlNamespaces) {
                exp = stripXmlNS(exp)
                act = stripXmlNS(act)
            }
            assertEquals(exp, act)
        }
    }

    void assertStringsAreSimilar(String expected, String actual, int maxDifference, boolean trim, boolean stripXmlNamespaces = false) {
        StringReader expectedReader = new StringReader(expected)
        StringReader actualReader = new StringReader(actual)
        List<String> expectedLines = expectedReader.readLines()
        List<String> actualLines = actualReader.readLines()
        assertEquals(expectedLines.size(), actualLines.size())
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance()
        expectedLines.eachWithIndex { String exp, int i ->
            String act = actualLines[i]
            if (trim) {
                exp = exp.trim()
                act = act.trim()
            }
            if (stripXmlNamespaces) {
                exp = stripXmlNS(exp)
                act = stripXmlNS(act)
            }
            int distance = levenshteinDistance.apply(exp, act)
            assertTrue(distance <= maxDifference, "The difference between ${exp} and ${act} is ${distance}")
        }
    }

    void assertPointsAreEqual(Point expected, Point actual, double delta) {
        assertEquals(expected.x, actual.x, delta)
        assertEquals(expected.y, actual.y, delta)
    }

    String stripXmlNS(String str) {
        str.replaceAll("xmlns.*?(\"|\').*?(\"|\')", "")
    }

    File getResource(String resource) {
        new File(getClass().getClassLoader().getResource(resource).toURI())
    }

    File getCopiedResource(String resource) {
        File file = getResource(resource)
        List names = splitFileName(file.name)
        File tempFile = File.createTempFile(names[0], ".${names[1]}")
        tempFile.write(file.text)
        tempFile
    }

    private List splitFileName(String name) {
        int i = name.lastIndexOf(".")
        [
                name.substring(0, i),
                name.substring(i + 1)
        ]
    }

    File createDir(String name) {
        File file = new File(folder, name)
        file.mkdir()
        file
    }

    File createTemporaryShapefile(String name) {
        File dir = createDir(name)
        new File(dir, "${name}.shp")
    }

    File createTemporaryFile(String name, String ext) {
        File dir = createDir(name)
        new File(dir, "${name}.${ext}")
    }

    StringReader readCsv(String resource) {
        new StringReader(getResource(resource).text)
    }

    StringReader getStringReader(String resource) {
        new StringReader(getResource(resource).text)
    }

    Layer getLayerFromCsv(String csv) {
        new CsvReader().read(csv)
    }

    String runApp(List args, String input = null) {
        // Save normal System.in and System.out
        InputStream sysin = System.in
        OutputStream sysout = System.out
        OutputStream syserr = System.err
        // Replace System.in with input text
        System.in = new ByteArrayInputStream(input.getBytes("UTF-8"))
        // Replace System.out with OutputStream we can capture
        OutputStream out = new ByteArrayOutputStream()
        System.out = new PrintStream(out)
        // Replace System.err with OutputStream we can capture
        OutputStream err = new ByteArrayOutputStream()
        System.err = new PrintStream(err)
        // Run the app with the List of arguments
        App.main(args as String[])
        // Replace normal System.in and System.out
        System.in = sysin
        System.out = sysout
        System.err = syserr
        // Return the captured output
        out.toString() ?: err.toString()
    }

    void drawOnBasemap(java.util.Map options = [:], String name, List<Renderable> layers) {
        Workspace workspace = new GeoPackage('src/test/resources/data.gpkg')
        Layer countries = workspace.get("countries")
        countries.style = new SLDReader().read(new File('src/test/resources/countries.sld'))
        Layer ocean = workspace.get("ocean")
        ocean.style = new SLDReader().read(new File('src/test/resources/ocean.sld'))
        Projection latLonProjection = new Projection("EPSG:4326")
        Projection mercatorProjection = new Projection("EPSG:3857")
        String projection = options.get("proj", "EPSG:4326")
        int width = 500
        int height = 300
        Bounds bounds = new Bounds(-180,-90,180,90, latLonProjection)
        if (projection.equalsIgnoreCase("EPSG:3857")) {
            width = 400
            height = 400
            bounds = new Bounds(-179.99, -85.0511, 179.99, 85.0511, latLonProjection).reproject(mercatorProjection)
        }
        Map map = new Map(
            width: width,
            height: height,
            layers: [ocean, countries],
            proj: new Projection(projection),
            bounds: bounds
        )
        map.setAdvancedProjectionHandling(true)
        map.setContinuousMapWrapping(false)
        if (options.bounds) {
            map.bounds = options.bounds
        }
        layers.each { Renderable layer ->
            map.addLayer(layer)
        }
        File file = new File("src/main/docs/images/${name}.png")
        if(!file.parentFile.exists()) {
            file.parentFile.mkdir()
        }
        map.render(file)
    }

    protected void draw(String name, List<Renderable> layers) {
        Map map = new Map(
                width: 500,
                height: 300,
                layers: layers
        )
        map.setAdvancedProjectionHandling(false)
        map.setContinuousMapWrapping(false)
        File file = new File("src/main/docs/images/${name}.png")
        if(!file.parentFile.exists()) {
            file.parentFile.mkdir()
        }
        map.render(file)
    }

    protected void draw(String name, List<Renderable> layers, Bounds bounds) {
        Map map = new Map(
                width: 500,
                height: 300,
                layers: layers,
                bounds: bounds
        )
        map.setAdvancedProjectionHandling(false)
        map.setContinuousMapWrapping(false)
        File file = new File("src/main/docs/images/${name}.png")
        if(!file.parentFile.exists()) {
            file.parentFile.mkdir()
        }
        map.render(file)
    }

}
