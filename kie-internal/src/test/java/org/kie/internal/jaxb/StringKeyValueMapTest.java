package org.kie.internal.jaxb;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;

public class StringKeyValueMapTest {

    @Test
    public void test() throws Exception { 
       StringKeyObjectValueMap map = new StringKeyObjectValueMap();
       String [] keys = { 
               "one", "two" , "thr"
       };
       for( int i = 0; i < keys.length; ++i ) { 
           map.put(keys[i], keys[i]);
       }
       MapHolder mapHolder = new MapHolder();
       mapHolder.map = map;
       
       JAXBContext ctx = JAXBContext.newInstance(StringKeyObjectValueMap.class, MapHolder.class);
       StringWriter writer = new StringWriter();
       Marshaller marshaller = ctx.createMarshaller();
       marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
       marshaller.marshal(mapHolder, writer);
       writer.flush();
       String xmlStr = writer.toString();
      
       ByteArrayInputStream xmlStrInputStream = new ByteArrayInputStream(xmlStr.getBytes(Charset.forName("UTF-8")));

       Object jaxbObj = ctx.createUnmarshaller().unmarshal(xmlStrInputStream);
       MapHolder copyMapHolder = (MapHolder) jaxbObj;
       
       for( Entry<String, Object> entry : copyMapHolder.map.entrySet() ) { 
           String key = entry.getKey();
           assertNotNull( "key", key );
           assertNotNull( "null " + key + " value", entry.getValue() );
           assertEquals(  key + " value", map.get(key), copyMapHolder.map.get(key));
       }
    }
   
    @XmlRootElement(name="map-holder")
    private static class MapHolder { 
        @XmlElement
        public StringKeyObjectValueMap map;
    }
}
