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

/**
 * XML-представление базы данных программы
 * <a href="https://play.google.com/store/apps/details?id=pnapp.productivity.store">Продавец консультант</a>
 * на основе
 * <a href="http://simple.sourceforge.net/">SimpleXML</a>.
 * Используется для импорта/экспорта данных, резервного копирования, синхронизаации с внешним источником данных.
 * <br><br>
 * Корневой элемент XML файла {@code catalog} поддерживает атрибуты {@link #title}, {@link #version}, {@link #ignoreRowId},
 * {@link #compareNames} и может содержать элементы {@link Group}, {@link Item} и {@link SwapEx},
 * а так-же набор параметров {@link #prefs} и элемент {@link #agreement}.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@NamespaceList({@Namespace(reference = "http://www.pnapp.ru/ShopAssistant", prefix = "sa")})
@Root(name = "catalog")
public class SaXML {

    /**
     * Группам {@link Group} и элементам {@link Item} можно сопоставить изображение, которое задается по внешнему {@link URL}
     * и/или идентификатором файла на Google Disk. Последний может быть задан во внешнем источнике данных, либо формируется в
     * результате резервного копирования изображения на Google Disk
     */
    @Root
    public static class Icon {
        /** Уникальный идентификатор группы в базе данных */
        @Attribute
        public long rowId;

        @Attribute(required = false)
        public URL url;

        @Attribute(required = false)
        public String driveId;
    }

    /** Сделки (продажи, поступления товара и т.п.). Значение {@link #quantity} может иметь знак. Элемент используется для
     * представления сделок внутри элемента {@link Item}. Сделки могут содержаться в корневом элементе, для этого используется
     * класс {@link SwapEx} */
    @Root
    public static class Swap {
        @Attribute
        public Date datetime;

        @Attribute
        public float quantity;

        @Attribute(required = false)
        public Float price;

        /** Уникальный идентификатор группы в базе данных */
        @Attribute(required = false)
        public long rowId;

        /** Флаг предписывающий удалить элемент из базы данных */
        @Attribute(required = false)
        public Boolean delete;
    }

    /** Версия {@link Swap} для использования в корневом элементе XML {@code catalog}. Содержит обязательный атрибут
     * {@link #itemId} указывающий на элемент {@link Item} для которого совершена данная сделка */
    @Root
    public static class SwapEx extends Swap {
        @Attribute
        public long itemId;
    }

    /**
     * Основная единица каталога. Содержит описание товара и т.п.
     */
    @Root
    public static class Item {
        @Element(required = false)
        public Icon icon;

        @Element(required = false)
        public String note;

        @ElementList(entry = "swap", inline = true, required = false)
        public List<Swap> swapsList;

        /** Уникальный идентификатор элемента в базе данных. В частности, используется для привязки сделок {@link Swap} */
        @Attribute(required = false)
        public long rowId;

        @Attribute(required = false)
        public String name;

        /** Флаг предписывающий удалить элемент из базы данных */
        @Attribute(required = false)
        public Boolean delete;

        /** Остаток продукции на складе и т.п. Количество товара, которое не может быть выражено через {@link Swap} */
        @Attribute(required = false)
        public Long quantity;
    }

    /**
     * Используется для группировки элементов {@link Item}. Позволяет формировать структуру каталога включая в группу
     * как элементы, так и другие группы.
     */
    @Root
    public static class Group {
        @Element(required = false)
        public Icon icon;

        @Element(required = false)
        public String note;

        @ElementList(entry = "group", inline = true, required = false)
        public List<Group> groupsList;

        @ElementList(entry = "item", inline = true, required = false)
        public List<Item> itemsList;

        /** Уникальный идентификатор группы в базе данных */
        @Attribute(required = false)
        public long rowId;

        @Attribute(required = false)
        public String name;

        /** Флаг предписывающий удалить элемент из базы данных */
        @Attribute(required = false)
        public Boolean delete;
    }

    /**
     * Если элемент задан, при импорте данных, выводится отображается пользовательское соглашение с соответствующим текстом,
     * которое должно быть принято пользователем, чтобы импорт данных был выполнен
     */
    @Element(required = false)
    public String agreement;

    /**
     * Набор параметров базы данных. Набор параетров не фиксирован и может изменяться. В настоящее время поддерживаются:
     * <ul>
     *     <li>icon - идентификатор {@link Icon}, иконка ассоциированная с базой данных</li>
     *     <li>dailyReport - формат для ежедневного отчета</li>
     *     <li>dailyReportRow - формат строки ежедневного отчета</li>
     *     <li>monthlyReport - формат ежемесячного отчета</li>
     *     <li>monthlyReportRow - формат строки ежемесячного отчета</li>
     *     <li>colorPrimary - основной цвет цветовой схемы</li>
     *     <li>colorPrimaryDark - основной темный цвет</li>
     *     <li>colorPrimaryLight - основной светлый цвет</li>
     *     <li>colorAccent - цает для акцентирования</li>
     * </ul>
     */
    @ElementMap(entry = "pref", key = "name", attribute = true, inline = true, required = false)
    public Map<String, String> prefs;

    /**
     * Список групп в корне
     */
    @ElementList(entry = "group", inline = true, required = false)
    public List<Group> groups;

    /**
     * Список элементов в корне, не привязанных к конкретной группе
     */
    @ElementList(entry = "item", inline = true, required = false)
    public ArrayList<Item> items;

    /**
     * Список сделок в корне с непосредственным указанием связанного элемента через {@link SwapEx#itemId}
     */
    @ElementList(entry = "swap", inline = true, required = false)
    public ArrayList<SwapEx> swaps;

    /**
     * Имя файла базы данных
     */
    @Attribute
    public String title;

    /**
     * Верися базы данных
     */
    @Attribute(required = false)
    public String version;

    /**
     * Атрибут указывает, что при импорте данных нужно игнорировать идентификаторы элементов и групп, что позволяет,
     * например, выполнить слияние баз
     */
    @Attribute(required = false)
    public Boolean ignoreRowId;

    /**
     * Атрибут указывает, что при импорте данных нужно сравнивать имена элементов в базе данных и в файле.
     * <ul>
     *     <li>Если {@link #ignoreRowId}{@code = false}, то не совпадение имени элемента при совпадении {@code rowId}
     *     вызовет ошибку</li>
     *     <li>Если {@link #ignoreRowId}{@code = true}, то элементы идентифицируются по имени в пределах группы</li>
     * </ul>
     */
    @Attribute(required = false)
    public Boolean compareNames;

    @Override
    public String toString() {
        return "SaXML{" +
                "version='" + version + '\'' +
                ", title=" + title +
                '}';
    }

    /**
     * Десериализация
     * @param inputStream входной поток данных
     * @return результат десериализации функцией {@code Serializer#read(Class, InputStream)}
     * @throws Exception
     */
    public static SaXML get(InputStream inputStream) throws Exception {
        Serializer serializer = new Persister();
        return serializer.read(SaXML.class, inputStream);
    }

    /**
     * Сериализация
     * @param xml объект для сериализации
     * @param outputStream поток для вывода данных
     * @throws Exception
     */
    public static void put(SaXML xml, OutputStream outputStream) throws Exception {
        new Persister().write(xml, outputStream);
    }
}
