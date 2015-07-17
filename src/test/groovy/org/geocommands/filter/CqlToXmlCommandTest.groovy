package org.geocommands.filter

import org.geocommands.filter.CqlToXmlCommand.CqlToXmlOptions
import org.geocommands.BaseTest
import org.junit.Test
import static org.junit.Assert.assertTrue

/**
 * The CqlToXmlCommand Unit Test
 * @author Jared Erickson
 */
class CqlToXmlCommandTest extends BaseTest {

    @Test void execute() {
        CqlToXmlOptions options = new CqlToXmlOptions(cql: "name=wa")
        CqlToXmlCommand cmd = new CqlToXmlCommand()
        String result = cmd.execute(options)
        assertTrue result.startsWith("<ogc:Filter")
        assertTrue result.trim().endsWith("</ogc:Filter>")
    }

    @Test void run() {
        String result = runApp([
           "filter cql2xml"
        ],"name=wa")
        assertTrue result.startsWith("<ogc:Filter")
        assertTrue result.trim().endsWith("</ogc:Filter>")
    }

}
