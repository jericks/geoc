package org.geocommands.doc

import geoscript.layer.Layer
import geoscript.layer.Shapefile
import geoscript.style.io.SimpleStyleReader
import org.junit.jupiter.api.Test

class StyleTest extends CartoTest {

    @Test
    void createSld() {
        String command = "geoc style create -s stroke=navy -s stroke-width=0.5 -t sld -o target/boundaries.sld"
        String result = runApp(command, "")
        writeTextFile("geoc_style_create_stroke_sld_command", command)
        copyFile(new File("target/boundaries.sld"), new File("src/main/docs/output/geoc_style_create_stroke_sld_command.sld"))
    }

    @Test
    void createYsld() {
        String command = "geoc style create -s fill=white -s stroke=black -s stroke-width=1.5 -t ysld -o target/boundaries.ysld"
        String result = runApp(command, "")
        writeTextFile("geoc_style_create_stroke_ysld_command", command)
        copyFile(new File("target/boundaries.ysld"), new File("src/main/docs/output/geoc_style_create_stroke_ysld_command.ysld"))
    }

    @Test
    void css2sld() {
        String css = """* {
   mark: symbol(circle);
   mark-size: 6px;
 }

 :mark {
   fill: red;
 }"""
        File cssFile = new File("target/points.css")
        cssFile.text = css
        String command = "geoc style css2sld -i target/points.css -o target/points.sld"
        String result = runApp(command, "")
        writeTextFile("geoc_style_css2sld_command", command)
        copyFile(new File("target/points.css"), new File("src/main/docs/output/geoc_style_css2sld_command.css"))
        copyFile(new File("target/points.sld"), new File("src/main/docs/output/geoc_style_css2sld_command.sld"))
    }

    @Test
    void sld2ysld() {
        String sld = """<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
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
        File sldFile = new File("target/points.sld")
        sldFile.text = sld
        String command = "geoc style sld2ysld -i target/points.sld -o target/points.ysld"
        String result = runApp(command, "")
        writeTextFile("geoc_style_sld2ysld_command", command)
        copyFile(new File("target/points.sld"), new File("src/main/docs/output/geoc_style_sld2ysld_command.sld"))
        copyFile(new File("target/points.ysld"), new File("src/main/docs/output/geoc_style_sld2ysld_command.ysld"))
    }

    @Test
    void ysld2sld() {
        String ysld = """name: Default Styler
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
        File ysldFile = new File("target/points.ysld")
        ysldFile.text = ysld
        String command = "geoc style ysld2sld -i target/points.ysld -o target/points.sld"
        String result = runApp(command, "")
        writeTextFile("geoc_style_ysld2sld_command", command)
        copyFile(new File("target/points.ysld"), new File("src/main/docs/output/geoc_style_ysld2sld_command.ysld"))
        copyFile(new File("target/points.sld"), new File("src/main/docs/output/geoc_style_ysld2sld_command.sld"))
    }

    @Test
    void uniquevaluesfromtext() {
        String text = """AHa=#aa0c74
AHat=#b83b1f
AHcf=#964642
AHh=#78092e
AHpe=#78092e
AHt=#5f025a
AHt3=#e76161
Aa1=#fcedcd
Aa2=#94474b
"""
        File file = new File("target/units.txt")
        file.text = text
        String command = "geoc style uniquevaluesfromtext -i target/units.txt -f UNIT -g polygon -t sld -o target/units.sld"
        String result = runApp(command, "")
        writeTextFile("geoc_style_uniquevaluesfromtext_command", command)
        copyFile(new File("target/units.txt"), new File("src/main/docs/output/geoc_style_uniquevaluesfromtext_command_units.txt"))
        copyFile(new File("target/units.sld"), new File("src/main/docs/output/geoc_style_uniquevaluesfromtext_command.sld"))
    }
}
