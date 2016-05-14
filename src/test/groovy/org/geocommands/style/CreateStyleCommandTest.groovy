package org.geocommands.style

import org.geocommands.style.CreateStyleCommand.CreateStyleOptions
import org.geocommands.BaseTest
import org.junit.Test

/**
 * The CreateSimpleStyleCommand Unit Test
 * @author Jared Erickson
 */
class CreateStyleCommandTest extends BaseTest {

    @Test void execute() {
        CreateStyleCommand cmd = new CreateStyleCommand()
        File file = folder.newFile("outline.sld")
        CreateStyleOptions options = new CreateStyleOptions(
            options: [
                "stroke": "black",
                "stroke-width": 0.5
            ],
            output: file.absolutePath
        )
        Reader reader = new StringReader("")
        Writer writer = new StringWriter()
        cmd.execute(options, reader, writer)
        String result = file.text
        assertStringsEqual("""<?xml version="1.0" encoding="UTF-8"?>
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
          <sld:LineSymbolizer>
            <sld:Stroke>
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
                "style create",
                "-s", "stroke=black",
                "-s", "stroke-width=0.5",
                "-t", "ysld"
        ], "")
        assertStringsEqual("""name: Default Styler
feature-styles:
- name: name
  rules:
  - scale: [min, max]
    symbolizers:
    - line:
        stroke-color: '#000000'
        stroke-width: 0.5""", result.trim(), true, false)
    }

}
