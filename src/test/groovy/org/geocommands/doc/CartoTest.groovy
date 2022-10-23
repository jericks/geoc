package org.geocommands.doc

import org.junit.jupiter.api.Test

class CartoTest extends DocTest {

    @Test
    void simpleJson() {
        String command = "geoc carto map -t json -c src/test/resources/carto/simple.json -o target/carto_simple_json.png"
        String result = runApp(command, "")
        writeTextFile("geoc_carto_map_simple_json_command", command)
        writeTextFile("geoc_carto_map_simple_json_command_output", result)
        copyFile(new File("target/carto_simple_json.png"), new File("src/main/docs/images/geoc_carto_map_simple_json_command.png"))
        copyFile(new File("src/test/resources/carto/simple.json"), new File("src/main/docs/output/geoc_carto_map_simple_json_command.json"))
    }

    @Test
    void simpleXml() {
        String command = "geoc carto map -t xml -c src/test/resources/carto/simple.xml -o target/carto_simple_xml.png"
        String result = runApp(command, "")
        writeTextFile("geoc_carto_map_simple_xml_command", command)
        writeTextFile("geoc_carto_map_simple_xml_command_output", result)
        copyFile(new File("target/carto_simple_xml.png"), new File("src/main/docs/images/geoc_carto_map_simple_xml_command.png"))
        copyFile(new File("src/test/resources/carto/simple.xml"), new File("src/main/docs/output/geoc_carto_map_simple_xml_command.xml"))
    }

