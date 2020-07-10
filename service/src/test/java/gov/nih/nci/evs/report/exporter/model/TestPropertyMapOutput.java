package gov.nih.nci.evs.report.exporter.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestPropertyMapOutput {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		PropertyMap map = new PropertyMap();
	      map.setType("Related To");
	      map.setTargetName("9861/3");
	      map.setTargetTermGroup("PT");
	      map.setTargetCode("MORPH");
	      map.setTargetTerminology("GDC");
	      
	      System.out.println(map.toString());
	      assertNotNull(map.toString());
	      assertEquals(map.toString(),"GDC PT MORPH 9861/3:Related To" );
	}
	
	@Test
	void test2() {
		PropertyMap map = new PropertyMap();
	      //map.setType("Related To");
	      map.setTargetName("9861/3");
	      map.setTargetTermGroup("PT");
	      map.setTargetCode("MORPH");
	      map.setTargetTerminology("GDC");
	      
	      System.out.println(map.toString());
	      assertNotNull(map.toString());
	      assertEquals(map.toString(),"GDC PT MORPH 9861/3:" );
	}
	
	@Test
	void test3() {
		PropertyMap map = new PropertyMap();
	      map.setType("Related To");
//	      map.setTargetName("9861/3");
	      map.setTargetTermGroup("PT");
	      map.setTargetCode("MORPH");
	      map.setTargetTerminology("GDC");
	      
	      System.out.println(map.toString());
	      assertNotNull(map.toString());
	      assertEquals(map.toString(),"GDC PT MORPH Related To");
	      
	}
	
	@Test
	void test4() {
		PropertyMap map = new PropertyMap();
	      map.setType("Related To");
	      map.setTargetName("9861/3");
//	      map.setTargetTermGroup("PT");
	      map.setTargetCode("MORPH");
	      map.setTargetTerminology("GDC");
	      
	      System.out.println(map.toString());
	      assertNotNull(map.toString());
	      assertEquals(map.toString(),"GDC MORPH 9861/3:Related To" );
	      
	}
	
	@Test
	void test5() {
		PropertyMap map = new PropertyMap();
	      map.setType("Related To");
	      map.setTargetName("9861/3");
	      map.setTargetTermGroup("PT");
//	      map.setTargetCode("MORPH");
	      map.setTargetTerminology("GDC");
	      
	      System.out.println(map.toString());
	      assertNotNull(map.toString());
	      assertEquals(map.toString(),"GDC PT 9861/3:Related To" );
	}
	
	@Test
	void test6() {
		PropertyMap map = new PropertyMap();
	      map.setType("Related To");
	      map.setTargetName("9861/3");
	      map.setTargetTermGroup("PT");
	      map.setTargetCode("MORPH");
//	      map.setTargetTerminology("GDC");	      
	      System.out.println(map.toString());
	      assertNotNull(map.toString());
	      assertEquals(map.toString(),"PT MORPH 9861/3:Related To" );
	}
	


}
