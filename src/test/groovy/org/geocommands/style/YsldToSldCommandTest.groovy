package org.geocommands.style

import org.geocommands.BaseTest
import org.geocommands.style.YsldToSldCommand.YsldToSldOptions
import org.junit.Test

/**
 * The CssToSldCommand Unit Test
 * @author Jared Erickson
 */
class YsldToSldCommandTest extends BaseTest {

    private String ysld = """name: Default Styler
feature-styles:
- name: name
  rules:
  - scale: [min, max]
    symbolizers:
    - point:
        size: 6
        symbols:
        - mark:
            shape: circle
            fill-color: '#FF0000'
  x-ruleEvaluation: first
"""

    private String sld = """<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
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
                  <sld:CssParameter name="fill">#FF0000</sld:CssParameter>
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
        Reader reader = new StringReader(ysld)
        Writer writer = new StringWriter()
        YsldToSldCommand cmd = new YsldToSldCommand()
        YsldToSldOptions options = new YsldToSldOptions()
        cmd.execute(options, reader, writer)
        assertStringsEqual(sld, writer.toString().trim(), true, true)
    }

    @Test
    void executeWithFiles() {
        Reader reader = new StringReader("")
        Writer writer = new StringWriter()
        File inFile = createTemporaryFile("points", "ysld")
        inFile.write(ysld)
        File outFile = File.createTempFile("points", "sld")
        YsldToSldCommand cmd = new YsldToSldCommand()
        YsldToSldOptions options = new YsldToSldOptions(
                input: inFile.absolutePath,
                output: outFile.absolutePath
        )
        cmd.execute(options, reader, writer)
        assertStringsEqual(sld, outFile.text.trim(), true, true)
    }

    @Test
    void runWithStrings() {
        String result = runApp([
                "style ysld2sld"
        ], ysld)
        assertStringsEqual(sld, result.trim(), true, true)
    }

    @Test
    void runWithFiles() {
        File inFile = createTemporaryFile("points", "ysld")
        inFile.write(ysld)
        File outFile = File.createTempFile("points", "sld")
        runApp([
                "style ysld2sld",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ], "")
        assertStringsEqual(sld, outFile.text.trim(), true, true)
    }
}
