package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.ContrastEnhancementStyleCommand.ContrastEnhancementStyleOptions
import org.junit.Test

/**
 * The ContrastEnhancementStyleCommand Unit Test
 */
class ContrastEnhancementStyleCommandTest extends BaseTest {

    @Test
    void execute() {
        ContrastEnhancementStyleCommand command = new ContrastEnhancementStyleCommand()
        ContrastEnhancementStyleOptions options = new ContrastEnhancementStyleOptions(
                method: "histogram",
                gammaValue: 0.78
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
      <sld:Title/>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:RasterSymbolizer>
            <sld:Geometry>
              <ogc:Literal>grid</ogc:Literal>
            </sld:Geometry>
            <sld:ContrastEnhancement>
              <sld:Histogram/>
              <sld:GammaValue>0.78</sld:GammaValue>
            </sld:ContrastEnhancement>
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
                "raster style contrast enhancement",
                "-m", "normalize",
                "-g", "0.25"
        ], "").trim()
        String expected = """<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:UserLayer>
    <sld:LayerFeatureConstraints>
      <sld:FeatureTypeConstraint/>
    </sld:LayerFeatureConstraints>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:Title/>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:RasterSymbolizer>
            <sld:Geometry>
              <ogc:Literal>grid</ogc:Literal>
            </sld:Geometry>
            <sld:ContrastEnhancement>
              <sld:Normalize/>
              <sld:GammaValue>0.25</sld:GammaValue>
            </sld:ContrastEnhancement>
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
