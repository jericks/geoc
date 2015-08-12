package org.geocommands.raster

import org.geocommands.BaseTest
import org.geocommands.raster.ChannelSelectionStyleCommand.ChannelSelectionStyleOptions
import org.junit.Test

/**
 * The ChannelSelectionStyleCommand Unit Test
 * @author Jared Erickson
 */
class ChannelSelectionStyleCommandTest extends BaseTest {

    @Test
    void executeRGB() {
        ChannelSelectionStyleCommand command = new ChannelSelectionStyleCommand()
        ChannelSelectionStyleOptions options = new ChannelSelectionStyleOptions(
                red: "red",
                green: "green",
                blue: "blue"
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
            <sld:ChannelSelection>
              <sld:RedChannel>
                <sld:SourceChannelName>red</sld:SourceChannelName>
                <sld:ContrastEnhancement/>
              </sld:RedChannel>
              <sld:GreenChannel>
                <sld:SourceChannelName>green</sld:SourceChannelName>
                <sld:ContrastEnhancement/>
              </sld:GreenChannel>
              <sld:BlueChannel>
                <sld:SourceChannelName>blue</sld:SourceChannelName>
                <sld:ContrastEnhancement/>
              </sld:BlueChannel>
            </sld:ChannelSelection>
            <sld:ContrastEnhancement/>
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
    void executeGray() {
        ChannelSelectionStyleCommand command = new ChannelSelectionStyleCommand()
        ChannelSelectionStyleOptions options = new ChannelSelectionStyleOptions(
                gray: "gray"
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
            <sld:ChannelSelection>
              <sld:GrayChannel>
                <sld:SourceChannelName>gray</sld:SourceChannelName>
                <sld:ContrastEnhancement/>
              </sld:GrayChannel>
            </sld:ChannelSelection>
            <sld:ContrastEnhancement/>
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
                "raster style channel selection",
                "-r", "red,histogram,0.5",
                "-g", "green,normalize,0.25",
                "-b", "green,histogram,0.33",
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
            <sld:ChannelSelection>
              <sld:RedChannel>
                <sld:SourceChannelName>red</sld:SourceChannelName>
                <sld:ContrastEnhancement>
                  <sld:Histogram/>
                  <sld:GammaValue>0.5</sld:GammaValue>
                </sld:ContrastEnhancement>
              </sld:RedChannel>
              <sld:GreenChannel>
                <sld:SourceChannelName>green</sld:SourceChannelName>
                <sld:ContrastEnhancement>
                  <sld:Normalize/>
                  <sld:GammaValue>0.25</sld:GammaValue>
                </sld:ContrastEnhancement>
              </sld:GreenChannel>
              <sld:BlueChannel>
                <sld:SourceChannelName>green</sld:SourceChannelName>
                <sld:ContrastEnhancement>
                  <sld:Histogram/>
                  <sld:GammaValue>0.33</sld:GammaValue>
                </sld:ContrastEnhancement>
              </sld:BlueChannel>
            </sld:ChannelSelection>
            <sld:ContrastEnhancement/>
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
