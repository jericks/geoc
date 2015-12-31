package org.geocommands.vector

import org.geocommands.BaseTest
import org.geocommands.vector.DefaultStyleCommand.DefaultStyleOptions
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * The DefaultStyleCommand Unit Test
 * @author Jared Erickson
 */
class DefaultStyleCommandTest extends BaseTest {

    private final String sld = """<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:UserLayer>
    <sld:LayerFeatureConstraints>
      <sld:FeatureTypeConstraint/>
    </sld:LayerFeatureConstraints>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:WellKnownName>circle</sld:WellKnownName>
                <sld:Fill>
                  <sld:CssParameter name="fill">#f5deb3</sld:CssParameter>
                </sld:Fill>
              </sld:Mark>
              <sld:Size>6</sld:Size>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:UserLayer>
</sld:StyledLayerDescriptor>"""

    @Test
    void execute() {
        DefaultStyleCommand cmd = new DefaultStyleCommand()
        File file = getResource("points.properties")
        DefaultStyleOptions options = new DefaultStyleOptions(
                inputWorkspace: file.absolutePath,
                color: "wheat"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, new StringReader(""), writer)
        String actual = writer.toString()
        String expected = sld
        assertStringsEqual(expected, actual, true, true)
    }


    @Test
    void executeWithCsv() {
        DefaultStyleCommand cmd = new DefaultStyleCommand()
        DefaultStyleOptions options = new DefaultStyleOptions(
                color: "wheat"
        )
        StringWriter writer = new StringWriter()
        cmd.execute(options, readCsv("points.csv"), writer)
        String actual = writer.toString()
        String expected = sld
        assertStringsEqual(expected, actual, true, true)
    }

    @Test
    void runAsCommandLine() {
        File file = getResource("points.properties")
        String output = runApp([
                "vector defaultstyle",
                "-i", file.absolutePath,
                "-c", "wheat"
        ], "")
        String actual = output
        String expected = sld + System.getProperty("line.separator")
        assertStringsEqual(expected, actual, true, true)

        output = runApp([
                "vector defaultstyle",
                "-c", "wheat"
        ], readCsv("points.csv").text)
        actual = output
        expected = sld + System.getProperty("line.separator")
        assertStringsEqual(expected, actual, true, true)
    }
}
