package com.patroclos.knapp.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

import com.patroclos.knapp.util.MsgManipulate;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PatroclosUnit {
	
	@Autowired
	private MockMsg mockMsg;
	
	@Autowired
	private MsgManipulate extractInf;
	
	String xmlExpected = null;
	String xmlActual = null;
	
	@Before
    public void setup() throws Throwable {
	
		xmlExpected = extractInf.prepareSend(mockMsg.getMsgExpectIn());
		xmlActual = mockMsg.getMsgExpectOut();
		
    }
	

	@Test
    public void testParse() throws Throwable, Exception {

		Diff d = new Diff(xmlExpected, xmlActual);
		assertTrue(d.identical());
		
		XMLAssert.assertXMLEqual(xmlExpected, xmlActual);

    }
	
	
	@Test
    public void testXpath() throws Throwable, Exception {

	XpathEngine eng = XMLUnit.newXpathEngine();

	 String input = xmlActual;
	 Document doc = XMLUnit.buildControlDocument(xmlActual);
	  
	 assertEquals("20180100", eng.evaluate("/UC_STOCK_LEVEL_IFD/CTRL_SEG/*[self::TRNVER or self::CLIENT_ID or self::ROUTE_ID]", doc));  
	 XMLAssert.assertXpathExists("/UC_STOCK_LEVEL_IFD/CTRL_SEG/*[self::TRNVER or self::CLIENT_ID or self::ROUTE_ID]", input);  
	 
	 assertEquals(3, eng.getMatchingNodes("/UC_STOCK_LEVEL_IFD/CTRL_SEG/*[self::TRNVER or self::CLIENT_ID or self::ROUTE_ID]", doc).getLength());
	
	}
	
	@Test
	public void testXmlReturn() throws Throwable {
		
		assertNotNull(extractInf.beautyXML( xmlActual));
		
	}

	
	@Test
	public void testXmlExtract() throws Throwable {
			
		 assertThat(extractInf.prepareSend(mockMsg.getMsgExpectIn())).isEqualTo(mockMsg.getMsgExpectOut());
		
	}
	
	
}