    @Test
    void mapJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 380,
  "height": 280,
  "type": "map",
  "name": "mainMap",
  "imageType": "png",
  "backgroundColor": "white",
  "fixAspectRatio": true,
  "proj": "EPSG:4326",
  "bounds": {
    "minX": -180,
    "minY": -90,
    "maxX": 180,
    "maxY": 90
  },
  "layers": [
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "ocean", "style": "src/test/resources/ocean.sld"},
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "countries", "style": "src/test/resources/countries.sld"}
  ]
}"""
        createCartoFragment("json", "map", fragment, 400, 300)
    }

    @Test
    void mapXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>380</width>
    <height>280</height>
    <type>map</type>
    <name>mainMap</name>
    <imageType>png</imageType>
    <backgroundColor>white</backgroundColor>
    <fixAspectRatio>true</fixAspectRatio>
    <proj>EPSG:4326</proj>
    <bounds>
        <minX>-180</minX>
        <minY>-90</minY>
        <maxX>180</maxX>
        <maxY>90</maxY>
        <proj>EPSG:4326</proj>
    </bounds>
    <layers>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>ocean</layername>
            <style>src/test/resources/ocean.sld</style>
        </layer>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>countries</layername>
            <style>src/test/resources/countries.sld</style>
        </layer>
    </layers>
</item>"""
        createCartoFragment("xml", "map", fragment, 400, 300)
    }

    @Test
    void overviewMapJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 580,
  "height": 240,
  "type": "map",
  "name": "mainMap",
  "fixAspectRatio": false,
  "bounds": {
    "minX": -108.917446,
    "minY": 43.519820,
    "maxX": -89.229946,
    "maxY": 50.137433
  },
  "layers": [
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "ocean", "style": "src/test/resources/ocean.sld"},
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "countries", "style": "src/test/resources/countries.sld"},
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "states", "style": "src/test/resources/states.sld"}
  ]
},
{
  "x": 10,
  "y": 260,
  "width": 580,
  "height": 240,
  "type": "overViewMap",
  "zoomIntoBounds": false,
  "scaleFactor": 2.0,
  "linkedMap": "mainMap", 
  "layers": [
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "ocean", "style": "src/test/resources/ocean.sld"},
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "countries", "style": "src/test/resources/countries.sld"}
  ]
}
"""
        createCartoFragment("json", "overViewMap", fragment, 600, 510)
    }

    @Test
    void overViewMapXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>580</width>
    <height>240</height>
    <type>map</type>
    <name>mainMap</name>
    <imageType>png</imageType>
    <backgroundColor>white</backgroundColor>
    <fixAspectRatio>true</fixAspectRatio>
    <proj>EPSG:4326</proj>
    <bounds>
        <minX>-108.917446</minX>
        <minY>43.519820</minY>
        <maxX>-89.229946</maxX>
        <maxY>50.137433</maxY>
        <proj>EPSG:4326</proj>
    </bounds>
    <layers>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>ocean</layername>
            <style>src/test/resources/ocean.sld</style>
        </layer>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>countries</layername>
            <style>src/test/resources/countries.sld</style>
        </layer>
         <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>states</layername>
            <style>src/test/resources/states.sld</style>
        </layer>
    </layers>
</item>
<item>
    <x>10</x>
    <y>260</y>
    <width>580</width>
    <height>240</height>
    <type>overviewMap</type>
    <zoomIntoBounds>false</zoomIntoBounds>
    <scaleFactor>2.0</scaleFactor>
    <linkedMap>mainMap</linkedMap>
    <layers>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>ocean</layername>
            <style>src/test/resources/ocean.sld</style>
        </layer>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>countries</layername>
            <style>src/test/resources/countries.sld</style>
        </layer>
    </layers>
</item>"""
        createCartoFragment("xml", "overviewMap", fragment, 600, 510)
    }

    @Test
    void scaleBarJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 380,
  "height": 280,
  "type": "map",
  "name": "mainMap",
  "layers": [
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "ocean", "style": "src/test/resources/ocean.sld"},
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "countries", "style": "src/test/resources/countries.sld"}
  ]
}, {
  "x": 10,
  "y": 250,
  "width": 380,
  "height": 40,
  "type": "scalebar",
  "map": "mainMap",
  "strokeColor": "black",
  "strokeWidth": 1,
  "border": 5,
  "units": "METRIC",
  "fillColor": "white",
  "font": {
    "name": "Arial",
    "style": "plain",
    "size": 14
  }
}"""
        createCartoFragment("json", "scalebar", fragment, 400, 300)
    }

    @Test
    void scaleBarXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>580</width>
    <height>240</height>
    <type>map</type>
    <name>mainMap</name>
    <layers>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>ocean</layername>
            <style>src/test/resources/ocean.sld</style>
        </layer>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>countries</layername>
            <style>src/test/resources/countries.sld</style>
        </layer>
         <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>states</layername>
            <style>src/test/resources/states.sld</style>
        </layer>
    </layers>
</item>
<item>
    <x>10</x>
    <y>250</y>
    <width>380</width>
    <height>40</height>
    <type>scalebar</type>
    <map>mainMap</map>
    <strokeColor>black</strokeColor>
    <strokeWidth>1</strokeWidth>
    <border>5</border>
    <units>US</units>
    <fillColor>white</fillColor>
    <font>
        <name>Arial</name>
        <style>plain</style>
        <size>14</size>
    </font>
</item>
"""
        createCartoFragment("xml", "scalebar", fragment, 400, 300)
    }

    @Test
    void scaleTextJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 380,
  "height": 280,
  "type": "map",
  "name": "mainMap",
  "layers": [
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "ocean", "style": "src/test/resources/ocean.sld"},
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "countries", "style": "src/test/resources/countries.sld"}
  ]
}, {
  "x": 10,
  "y": 250,
  "width": 380,
  "height": 40,
  "type": "scaletext",
  "map": "mainMap",
  "format": "#",
  "prefixText": "Scale: ",
  "horizontalAlign": "center",
  "verticalAlign": "middle",
  "color": "black",
  "font": {
    "name": "Arial",
    "style": "plain",
    "size": 14
  }
}"""
        createCartoFragment("json", "scaletext", fragment, 400, 300)
    }

    @Test
    void scaleTextXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>580</width>
    <height>240</height>
    <type>map</type>
    <name>mainMap</name>
    <layers>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>ocean</layername>
            <style>src/test/resources/ocean.sld</style>
        </layer>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>countries</layername>
            <style>src/test/resources/countries.sld</style>
        </layer>
         <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>states</layername>
            <style>src/test/resources/states.sld</style>
        </layer>
    </layers>
</item>
<item>
    <x>10</x>
    <y>250</y>
    <width>380</width>
    <height>40</height>
    <type>scaletext</type>
    <map>mainMap</map>
    <format>#</format>
    <prefixText>Scale :</prefixText>
    <horizontalAlign>center</horizontalAlign>
    <verticalAlign>middle</verticalAlign>
    <color>black</color>
    <font>
        <name>Arial</name>
        <style>plain</style>
        <size>14</size>
    </font>
</item>
"""
        createCartoFragment("xml", "scaletext", fragment, 400, 300)
    }

    @Test
    void legendJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 380,
  "height": 190,
  "type": "map",
  "name": "mainMap",
  "layers": [
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "ocean", "style": "src/test/resources/ocean.sld"},
    {"layertype": "layer", "file": "src/test/resources/data.gpkg", "layername": "countries", "style": "src/test/resources/countries.sld"}
  ]
}, {
  "x": 10,
  "y": 210,
  "width": 380,
  "height": 70,
  "type": "legend",
  "map": "mainMap",
  "backgroundColor": "white",
  "title": "Legend",
  "titleFont":{
    "name": "Arial",
    "style": "bold",
    "size": 18
  },
  "titleColor": "black",
  "textColor": "black",
  "textFont": {
    "name": "Arial",
    "style": "plain",
    "size": 12
  },
  "numberFormat": "#.##",
  "legendEntryWidth": "50",
  "legendEntryHeight": "30",
  "gapBetweenEntries": "10"
}"""
        createCartoFragment("json", "legend", fragment, 400, 300)
    }

    @Test
    void legendXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>380</width>
    <height>190</height>
    <type>map</type>
    <name>mainMap</name>
    <layers>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>ocean</layername>
            <style>src/test/resources/ocean.sld</style>
        </layer>
        <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>countries</layername>
            <style>src/test/resources/countries.sld</style>
        </layer>
         <layer>
            <layertype>layer</layertype>
            <file>src/test/resources/data.gpkg</file>
            <layername>states</layername>
            <style>src/test/resources/states.sld</style>
        </layer>
    </layers>
</item>
<item>
    <x>10</x>
    <y>210</y>
    <width>380</width>
    <height>70</height>
    <type>legend</type>
    <map>mainMap</map>
    <backgroundColor>white</backgroundColor>
    <title>Legend</title>
    <titleFont>
        <name>Arial</name>
        <style>bold</style>
        <size>14</size>
    </titleFont>
    <titleColor>black</titleColor>
    <textColor>black</textColor>
    <textFont>
        <name>Arial</name>
        <style>plain</style>
        <size>12</size>
    </textFont>
    <numberFormat>#.##</numberFormat>
    <legendEntryWidth>50</legendEntryWidth>
    <legendEntryHeight>30</legendEntryHeight>
    <gapBetweenEntries>10</gapBetweenEntries>
</item>
"""
        createCartoFragment("xml", "legend", fragment, 400, 300)
    }

    @Test
    void rectangleJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 30,
  "height": 30,
  "type": "rectangle",
  "fillColor": "white",
  "strokeColor": "black"
}"""
        createCartoFragment("json", "rectangle", fragment, 50, 50)
    }

    @Test
    void rectangleXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>30</width>
    <height>30</height>
    <type>rectangle</type>
    <fillColor>white</fillColor>
    <strokeColor>black</strokeColor>
</item>
"""
        createCartoFragment("xml", "rectangle", fragment, 50, 50)
    }

    @Test
    void lineJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 180,
  "height": 0,
  "type": "line",
  "strokeColor": "black",
  "strokeWidth": 2
}"""
        createCartoFragment("json", "line", fragment, 200, 50)
    }

    @Test
    void lineXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>180</width>
    <height>0</height>
    <type>line</type>
    <strokeColor>black</strokeColor>
    <strokeWidth>2</strokeWidth>
</item>
"""
        createCartoFragment("xml", "line", fragment, 200, 50)
    }

    @Test
    void imageJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 512,
  "height": 431,
  "type": "image",
  "path": "src/main/docs/static/images/geoc.png"
}"""
        createCartoFragment("json", "image", fragment, 532, 451)
    }

    @Test
    void imageXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>512</width>
    <height>431</height>
    <type>image</type>
    <path>src/main/docs/static/images/geoc.png</path>
</item>
"""
        createCartoFragment("xml", "image", fragment, 532, 451)
    }

    @Test
    void textJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 140,
  "height": 30,
  "type": "text",
  "text": "Map Text",
  "horizontalAlign": "center",
  "verticalAlign": "middle",
  "color": "black",
  "font": {
    "name": "Arial",
    "style": "plain",
    "size": 14
  }
}"""
        createCartoFragment("json", "text", fragment, 150, 50)
    }

    @Test
    void textXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>140</width>
    <height>30</height>
    <type>text</type>
    <text>Map Text</text>
    <horizontalAlign>center</horizontalAlign>
    <verticalAlign>middle</verticalAlign>
    <color>black</color>
    <font>
        <name>Arial</name>
        <style>plain</style>
        <size>14</size>
    </font>
</item>"""
        createCartoFragment("xml", "text", fragment, 150, 50)
    }

    @Test
    void paragraphJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 240,
  "height": 140,
  "type": "paragraph",
  "text": "The Carto package contains classes for creating cartographic documents. All items are added to the document with x and y coordinates whose origin is the upper left and width and a height.",
  "color": "black",
  "font": {
    "name": "Arial",
    "style": "plain",
    "size": 14
  }
}"""
        createCartoFragment("json", "paragraph", fragment, 250, 150)
    }

    @Test
    void paragraphXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>240</width>
    <height>140</height>
    <type>paragraph</type>
    <text>The Carto package contains classes for creating cartographic documents. All items are added to the document with x and y coordinates whose origin is the upper left and width and a height.t</text>
    <color>black</color>
    <font>
        <name>Arial</name>
        <style>plain</style>
        <size>14</size>
    </font>
</item>"""
        createCartoFragment("xml", "paragraph", fragment, 250, 150)
    }

    @Test
    void northArrowJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 130,
  "height": 130,
  "type": "northarrow",
  "style": "North",
  "fillColor1": "black",
  "fillColor2": "white",
  "strokeColor1": "black",
  "strokeColor2": "black",
  "strokeWidth": 1,
  "drawText": true,
  "textColor": "black",
  "font": {
    "name": "Arial",
    "style": "plain",
    "size": 24
  }
}"""
        createCartoFragment("json", "northarrow", fragment, 150, 150)
    }

    @Test
    void northArrowXML() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>130</width>
    <height>130</height>
    <type>northarrow</type>
    <style>NorthEastSouthWest</style>
    <fillColor1>black</fillColor1>
    <fillColor2>white</fillColor2>
    <strokeColor1>black</strokeColor1>
    <strokeColor2>black</strokeColor2>
    <strokeWidth>1</strokeWidth>
    <drawText>true</drawText>
    <textColor>black</textColor>
    <font>
        <name>Arial</name>
        <style>plain</style>
        <size>24</size>
    </font>
</item>"""
        createCartoFragment("xml", "northarrow", fragment, 150, 150)
    }

    @Test
    void dateTextJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 140,
  "height": 30,
  "type": "datetext",
  "date": "1/22/2022",
  "format": "MM/dd/yyyy",
  "horizontalAlign": "center",
  "verticalAlign": "middle",
  "color": "black",
  "font": {
    "name": "Arial",
    "style": "plain",
    "size": 14
  }
}"""
        createCartoFragment("json", "datetext", fragment, 150, 50)
    }

    @Test
    void dateTextXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>140</width>
    <height>30</height>
    <type>dateText</type>
    <date>1/22/2022</date>
    <format>MM/dd/yyyy</format>
    <horizontalAlign>center</horizontalAlign>
    <verticalAlign>middle</verticalAlign>
    <color>black</color>
    <font>
        <name>Arial</name>
        <style>plain</style>
        <size>14</size>
    </font>
</item>"""
        createCartoFragment("xml", "datetext", fragment, 150, 50)
    }

    @Test
    void gridJson() {
        String fragment = """{
  "x": 0,
  "y": 0,
  "width": 100,
  "height": 100,
  "type": "grid",
  "size": 10,
  "strokeColor": "black",
  "strokeWidth": 0.5
}"""
        createCartoFragment("json", "grid", fragment, 100, 100)
    }

    @Test
    void gridXml() {
        String fragment = """
<item>
    <x>0</x>
    <y>0</y>
    <width>100</width>
    <height>100</height>
    <type>grid</type>
    <size>10</size>
    <strokeColor>black</strokeColor>
    <strokeWidth>0.5</strokeWidth>
</item>"""
        createCartoFragment("xml", "grid", fragment, 100, 100)
    }

    @Test
    void tableJson() {
        String fragment = """{
  "x": 10,
  "y": 10,
  "width": 280,
  "height": 80,
  "type": "table",
  "columns": ["ID", "Name", "Abbreviation"],
  "rows": [
    {"ID": 1, "Name": "Washington", "Abbreviation": "WA"},
    {"ID": 2, "Name": "Oregon", "Abbreviation": "OR"},
    {"ID": 3, "Name": "California", "Abbreviation": "CA"}
  ]
}"""
        createCartoFragment("json", "table", fragment, 300, 100)
    }

    @Test
    void tableXml() {
        String fragment = """<item>
    <x>10</x>
    <y>10</y>
    <width>280</width>
    <height>80</height>
    <type>table</type>
    <columns>
        <column>ID</column>
        <column>Name</column>
        <column>Abbreviation</column>
    </columns>
    <rows>
        <row>
            <ID>1</ID>
            <Name>Washington</Name>
            <Abbreviation>WA</Abbreviation>
        </row>
        <row>
            <ID>2</ID>
            <Name>Oregon</Name>
            <Abbreviation>OR</Abbreviation>
        </row>
        <row>
            <ID>3</ID>
            <Name>California</Name>
            <Abbreviation>CA</Abbreviation>
        </row>
    </rows>
</item>
"""
        createCartoFragment("xml", "table", fragment, 300, 100)
    }

    void createCartoFragment(String type, String name, String fragment, int width, int height) {
        String document = type.equalsIgnoreCase("json") ? createJsonDocument(fragment, width, height) : createXmlDocument(fragment, width, height)
        println document
        File file = new File("target/carto_map_${name}_${type}.${type}")
        file.text = document
        String command = "geoc carto map -t ${type} -c ${file.absolutePath} -o target/carto_map_${name}_${type}.png"
        String result = runApp(command, "")
        println result
        writeTextFile("carto_map_${name}_${type}", fragment)
        copyFile(new File("target/carto_map_${name}_${type}.png"), new File("src/main/docs/images/carto_map_${name}_${type}.png"))
    }

    private String createJsonDocument(String fragment, int width, int height) {
        """
        {
          "type": "png",
          "width": ${width},
          "height": ${height},
          "items": [
             ${fragment}  
          ]
        }
        """.stripMargin().trim()
    }

    private String createXmlDocument(String fragment, int width, int height) {
        """
        <carto>
            <type>png</type>
            <width>${width}</width>
            <height>${height}</height>
            <items>
                ${fragment}
            </items>
        </carto>  
        """.stripMargin().trim()
    }

}
