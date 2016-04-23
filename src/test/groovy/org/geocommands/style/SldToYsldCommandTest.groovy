package org.geocommands.style

import org.geocommands.BaseTest
import org.geocommands.style.SldToYsldCommand.SldToYsldOptions
import org.junit.Test

/**
 * The SldToYsldCommand Unit Test
 * @author Jared Erickson
 */
class SldToYsldCommandTest extends BaseTest {

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

    private String sld = """<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
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
        Reader reader = new StringReader(sld)
        Writer writer = new StringWriter()
        SldToYsldCommand cmd = new SldToYsldCommand()
        SldToYsldOptions options = new SldToYsldOptions()
        cmd.execute(options, reader, writer)
        assertStringsEqual(ysld, writer.toString(), true, true)
    }

    @Test
    void executeWithFiles() {
        Reader reader = new StringReader("")
        Writer writer = new StringWriter()
        File inFile = createTemporaryFile("points", "sld")
        inFile.write(sld)
        File outFile = File.createTempFile("points", "ysld")
        SldToYsldCommand cmd = new SldToYsldCommand()
        SldToYsldOptions options = new SldToYsldOptions(
                input: inFile.absolutePath,
                output: outFile.absolutePath
        )
        cmd.execute(options, reader, writer)
        assertStringsEqual(ysld, outFile.text, true, true)
    }

    @Test
    void runWithStrings() {
        String result = runApp([
                "style sld2ysld"
        ], sld)
        assertStringsEqual(ysld, result.trim(), true, true)
    }

    @Test
    void runWithFiles() {
        File inFile = createTemporaryFile("points", "sld")
        inFile.write(sld)
        File outFile = File.createTempFile("points", "ysld")
        runApp([
                "style sld2ysld",
                "-i", inFile.absolutePath,
                "-o", outFile.absolutePath
        ], "")
        assertStringsEqual(ysld, outFile.text, true, true)
    }
}
