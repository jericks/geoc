package org.geocommands.tile

import org.geocommands.tile.PyramidCommand.PyramidOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * The PyramidCommand Unit Test.
 * @author Jared Erickson
 */
class PyramidCommandTest extends BaseTest {

    @Test void executeToCsv() {
        PyramidCommand cmd = new PyramidCommand()
        PyramidOptions options = new PyramidOptions(
                tileLayer: getResource("earthquakes.mbtiles"),
                tileLayerName: "earthquakes",
                outputType: "csv"
        )
        String result = cmd.execute(options)
        assertEquals """EPSG:3857
-2.0036395147881314E7,-2.0037471205137067E7,2.0036395147881314E7,2.003747120513706E7,EPSG:3857
BOTTOM_LEFT
256,256
0,1,1,156412.0,156412.0
1,2,2,78206.0,78206.0
2,4,4,39103.0,39103.0
3,8,8,19551.5,19551.5
4,16,16,9775.75,9775.75
5,32,32,4887.875,4887.875
6,64,64,2443.9375,2443.9375
7,128,128,1221.96875,1221.96875
8,256,256,610.984375,610.984375
9,512,512,305.4921875,305.4921875
10,1024,1024,152.74609375,152.74609375
11,2048,2048,76.373046875,76.373046875
12,4096,4096,38.1865234375,38.1865234375
13,8192,8192,19.09326171875,19.09326171875
14,16384,16384,9.546630859375,9.546630859375
15,32768,32768,4.7733154296875,4.7733154296875
16,65536,65536,2.38665771484375,2.38665771484375
17,131072,131072,1.193328857421875,1.193328857421875
18,262144,262144,0.5966644287109375,0.5966644287109375
19,524288,524288,0.29833221435546875,0.29833221435546875
""", result
    }

    @Test void executeToXml() {
        PyramidCommand cmd = new PyramidCommand()
        PyramidOptions options = new PyramidOptions(
                tileLayer: getResource("earthquakes.mbtiles"),
                tileLayerName: "earthquakes",
                outputType: "xml"
        )
        String result = cmd.execute(options)
        assertEquals """<pyramid>
  <proj>EPSG:3857</proj>
  <bounds>
    <minX>-2.0036395147881314E7</minX>
    <minY>-2.0037471205137067E7</minY>
    <maxX>2.0036395147881314E7</maxX>
    <maxY>2.003747120513706E7</maxY>
  </bounds>
  <origin>BOTTOM_LEFT</origin>
  <tileSize>
    <width>256</width>
    <height>256</height>
  </tileSize>
  <grids>
    <grid>
      <z>0</z>
      <width>1</width>
      <height>1</height>
      <xres>156412.0</xres>
      <yres>156412.0</yres>
    </grid>
    <grid>
      <z>1</z>
      <width>2</width>
      <height>2</height>
      <xres>78206.0</xres>
      <yres>78206.0</yres>
    </grid>
    <grid>
      <z>2</z>
      <width>4</width>
      <height>4</height>
      <xres>39103.0</xres>
      <yres>39103.0</yres>
    </grid>
    <grid>
      <z>3</z>
      <width>8</width>
      <height>8</height>
      <xres>19551.5</xres>
      <yres>19551.5</yres>
    </grid>
    <grid>
      <z>4</z>
      <width>16</width>
      <height>16</height>
      <xres>9775.75</xres>
      <yres>9775.75</yres>
    </grid>
    <grid>
      <z>5</z>
      <width>32</width>
      <height>32</height>
      <xres>4887.875</xres>
      <yres>4887.875</yres>
    </grid>
    <grid>
      <z>6</z>
      <width>64</width>
      <height>64</height>
      <xres>2443.9375</xres>
      <yres>2443.9375</yres>
    </grid>
    <grid>
      <z>7</z>
      <width>128</width>
      <height>128</height>
      <xres>1221.96875</xres>
      <yres>1221.96875</yres>
    </grid>
    <grid>
      <z>8</z>
      <width>256</width>
      <height>256</height>
      <xres>610.984375</xres>
      <yres>610.984375</yres>
    </grid>
    <grid>
      <z>9</z>
      <width>512</width>
      <height>512</height>
      <xres>305.4921875</xres>
      <yres>305.4921875</yres>
    </grid>
    <grid>
      <z>10</z>
      <width>1024</width>
      <height>1024</height>
      <xres>152.74609375</xres>
      <yres>152.74609375</yres>
    </grid>
    <grid>
      <z>11</z>
      <width>2048</width>
      <height>2048</height>
      <xres>76.373046875</xres>
      <yres>76.373046875</yres>
    </grid>
    <grid>
      <z>12</z>
      <width>4096</width>
      <height>4096</height>
      <xres>38.1865234375</xres>
      <yres>38.1865234375</yres>
    </grid>
    <grid>
      <z>13</z>
      <width>8192</width>
      <height>8192</height>
      <xres>19.09326171875</xres>
      <yres>19.09326171875</yres>
    </grid>
    <grid>
      <z>14</z>
      <width>16384</width>
      <height>16384</height>
      <xres>9.546630859375</xres>
      <yres>9.546630859375</yres>
    </grid>
    <grid>
      <z>15</z>
      <width>32768</width>
      <height>32768</height>
      <xres>4.7733154296875</xres>
      <yres>4.7733154296875</yres>
    </grid>
    <grid>
      <z>16</z>
      <width>65536</width>
      <height>65536</height>
      <xres>2.38665771484375</xres>
      <yres>2.38665771484375</yres>
    </grid>
    <grid>
      <z>17</z>
      <width>131072</width>
      <height>131072</height>
      <xres>1.193328857421875</xres>
      <yres>1.193328857421875</yres>
    </grid>
    <grid>
      <z>18</z>
      <width>262144</width>
      <height>262144</height>
      <xres>0.5966644287109375</xres>
      <yres>0.5966644287109375</yres>
    </grid>
    <grid>
      <z>19</z>
      <width>524288</width>
      <height>524288</height>
      <xres>0.29833221435546875</xres>
      <yres>0.29833221435546875</yres>
    </grid>
  </grids>
</pyramid>""", result
    }

