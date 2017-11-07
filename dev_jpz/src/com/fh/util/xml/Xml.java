package com.fh.util.xml;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Xml
{
  public static String[] extractSimpleXMLResult(String xml, String[] keyList)
  {
    String[] arrayOfString1;
    Document document = null;
    try {
      document = DocumentHelper.parseText(xml);
    }
    catch (DocumentException ex)
    {
      return null;
    }

    String[] result = new String[keyList.length];
    //int i = 0;
    Element root = document.getRootElement();

    int j = (arrayOfString1 = keyList).length; for (int i = 0; i < j; ++i) { String key = arrayOfString1[i];
      result[(i++)] = root.element(key).getText();
    }

    return result;
  }

  public static List<String> extractSimpleXMLResultArray(String xml)
  {
    Document document = null;
    try {
      document = DocumentHelper.parseText(xml);
    }
    catch (DocumentException ex)
    {
      return null;
    }

    List result = new ArrayList();
    Element root = document.getRootElement();
    Iterator iterator = root.elementIterator();

    while (iterator.hasNext()) {
      Element element = (Element)iterator.next();
      result.add(element.getText());
    }

    return result;
  }

  public static Map<String, Object> extractSimpleXMLResultMap(String xml)
  {
    Document document = null;
    try {
      document = DocumentHelper.parseText(xml);
    }
    catch (DocumentException ex)
    {
      return null;
    }

    Map result = new HashMap();
    Element root = document.getRootElement();
    Iterator iterator = root.elementIterator();

    while (iterator.hasNext()) {
      Element element = (Element)iterator.next();
      result.put(element.getName(), element.getText());
    }

    return result;
  }

  public static List<String[]> extractMultiXMLResult(String xml, String sectionNodeName, String[] keyList)
  {
    Document document = null;
    try {
      document = DocumentHelper.parseText(xml);
    }
    catch (DocumentException ex)
    {
      return null;
    }

    List result = new ArrayList();
    Element root = document.getRootElement();
    Iterator iterator = root.elementIterator();

    while (iterator.hasNext()) {
      Element element = (Element)iterator.next();

      if (element.getName().equals(sectionNodeName)) {
        String[] arrayOfString1;
        String[] string = new String[keyList.length];
        //int i = 0;

        int j = (arrayOfString1 = keyList).length; for (int i = 0; i < j; ++i) { String key = arrayOfString1[i];
          string[(i++)] = element.element(key).getText();
        }

        result.add(string);
      }
    }

    return result;
  }

  public static List<String[]> extractMultiXMLResultFromAttribute(String xml, String sectionNodeName, String[] keyList)
  {
    Document document = null;
    try {
      document = DocumentHelper.parseText(xml);
    }
    catch (DocumentException ex)
    {
      return null;
    }

    List result = new ArrayList();
    Element root = document.getRootElement();
    Iterator iterator = root.elementIterator();

    while (iterator.hasNext()) {
      Element element = (Element)iterator.next();

      if (element.getName().equals(sectionNodeName)) {
        String[] arrayOfString1;
        String[] string = new String[keyList.length];
       // int i = 0;

        int j = (arrayOfString1 = keyList).length; for (int i = 0; i < j; ++i) { String key = arrayOfString1[i];
          string[(i++)] = element.attribute(key).getText();
        }

        result.add(string);
      }
    }

    return result;
  }

  public static void main(String[] args) {
    String xml = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><FromUserName><![CDATA[fromUser]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content><MsgId>1234567890123456</MsgId></xml>";

    Map map = extractSimpleXMLResultMap(xml);
    System.out.println(map.get("MsgType"));
  }
}