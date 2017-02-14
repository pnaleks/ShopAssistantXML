package ru.pnapp.sa_xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
@NamespaceList({@Namespace(reference = "http://www.pnapp.ru/ShopAssistant", prefix = "sa_xml")})
@Root(name = "catalog")
public class SaXML {

    @Root
    public static class Icon {
        @Attribute
        long rowId;

        @Attribute(required = false)
        URL url;

        @Attribute(required = false)
        String driveId;
    }

    @Root
    public static class Swap {
        @Attribute
        Date datetime;

        @Attribute
        float quantity;

        @Attribute(required = false)
        Float price;

        @Attribute(required = false)
        Long rowId;

        @Attribute(required = false)
        Boolean delete;
    }

    @Root
    public static class Item {
        @Element(required = false)
        Icon icon;

        @Element(required = false)
        String note;

        @ElementList(entry = "swap", inline = true, required = false)
        List<Swap> swapsList;

        @Attribute(required = false)
        Long rowId;

        @Attribute(required = false)
        String name;

        @Attribute(required = false)
        Boolean delete;
    }

    @Root
    public static class Group {
        @Element(required = false)
        Icon icon;

        @Element(required = false)
        String note;

        @ElementList(entry = "group", inline = true, required = false)
        List<Group> groupsList;

        @ElementList(entry = "item", inline = true, required = false)
        List<Item> itemsList;

        @Attribute(required = false)
        long rowId;

        @Attribute(required = false)
        String name;

        @Attribute(required = false)
        Boolean delete;
    }

    @Element(required = false)
    String agreement;

    @ElementMap(entry = "pref", key = "name", attribute = true, inline = true, required = false)
    Map<String, String> prefs;

    @ElementList(entry = "group", inline = true, required = false)
    List<Group> groups;

    @ElementList(entry = "item", inline = true, required = false)
    ArrayList<Item> items;

    @Attribute
    String title;

    @Attribute(required = false)
    String version;

    @Attribute(required = false)
    Boolean ignoreRowId;

    @Attribute(required = false)
    Boolean compareNames;

    @Override
    public String toString() {
        return "SaXML{" +
                "version='" + version + '\'' +
                ", title=" + title +
                '}';
    }

    public static SaXML get(InputStream inputStream) throws Exception {
        Serializer serializer = new Persister();
        return serializer.read(SaXML.class, inputStream);
    }

    public static void put(SaXML xml, OutputStream outputStream) throws Exception {
        new Persister().write(xml, outputStream);
    }
}
