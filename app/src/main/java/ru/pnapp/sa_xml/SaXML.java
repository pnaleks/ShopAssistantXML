package ru.pnapp.sa_xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess", "unused"})
@NamespaceList({@Namespace(reference = "http://www.pnapp.ru/ShopAssistant", prefix = "sa_xml")})
@Root(strict = false, name = "catalog")
public class SaXML {

    public static class Icon {
        @Attribute(required = true)
        long rowId;

        @Attribute(required = false)
        String uri;

        @Attribute(required = false)
        String driveId;
    }

    public static class Swap {
        @Attribute(required = true)
        long rowId;

        @Attribute(required = true)
        float quantity;

        @Attribute(required = false)
        float price;

        @Attribute(required = true)
        String dateTime;

        @Attribute(required = false, empty = "false")
        boolean delete;
    }

    public static class Item {
        @Element(required = false)
        Icon icon;

        @Element(required = false)
        String note;

        @ElementList(name = "swap", required = false)
        ArrayList<Swap> swapsList;

        @Attribute(required = false)
        long rowId;

        @Attribute(required = false)
        String name;

        @Attribute(required = false, empty = "false")
        boolean delete;
    }

    public static class Group {
        @Element(required = false)
        Icon icon;

        @Element(required = false)
        String note;

        @ElementList(name = "group", required = false)
        ArrayList<Group> groupsList;

        @ElementList(name = "item", required = false)
        ArrayList<Item> itemsList;
    }

    public static class Preference {
        @Element(required = true)
        String value;

        @Attribute(required = true)
        String name;
    }

    @Element(required = false)
    String agreement;

    @ElementList(name = "pref", required = false)
    ArrayList<Preference> prefList;

    @Element(required = false)
    ArrayList swaps;

    @Attribute(required = true)
    String title;

    @Attribute(required = true)
    String version;

    @Attribute(required = false, empty = "false")
    Boolean ignoreRowId;

    @Attribute(required = false, empty = "true")
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
}
