package ru.pnapp.sa_xml;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private boolean luck() { return Math.random() > 0.5; }
    private Object headsOrTails(Object head, Object tail) { return luck() ? head : tail; }

    @Test
    public void tagCatalogTest() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("title", (String) headsOrTails("The title", "Another good title"));
        for(int i = 0; i<3; i++) {
            if (luck()) map.put("version", String.format(Locale.US, "v%f", Math.random()));
            if (luck()) map.put("ignoreRowId", (String) headsOrTails("true", "false"));
            if (luck()) map.put("compareNames", (String) headsOrTails("true", "false"));

            String str = "<catalog";
            for (String key : map.keySet()) {
                str += ' ' + key + '=' + '"' + map.get(key) + '"';
            }
            str += "/>";

            System.out.println(str);
            SaXML sa = SaXML.get(new ByteArrayInputStream(str.getBytes()));

            assertNotNull("SaXML.get() returns NULL", sa);

            for (String key : map.keySet()) {
                Field f = SaXML.class.getDeclaredField(key);
                String inp = map.get(key);
                String out = f.get(sa).toString();
                assertEquals(inp, out);
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            SaXML.put(sa, bos);
            System.out.println(bos);

            System.out.println();
        }
    }

    @Test
    public void catalogTest() throws Exception {
        final String TITLE = "Test Database";
        final String VERSION = "1.0b";
        final String IGNORE_ROW_ID = (String) headsOrTails("true","false");
        final String COMPARE_NAMES = (String) headsOrTails("true","false");

        final String XML_STRING = "<?xml version='1.0' encoding='UTF-8'?>\n"
                + String.format("<catalog title='%s' version='%s' ignoreRowId='%s' compareNames='%s'>\n",
                TITLE, VERSION, IGNORE_ROW_ID, COMPARE_NAMES)
                + "    <pref name='some_pref1'>some_value1</pref>\n"
                + "    <pref name='some_pref2'>some_value2</pref>\n"
                + "    <group rowId='1' name='group1'>\n"
                + "        <group rowId='11' name='group1.1'>\n"
                + "            <group rowId='111' name='group1.1.1'>\n"
                + "                <group rowId='1111' name='group1.1.1.1'>\n"
                + "                   <icon rowId='1'\n"
                + "                       driveId='driveId:abracadabra'\n"
                + "                       url='https://sites.google.com/site/pnappandroid/home/shop_assistant.png'/>\n"
                + "                   <note>What the pretty group of items</note>\n"
                + "                </group>\n"
                + "            </group>\n"
                + "            <item rowId='12' name='Group 1.1, Item 1'/>\n"
                + "            <item rowId='13' name='Group 1.1, Item 2'>\n"
                + "                <swap datetime='2017-02-18' quantity='-1'/>\n"
                + "                <swap datetime='2017-02-22 12:40:18 GMT' quantity='-4'/>\n"
                + "            </item>\n"
                + "            <item rowId='14' name='Group 1.1, Item 3'/>\n"
                + "        </group>\n"
                + "        <item rowId='124' name='Group 1, Item 1'/>\n"
                + "        <item rowId='125' name='Group 1, Item 2'/>\n"
                + "        <item rowId='126' name='Group 1, Item 3'/>\n"
                + "    </group>\n"
                + "    <group rowId='2' name='group2'/>\n"
                + "    <item rowId='123' name='1. Root item'/>\n"
                + "    <item rowId='234' name='2. Root item'/>\n"
                + "</catalog>";

        ByteArrayInputStream is = new ByteArrayInputStream(XML_STRING.getBytes());

        SaXML sa = SaXML.get(is);

        assertNotNull(sa);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SaXML.put(sa, bos);
        System.out.println(bos);
    }
}