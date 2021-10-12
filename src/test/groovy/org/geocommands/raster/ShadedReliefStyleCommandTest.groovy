package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.ShadedReliefStyleCommand.ShadedReliefStyleOptions
import org.junit.jupiter.api.Test

/**
 * The ShadedReliefStyleCommand Unit Test
 * @author Jared Erickson
 */
class ShadedReliefStyleCommandTest extends BaseTest {

    @Test
    void execute() {
        ShadedReliefStyleCommand command = new ShadedReliefStyleCommand()
        ShadedReliefStyleOptions options = new ShadedReliefStyleOptions(
                brightnessOnly: true,
                reliefFactor: "65",
                opacity: 0.85
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
            <sld:Opacity>0.85</sld:Opacity>
            <sld:ContrastEnhancement/>
            <sld:ShadedRelief>
              <sld:BrightnessOnly>true</sld:BrightnessOnly>
              <sld:ReliefFactor>65</sld:ReliefFactor>
            </sld:ShadedRelief>
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
                "raster style shadedrelief",
                "-r", "65",
                "-b",
                "-o", "0.85"
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
            <sld:Opacity>0.85</sld:Opacity>
            <sld:ContrastEnhancement/>
            <sld:ShadedRelief>
              <sld:BrightnessOnly>true</sld:BrightnessOnly>
              <sld:ReliefFactor>65</sld:ReliefFactor>
            </sld:ShadedRelief>
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
