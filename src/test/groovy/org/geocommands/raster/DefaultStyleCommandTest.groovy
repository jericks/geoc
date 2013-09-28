package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.DefaultStyleCommand.DefaultStyleOptions
import org.junit.Test

/**
 * The DefaultStyleCommand Unit Test
 */
class DefaultStyleCommandTest extends BaseTest {

    @Test
    void execute() {
        DefaultStyleCommand command = new DefaultStyleCommand()
        DefaultStyleOptions options = new DefaultStyleOptions(
                opacity: 0.85
        )
        StringWriter writer = new StringWriter()
        command.execute(options, new StringReader(""), writer)
        String expected = """<?xml version="1.0" encoding="UTF-8"?>
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
          <sld:RasterSymbolizer>
            <sld:Geometry>
              <ogc:Literal>grid</ogc:Literal>
            </sld:Geometry>
            <sld:Opacity>0.85</sld:Opacity>
          </sld:RasterSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:UserLayer>
</sld:StyledLayerDescriptor>
"""
        String actual = writer.toString().trim()
        assertStringsEqual(expected, actual)
    }

    @Test
    void runAsCommandLine() {
        String actual = runApp([
                "raster style default",
                "-o","0.85"
        ], "").trim()
        String expected = """<?xml version="1.0" encoding="UTF-8"?>
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
          <sld:RasterSymbolizer>
            <sld:Geometry>
              <ogc:Literal>grid</ogc:Literal>
            </sld:Geometry>
            <sld:Opacity>0.85</sld:Opacity>
          </sld:RasterSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:UserLayer>
</sld:StyledLayerDescriptor>
"""
        assertStringsEqual(expected, actual)
    }

}
