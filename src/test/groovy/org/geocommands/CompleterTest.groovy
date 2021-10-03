package org.geocommands

import org.junit.Test
import static org.junit.Assert.*

class CompleterTest {

    @Test
    void complete() {
        Completer completer = new Completer()
        List<String> values = completer.complete("")
        assertEquals(12, values.size())
        assertTrue(values.contains("list"))
        assertTrue(values.contains("filter"))
        assertTrue(values.contains("tile"))
        assertTrue(values.contains("raster"))

        values = completer.complete("vec")
        assertEquals(12, values.size())
        assertTrue(values.contains("list"))
        assertTrue(values.contains("filter"))
        assertTrue(values.contains("tile"))
        assertTrue(values.contains("raster"))

        values = completer.complete("vector")
        assertEquals(101, values.size())
        assertTrue(values.contains("centroid"))
        assertTrue(values.contains("buffer"))
        assertTrue(values.contains("mincircle"))
        assertTrue(values.contains("count"))

        values = completer.complete("vector buf")
        assertEquals(101, values.size())
        assertTrue(values.contains("centroid"))
        assertTrue(values.contains("buffer"))
        assertTrue(values.contains("mincircle"))
        assertTrue(values.contains("count"))

        values = completer.complete("vector buffer")
        assertEquals(18, values.size())
        assertTrue(values.contains("-d"))
        assertTrue(values.contains("--distance"))
        assertTrue(values.contains("--output-workspace"))
        assertTrue(values.contains("-o"))

        values = completer.complete("vector buffer -d 10")
        assertEquals(18, values.size())
        assertTrue(values.contains("-d"))
        assertTrue(values.contains("--distance"))
        assertTrue(values.contains("--output-workspace"))
        assertTrue(values.contains("-o"))

        values = completer.complete("asdf asfd")
        assertEquals(0, values.size())

        values = completer.complete("vector asfd --distance")
        assertEquals(0, values.size())
    }

}
