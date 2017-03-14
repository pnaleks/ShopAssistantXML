package ru.pnapp.sa_xml;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void test_v3() throws Exception {
        SaXML sa = new SaXML();
        sa.title = "Test v3 XML";
        sa.version = "3";

        SaXML.Item item;
        sa.items = new ArrayList<>();

        item = new SaXML.Item();
        item.rowId = 1;
        item.name = "Item 1 (five)";
        item.quantity = 5F;
        sa.items.add(item);

        item = new SaXML.Item();
        item.rowId = 2;
        item.name = "Item 2 (null)";
        item.quantity = null;
        sa.items.add(item);

        SaXML.SwapEx swapEx;
        sa.swaps = new ArrayList<>();

        swapEx = new SaXML.SwapEx();
        swapEx.datetime = new Date();
        swapEx.itemId = 2;
        swapEx.rowId = 1;
        swapEx.quantity = 5;
        swapEx.price = 1200F;
        sa.swaps.add(swapEx);

        swapEx = new SaXML.SwapEx();
        swapEx.datetime = new Date();
        swapEx.itemId = 1;
        swapEx.rowId = 2;
        swapEx.quantity = 4;
        swapEx.price = 1800F;
        sa.swaps.add(swapEx);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SaXML.put(sa, bos);
        System.out.println(bos);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        SaXML sb = SaXML.get(bis);

        assertTrue(compare(sa, sb));
    }

    @Test
    public void test_rootIcons() throws Exception {
        SaXML sa = new SaXML();
        sa.title = "Test icons at the root of the catalog";
        sa.version = "3";

        sa.icons = new ArrayList<>();

        SaXML.Icon icon = new SaXML.Icon();
        icon.rowId = 1;
        icon.url = new URL("http://some.image.host/some_image.img");
        icon.driveId = "driveId:sdsafdoijewqoidj==";

        sa.icons.add(icon);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SaXML.put(sa, bos);
        System.out.println(bos);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        SaXML sb = SaXML.get(bis);

        assertNotNull(sb.icons);
        assertEquals(sa.icons.size(), sb.icons.size());
        assertEquals(sa.icons.get(0).rowId, sb.icons.get(0).rowId);
        assertEquals(sa.icons.get(0).url, sb.icons.get(0).url);
        assertEquals(sa.icons.get(0).driveId, sb.icons.get(0).driveId);
    }

    private boolean compare(SaXML a, SaXML b) {
        if (a == null) return b == null;

        return b != null && a.title.equals(b.title) &&
                (a.version == null ? b.version == null : a.version.equals(b.version)) &&
                (a.ignoreRowId == null ? b.ignoreRowId == null : a.ignoreRowId.equals(b.ignoreRowId)) &&
                (a.compareNames == null ? b.compareNames == null : a.compareNames.equals(b.compareNames)) &&
                (a.agreement == null ? b.agreement == null : a.agreement.equals(b.agreement)) &&
                compareGroups(a.groups, b.groups) &&
                compareItems(a.items, b.items) &&
                compareSwaps(a.swaps, b.swaps);
    }

    private boolean compareGroups(List<SaXML.Group> a, List<SaXML.Group> b) {
        if (a == null) return b == null;
        if (b == null) return false;
        if (a.size() != b.size()) return false;
        boolean result = false;
        for (SaXML.Group ag : a) {
            for (SaXML.Group bg : b) {
                result = (ag.rowId == bg.rowId) &&
                        (ag.name == null ? bg.name == null : ag.name.equals(bg.name)) &&
                        compareGroups(ag.groupsList, bg.groupsList) &&
                        compareItems(ag.itemsList, bg.itemsList);
                if (result) break;
            }
            if (!result) return false;
        }
        return true;
    }

    private boolean compareItems(List<SaXML.Item> a, List<SaXML.Item> b) {
        if (a == null) return b == null;
        if (b == null) return false;
        if (a.size() != b.size()) return false;
        boolean result = false;
        for (SaXML.Item ai : a) {
            for (SaXML.Item bi : b) {
                result = (ai.rowId == bi.rowId) &&
                        (ai.name == null ? bi.name == null : ai.name.equals(bi.name)) &&
                        (ai.quantity == null ? bi.quantity == null : ai.quantity.equals(bi.quantity)) &&
                        compareSwaps(ai.swapsList, bi.swapsList);
                if (result) break;
            }
            if (!result) return false;
        }
        return true;
    }

    private boolean compareSwaps(List<? extends SaXML.Swap> a, List<? extends SaXML.Swap> b) {
        if (a == null) return b == null;
        if (b == null) return false;
        if (a.size() != b.size()) return false;
        boolean result = false;
        for (SaXML.Swap as : a) {
            for (SaXML.Swap bs : b) {
                result = false;
                if (as instanceof SaXML.SwapEx) {
                    if (bs instanceof SaXML.SwapEx) {
                        result = ((SaXML.SwapEx) as).itemId == ((SaXML.SwapEx) bs).itemId;
                    }
                    if (!result) continue;
                }
                result = (as.rowId == bs.rowId) &&
                        (as.price == null ? bs.price == null : as.price.equals(bs.price)) &&
                        (as.quantity == bs.quantity);
                if (result) break;
            }
            if (!result) return false;
        }
        return true;
    }
}