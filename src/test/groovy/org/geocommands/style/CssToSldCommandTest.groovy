package org.geocommands.style

import org.geocommands.BaseTest
import org.geocommands.style.CssToSldCommand.CssToSldOptions
import org.junit.Test

/**
 * The CssToSldCommand Unit Test
 * @author Jared Erickson
 */
class CssToSldCommandTest extends BaseTest {

    private String css = """* {
   mark: symbol(circle);
   mark-size: 6px;
 }

 :mark {
   fill: red;
 }"""

    private String sld = """<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:UserLayer>
    <sld:LayerFeatureConstraints>
      <sld:FeatureTypeConstraint/>
    </sld:LayerFeatureConstraints>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Rule>
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:WellKnownName>circle</sld:WellKnownName>
                <sld:Fill>
                  <sld:CssParameter name="fill">#ff0000</sld:CssParameter>
                </sld:Fill>
              </sld:Mark>
              <sld:Size>6</sld:Size>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>
        <sld:VendorOption name="ruleEvaluation">first</sld:VendorOption>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:UserLayer>
</sld:StyledLayerDescriptor>

"""

    @Test
    void executeWithStrings() {
        Reader reader = new StringReader(css)
        Writer writer = new StringWriter()
        CssToSldCommand cmd = new CssToSldCommand()
        CssToSldOptions options = new CssToSldOptions()
        cmd.execute(options, reader, writer)
        assertStringsEqual(sld, writer.toString(), true, true)
    }

    @Test
    void executeWithFiles() {
        Reader reader = new StringReader("")
        Writer writer = new StringWriter()
        File inFile = createTemporaryFile("points", "css")
        inFile.write(css)
        File outFile = File.createTempFile("points", "sld")
        CssToSldCommand cmd = new CssToSldCommand()
        CssToSldOptions options = new CssToSldOptions(
                input: inFile.absolutePath,
                output: outFile.absolutePath
        )
        cmd.execute(options, reader, writer)
        assertStringsEqual(sld, outFile.text, true, true)
    }

    @Test
    void runWithStrings() {
        String sld = runApp([
                "style css2sld"
        ], css)
        assertStringsEqual(sld, sld, true, true)
    }

    @Test
    void runWithFiles() {
        File inFile = createTemporaryFile("points", "css")
        inFile.write(css)
        File outFile = File.createTempFile("points", "sld")
        runApp([
                "style css2sld",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ], "")
        assertStringsEqual(sld, outFile.text, true, true)
    }
}
