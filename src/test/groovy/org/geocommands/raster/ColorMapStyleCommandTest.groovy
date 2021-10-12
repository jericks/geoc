package org.geocommands.raster

import org.geocommands.BaseTest
import org.junit.jupiter.api.Test

/**
 * The ColorMapStyleCommand Unit Test
 */
class ColorMapStyleCommandTest extends BaseTest {

    @Test
    void execute() {
        ColorMapStyleCommand command = new ColorMapStyleCommand()
        ColorMapStyleCommand.ColorMapStyleOptions options = new ColorMapStyleCommand.ColorMapStyleOptions(
                values: ["10=red", "50=blue", "100=wheat", "250=white"],
                type: "ramp",
                extended: false
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
        String expected = """<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:UserLayer>
    <sld:LayerFeatureConstraints>
      <sld:FeatureTypeConstraint/>
    </sld:LayerFeatureConstraints>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:RasterSymbolizer>
            <sld:Geometry>
              <ogc:Literal>grid</ogc:Literal>
            </sld:Geometry>
            <sld:ColorMap>
              <sld:ColorMapEntry color="#ff0000" opacity="1.0" quantity="10"/>
              <sld:ColorMapEntry color="#0000ff" opacity="1.0" quantity="50"/>
              <sld:ColorMapEntry color="#f5deb3" opacity="1.0" quantity="100"/>
              <sld:ColorMapEntry color="#ffffff" opacity="1.0" quantity="250"/>
            </sld:ColorMap>
            <sld:ContrastEnhancement/>
          </sld:RasterSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:UserLayer>
</sld:StyledLayerDescriptor>
"""
        String actual = writer.toString().trim()
        assertStringsEqual(expected, actual, true, true)
    }

    @Test
    void runAsCommandLine() {
        String actual = runApp([
                "raster style colormap",
                "-v", "10=red",
                "-v", "50=blue",
                "-v", "100=wheat",
                "-v", "250=white",
                "-t", "ramp",
                "-e",
        ], "").trim()
        String expected = """<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:UserLayer>
    <sld:LayerFeatureConstraints>
      <sld:FeatureTypeConstraint/>
    </sld:LayerFeatureConstraints>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:RasterSymbolizer>
            <sld:Geometry>
              <ogc:Literal>grid</ogc:Literal>
            </sld:Geometry>
            <sld:ColorMap extended="true">
              <sld:ColorMapEntry color="#ff0000" opacity="1.0" quantity="10"/>
              <sld:ColorMapEntry color="#0000ff" opacity="1.0" quantity="50"/>
              <sld:ColorMapEntry color="#f5deb3" opacity="1.0" quantity="100"/>
              <sld:ColorMapEntry color="#ffffff" opacity="1.0" quantity="250"/>
            </sld:ColorMap>
            <sld:ContrastEnhancement/>
          </sld:RasterSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:UserLayer>
</sld:StyledLayerDescriptor>
"""
        assertStringsEqual(expected, actual, true, true)
    }

}
