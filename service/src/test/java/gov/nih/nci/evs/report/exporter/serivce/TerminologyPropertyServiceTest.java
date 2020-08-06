package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;
import gov.nih.nci.evs.report.exporter.service.TerminologyPropertyService;

class TerminologyPropertyServiceTest {
	



		@InjectMocks
		private TerminologyPropertyService service;
		
		RestPropertyMetadata[] props;

	@BeforeEach
	void setUp() throws Exception {
		service = new TerminologyPropertyService();
		RestPropertyMetadata r1 = new RestPropertyMetadata();
		r1.setCode("p1");
		r1.setName("ALT_DEFINITION");
		r1.setTerminology("ncit");
		r1.setVersion("20.19d");
		RestPropertyMetadata r2 = new RestPropertyMetadata();
		r2.setCode("p2");
		r2.setName("Accepted_Therapeutic_Use_For");
		r2.setTerminology("ncit");
		r2.setVersion("20.19d");
		RestPropertyMetadata r3 = new RestPropertyMetadata();
		r3.setCode("p3");
		r3.setName("BioCarta_ID");
		r3.setTerminology("ncit");
		r3.setVersion("20.19d");
		RestPropertyMetadata r4 = new RestPropertyMetadata();
		r4.setCode("p4");
		r4.setName("CHEBI_ID");
		r4.setTerminology("ncit");
		r4.setVersion("20.19d");
		RestPropertyMetadata r5 = new RestPropertyMetadata();
		r5.setCode("p5");
		r5.setName("DEFINITION");
		r5.setTerminology("ncit");
		r5.setVersion("20.19d");
		RestPropertyMetadata r6 = new RestPropertyMetadata();
		r6.setCode("p6");
		r6.setName("DesignNote");
		r6.setTerminology("ncit");
		r6.setVersion("20.19d");
		RestPropertyMetadata r7 = new RestPropertyMetadata();
		r7.setCode("p7");
		r7.setName("Extensible_List");
		r7.setTerminology("ncit");
		r7.setVersion("20.19d");
		RestPropertyMetadata r8 = new RestPropertyMetadata();
		r8.setCode("p8");
		r8.setName("FDA_Table");
		r8.setTerminology("ncit");
		r8.setVersion("20.19d");
		RestPropertyMetadata r9 = new RestPropertyMetadata();
		r9.setCode("p9");
		r9.setName("FULL_SYN");
		r9.setTerminology("ncit");
		r9.setVersion("20.19d");

		
		props = new RestPropertyMetadata[] { 
				r1,
				r2,
				r3,
				r4,
				r5,
				r6,
				r7,
				r8,
				r9};
		
		ReflectionTestUtils.setField(service, "filterList", "BioCarta_ID,Extensible_List,FDA_Table");
		
	}

	@Test
	void test() {
		RestPropertyMetadata[] results = service.getFilteredList(props);
		assertTrue(Stream.of(results).anyMatch(x -> x.getName().equals("ALT_DEFINITION")));
		assertTrue(Stream.of(results).anyMatch(x -> x.getName().equals("Accepted_Therapeutic_Use_For")));
		assertFalse(Stream.of(results).anyMatch(x -> x.getName().equals("BioCarta_ID")));
		assertTrue(Stream.of(results).anyMatch(x -> x.getName().equals("CHEBI_ID")));
		assertTrue(Stream.of(results).anyMatch(x -> x.getName().equals("DEFINITION")));
		assertTrue(Stream.of(results).anyMatch(x -> x.getName().equals("DesignNote")));
		assertFalse(Stream.of(results).anyMatch(x -> x.getName().equals("Extensible_List")));
		assertFalse(Stream.of(results).anyMatch(x -> x.getName().equals("FDA_Table")));
		assertTrue(Stream.of(results).anyMatch(x -> x.getName().equals("FULL_SYN")));
	}

}
