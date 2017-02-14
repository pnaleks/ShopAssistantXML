package ru.pnapp.sa_xml;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(5, 2 + 2);
    }

    @Test
    public void test1() throws Exception {
        String xml =
                "<?xml version='1.0' encoding='UTF-8'?>"
                        + "<catalog title='Test Database' version='1.0' ignoreRowId='False' compareNames='True' >"
                        + "</catalog>";

        ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());

        SaXML sa = SaXML.get(is);
        assertNotNull(sa);
        assertEquals("Test Database", sa.title);
        assertEquals("1.0", sa.version);
        assertFalse(sa.ignoreRowId);
        assertTrue(sa.compareNames);
    }
}