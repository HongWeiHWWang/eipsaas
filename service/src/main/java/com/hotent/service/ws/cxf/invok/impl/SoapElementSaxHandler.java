package com.hotent.service.ws.cxf.invok.impl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 将字符串转换为SOAPElement的处理类
 * @author heyifan
 *
 */
@SuppressWarnings("rawtypes")
public class SoapElementSaxHandler extends DefaultHandler
{
	
	private HashMap prefixURIMapping;
    private ArrayList uris;
    private SOAPElement rootElement;
    private SOAPElement currentElement;
    private SOAPFactory soapFactory;
    
    public SoapElementSaxHandler()
    {
        prefixURIMapping = new HashMap();
        uris = new ArrayList();
        rootElement = null;
        currentElement = null;
    }

    public SOAPElement getSOAPElement()
    {
        return rootElement;
    }

    public void startDocument()
        throws SAXException
    {
        try
        {
            soapFactory = SOAPFactory.newInstance();
        }
        catch(SOAPException e)
        {
            throw new SAXException("Can't create a SOAPFactory instance", e);
        }
    }

    @SuppressWarnings("unchecked")
	public void startPrefixMapping(String prefix, String uri)
    {
        prefixURIMapping.put(uri, prefix);
        uris.add(uri);
    }

    public void characters(char ch[], int start, int length)
        throws SAXException
    {
        String str = String.valueOf(ch);
        if(length > 0)
            try
            {
                currentElement.addTextNode(str.substring(start, start + length));
            }
            catch(SOAPException e)
            {
                throw new SAXException("Can't add a text node into SOAPElement from text", e);
            }
    }

    public void endElement(String uri, String localName, String qName)
    {
        if(currentElement != rootElement)
            currentElement = currentElement.getParentElement();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
        throws SAXException
    {
        String prefix = (String)prefixURIMapping.get(namespaceURI);
        try
        {
            if(rootElement == null && currentElement == null)
            {
                rootElement = soapFactory.createElement(localName, prefix, namespaceURI);
                currentElement = rootElement;
            } else
            {
                currentElement = currentElement.addChildElement(localName, prefix, namespaceURI);
            }
            if(uris.size() > 0)
            {
                for(int i = 0; i < uris.size(); i++)
                {
                    String uri = (String)uris.get(i);
                    String pre = (String)prefixURIMapping.get(uri);
                    currentElement.addNamespaceDeclaration(pre, uri);
                }

                uris.clear();
            }
            for(int i = 0; i < atts.getLength(); i++)
            {
                javax.xml.soap.Name attriName;
                if(atts.getURI(i) != null)
                {
                    String attriPre = (String)prefixURIMapping.get(atts.getURI(i));
                    attriName = soapFactory.createName(atts.getLocalName(i), attriPre, atts.getURI(i));
                } else
                {
                    attriName = soapFactory.createName(atts.getLocalName(i));
                }
                currentElement.addAttribute(attriName, atts.getValue(i));
            }

        }
        catch(SOAPException e)
        {
            throw new SAXException(e);
        }
    }
}