    @Test void runToJson() {
        String result = runApp([
           "tile pyramid",
                "-l", getResource("earthquakes.mbtiles").absolutePath,
                "-n", "earthquakes",
                "-o", "json"
        ],"")
        assertEquals """{
    "proj": "EPSG:3857",
    "bounds": {
        "minX": -2.0036395147881314E7,
        "minY": -2.0037471205137067E7,
        "maxX": 2.0036395147881314E7,
        "maxY": 2.003747120513706E7
    },
    "origin": "BOTTOM_LEFT",
    "tileSize": {
        "width": 256,
        "height": 256
    },
    "grids": [
        {
            "z": 0,
            "width": 1,
            "height": 1,
            "xres": 156412.0,
            "yres": 156412.0
        },
        {
            "z": 1,
            "width": 2,
            "height": 2,
            "xres": 78206.0,
            "yres": 78206.0
        },
        {
            "z": 2,
            "width": 4,
            "height": 4,
            "xres": 39103.0,
            "yres": 39103.0
        },
        {
            "z": 3,
            "width": 8,
            "height": 8,
            "xres": 19551.5,
            "yres": 19551.5
        },
        {
            "z": 4,
            "width": 16,
            "height": 16,
            "xres": 9775.75,
            "yres": 9775.75
        },
        {
            "z": 5,
            "width": 32,
            "height": 32,
            "xres": 4887.875,
            "yres": 4887.875
        },
        {
            "z": 6,
            "width": 64,
            "height": 64,
            "xres": 2443.9375,
            "yres": 2443.9375
        },
        {
            "z": 7,
            "width": 128,
            "height": 128,
            "xres": 1221.96875,
            "yres": 1221.96875
        },
        {
            "z": 8,
            "width": 256,
            "height": 256,
            "xres": 610.984375,
            "yres": 610.984375
        },
        {
            "z": 9,
            "width": 512,
            "height": 512,
            "xres": 305.4921875,
            "yres": 305.4921875
        },
        {
            "z": 10,
            "width": 1024,
            "height": 1024,
            "xres": 152.74609375,
            "yres": 152.74609375
        },
        {
            "z": 11,
            "width": 2048,
            "height": 2048,
            "xres": 76.373046875,
            "yres": 76.373046875
        },
        {
            "z": 12,
            "width": 4096,
            "height": 4096,
            "xres": 38.1865234375,
            "yres": 38.1865234375
        },
        {
            "z": 13,
            "width": 8192,
            "height": 8192,
            "xres": 19.09326171875,
            "yres": 19.09326171875
        },
        {
            "z": 14,
            "width": 16384,
            "height": 16384,
            "xres": 9.546630859375,
            "yres": 9.546630859375
        },
        {
            "z": 15,
            "width": 32768,
            "height": 32768,
            "xres": 4.7733154296875,
            "yres": 4.7733154296875
        },
        {
            "z": 16,
            "width": 65536,
            "height": 65536,
            "xres": 2.38665771484375,
            "yres": 2.38665771484375
        },
        {
            "z": 17,
            "width": 131072,
            "height": 131072,
            "xres": 1.193328857421875,
            "yres": 1.193328857421875
        },
        {
            "z": 18,
            "width": 262144,
            "height": 262144,
            "xres": 0.5966644287109375,
            "yres": 0.5966644287109375
        },
        {
            "z": 19,
            "width": 524288,
            "height": 524288,
            "xres": 0.29833221435546875,
            "yres": 0.29833221435546875
        }
    ]
}
""", result
    }
}
