package com.app.jueee.concurrency.chapter04.common;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.app.jueee.concurrency.chapter04.common.CommonInformationItem;

/**
 * 实现了一个 SAX（Simple API for XML 的缩写）解析器。 它可以解析该文件并且创建一个 CommonInformationItem 列表。
 * 
 * @author hzweiyongqiang
 */
public class RSSDataCapturer extends DefaultHandler {

    private final int IN_TITLE = 1;
    private final int IN_LINK = 2;
    private final int IN_DESCRIPTION = 3;
    private final int IN_PUBDATE = 4;
    private final int IN_GUID = 5;

    private int status = 0;
    private CommonInformationItem item;
    private List<CommonInformationItem> list;
    private SimpleDateFormat formater;
    private StringBuffer buffer;

    private String name;

    public RSSDataCapturer(String name) {
        formater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        buffer = new StringBuffer();
        this.name = name;
    }

    public List<CommonInformationItem> load(String resource) {

        list = new ArrayList<>();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

            // get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            // parse the file and also register this class for call backs
            sp.parse(resource, this);

        } catch (SAXException se) {
            System.err.printf("%s\n", resource);
            se.printStackTrace();
        } catch (ParserConfigurationException pce) {
            System.err.printf("%s\n", resource);
            pce.printStackTrace();
        } catch (IOException ie) {
            System.err.printf("%s\n", resource);
            ie.printStackTrace();
        }

        return list;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // System.out.println("Texto: "+new String(ch,start,length));

        String txt = new String(ch, start, length);
        buffer.append(txt.trim());

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        /*System.out.println("****END ELEMENT****");
        System.out.println("Uri: "+uri);
        System.out.println("Local Name: "+localName);
        System.out.println("QName: "+qName);
        System.out.println("****END ELEMENT****");*/
        if ((item != null) && (qName.equalsIgnoreCase("title"))) {
            item.setTitle(buffer.toString());
            status = 0;

        }
        if ((item != null) && (qName.equalsIgnoreCase("link"))) {
            item.setLink(buffer.toString());
            status = 0;
        }
        if ((item != null) && (qName.equalsIgnoreCase("description"))) {
            item.addDescripcion(buffer.toString());
            status = 0;
        }
        if ((item != null) && (qName.equalsIgnoreCase("pubdate"))) {
            item.setTxtDate(buffer.toString());
            try {
                item.setDate(formater.parse(buffer.toString()));
            } catch (ParseException e) {
                item.setDate(new Date());
            }
            status = 0;
        }
        if ((item != null) && (qName.equalsIgnoreCase("guid"))) {
            item.setId(buffer.toString());
            status = 0;
        }
        if (qName.equalsIgnoreCase("item")) {
            if (item.getId() == null) {
                item.setId("" + item.getDescription().hashCode());
            }
            list.add(item);
            item = null;
        }
        buffer = new StringBuffer();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // TODO Auto-generated method stub
        /*System.out.println("****START ELEMENT****");
        System.out.println("Uri: "+uri);
        System.out.println("Local Name: "+localName);
        System.out.println("QName: "+qName);
        System.out.println("****START ELEMENT****");*/

        if ((item != null) && (qName.equalsIgnoreCase("title"))) {
            status = IN_TITLE;
            return;
        }
        if ((item != null) && (qName.equalsIgnoreCase("link"))) {
            status = IN_LINK;
            return;
        }
        if ((item != null) && (qName.equalsIgnoreCase("description"))) {
            status = IN_DESCRIPTION;
            return;
        }
        if ((item != null) && (qName.equalsIgnoreCase("pubdate"))) {
            status = IN_PUBDATE;
            return;
        }
        if ((item != null) && (qName.equalsIgnoreCase("guid"))) {
            status = IN_GUID;
            return;
        }
        if (qName.equalsIgnoreCase("item")) {
            item = new CommonInformationItem();
            item.setSource(name);
        }
    }
}
