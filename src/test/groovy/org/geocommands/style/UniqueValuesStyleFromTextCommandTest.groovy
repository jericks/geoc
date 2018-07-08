package org.geocommands.style

import org.geocommands.style.UniqueValuesStyleFromTextCommand.ReadUniqueValuesStyleOptions
import org.geocommands.BaseTest
import org.junit.Test

class UniqueValuesStyleFromTextCommandTest extends BaseTest {

    @Test void execute() {
        UniqueValuesStyleFromTextCommand cmd = new UniqueValuesStyleFromTextCommand()
        File inputFile = folder.newFile("units.txt")
        inputFile.text = """AHa=#aa0c74
AHat=#b83b1f
AHcf=#964642
AHh=#78092e
AHpe=#78092e
AHt=#5f025a
AHt3=#e76161
Aa1=#fcedcd
Aa2=#94474b
"""
        File outputFile = folder.newFile("units.sld")
        ReadUniqueValuesStyleOptions options = new ReadUniqueValuesStyleOptions(
                field: "UnitType",
                geometryType: "Polygon",
                input: inputFile.absolutePath,
                output: outputFile.absolutePath
        )
        Reader reader = new StringReader("")
        Writer writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String result = outputFile.text
        assertStringsEqual("""<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:UserLayer>
    <sld:LayerFeatureConstraints>
      <sld:FeatureTypeConstraint/>
    </sld:LayerFeatureConstraints>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>AHa</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#aa0c74</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#760851</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>AHat</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#b83b1f</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#802915</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>AHcf</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#964642</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#69312e</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>AHh</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#78092e</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#540620</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>AHpe</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#78092e</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#540620</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>AHt</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#5f025a</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#42013e</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>AHt3</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#e76161</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#a14343</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>Aa1</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#fcedcd</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#b0a58f</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>UnitType</ogc:PropertyName>
              <ogc:Literal>Aa2</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#94474b</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#673134</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.5</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:UserLayer>
</sld:StyledLayerDescriptor>""", result.trim(), true, true)
    }

    @Test void run() {
        String result = runApp([
                "style uniquevaluesfromtext",
                "-f", "UnitType",
                "-g", "Polygon",
                "-t", "ysld"
        ], """AHa=#aa0c74
AHat=#b83b1f
AHcf=#964642
AHh=#78092e
AHpe=#78092e
AHt=#5f025a
AHt3=#e76161
Aa1=#fcedcd
Aa2=#94474b
""")
        assertStringsEqual('''name: Default Styler
feature-styles:
- name: name
  rules:
  - filter: ${UnitType = 'AHa'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#AA0C74'
    - line:
        stroke-color: '#760851'
        stroke-width: 0.5
  - filter: ${UnitType = 'AHat'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#B83B1F'
    - line:
        stroke-color: '#802915'
        stroke-width: 0.5
  - filter: ${UnitType = 'AHcf'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#964642'
    - line:
        stroke-color: '#69312E'
        stroke-width: 0.5
  - filter: ${UnitType = 'AHh'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#78092E'
    - line:
        stroke-color: '#540620'
        stroke-width: 0.5
  - filter: ${UnitType = 'AHpe'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#78092E'
    - line:
        stroke-color: '#540620'
        stroke-width: 0.5
  - filter: ${UnitType = 'AHt'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#5F025A'
    - line:
        stroke-color: '#42013E'
        stroke-width: 0.5
  - filter: ${UnitType = 'AHt3'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#E76161'
    - line:
        stroke-color: '#A14343'
        stroke-width: 0.5
  - filter: ${UnitType = 'Aa1'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#FCEDCD'
    - line:
        stroke-color: '#B0A58F'
        stroke-width: 0.5
  - filter: ${UnitType = 'Aa2'}
    scale: [min, max]
    symbolizers:
    - polygon:
        fill-color: '#94474B'
    - line:
        stroke-color: '#673134'
        stroke-width: 0.5''', result.trim(), true, false)
    }

}
