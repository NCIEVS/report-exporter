package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import gov.nih.nci.evs.report.exporter.model.Association;
import gov.nih.nci.evs.report.exporter.model.Rel;
import gov.nih.nci.evs.report.exporter.service.AssociationService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

class AssociationServiceTest {
	
	AssociationService service;

	@BeforeEach
	void setUp() throws Exception {

		service = new AssociationService();
		}

	@Test
	void testAssociationWeighting() {
		Hashtable<String, Rel> associations = new Hashtable<String, Rel>();
		Association association = new Association();
		association.setType("hasAssociationOf");
		association.setRelatedCode("1111");
		association.setRelatedName("target1");
		Association association1 = new Association();
		association1.setType("hasAssociationOf");
		association1.setRelatedCode("2222");
		association1.setRelatedName("target2");
		association.setWeight(1);
		associations.put(association.getType(), association);
		assertTrue(associations.size() == 1);
		assertEquals(associations.get(association.getType()).getWeight(), 1);
		assertEquals(associations.get(association1.getType()).getWeight(), 1);
		CommonServices.saveOrUpdateWeightedRels(association, associations);
		assertTrue(associations.size() == 1);
		assertEquals(associations.get(association.getType()).getWeight(), 2);
		assertEquals(associations.get(association1.getType()).getWeight(), 2);
		CommonServices.saveOrUpdateWeightedRels(association1, associations);
		assertTrue(associations.size() == 1);
		assertEquals(associations.get(association.getType()).getWeight(), 3);
		assertEquals(associations.get(association1.getType()).getWeight(), 3);
		Association association2 = new Association();
		association2.setType("hasSomeAssociationOf");
		association2.setRelatedCode("3333");
		association2.setRelatedName("target3");
		CommonServices.saveOrUpdateWeightedRels(association2, associations);
		assertTrue(associations.size() == 2);
		assertEquals(associations.get(association.getType()).getWeight(), 3);
		assertEquals(associations.get(association1.getType()).getWeight(), 3);
		assertEquals(associations.get(association2.getType()).getWeight(), 1);
		Association association3 = new Association();
		association3.setType("hasSomeAssociationOf");
		association3.setRelatedCode("4444");
		association3.setRelatedName("target4");
		Association association4 = new Association();
		association4.setType("hasAssociationOf");
		association4.setRelatedCode("5555");
		association4.setRelatedName("target5");
		CommonServices.saveOrUpdateWeightedRels(association3, associations);
		assertTrue(associations.size() == 2);
		assertEquals(associations.get(association.getType()).getWeight(), 3);
		assertEquals(associations.get(association1.getType()).getWeight(), 3);
		assertEquals(associations.get(association2.getType()).getWeight(), 2);
		assertEquals(associations.get(association3.getType()).getWeight(), 2);
		assertEquals(associations.get(association4.getType()).getWeight(), 3);
		CommonServices.saveOrUpdateWeightedRels(association4, associations);
		assertTrue(associations.size() == 2);
		assertEquals(associations.get(association.getType()).getWeight(), 4);
		assertEquals(associations.get(association1.getType()).getWeight(), 4);
		assertEquals(associations.get(association2.getType()).getWeight(), 2);
		assertEquals(associations.get(association3.getType()).getWeight(), 2);
		assertEquals(associations.get(association4.getType()).getWeight(), 4);
		
	}
	
	@Test
	void testAssociationSorting() {
		
		AssociationService mockedAssociationService = spy(service);
		doReturn(getAssociationList())
		.when(mockedAssociationService).getAssociationsForCodes(
				Mockito.anyString());
		List<Rel> wAssociations = mockedAssociationService.getDistinctWeightedRelsForEntityCodes( getAssociationList());
		List<Rel> associations = CommonServices.sortRelListByWeight(wAssociations);
		assertEquals(associations.size(), 3);
		assertEquals(associations.stream().filter(x -> x.getType().equals("hasAssociationOf")).findAny().get().getType(), "hasAssociationOf");
		assertEquals(associations.stream().filter(x -> x.getType().equals("hasAnyAssociationOf")).findAny().get().getType(), "hasAnyAssociationOf");
		assertEquals(associations.stream().filter(x -> x.getType().equals("hasSomeAssociationOf")).findAny().get().getType(), "hasSomeAssociationOf");
		assertEquals(associations.get(0).getType(), "hasAssociationOf");
		assertEquals(associations.get(1).getType(), "hasSomeAssociationOf");
		assertEquals(associations.get(2).getType(), "hasAnyAssociationOf");
		associations.forEach(x ->System.out.println(x.toString()));
	}

	private List<Association> getAssociationList() {
		List<Association> associations = new ArrayList<Association>();
		Association association = new Association();
		association.setType("hasAssociationOf");
		association.setRelatedCode("1111");
		association.setRelatedName("target1");
		Association association1 = new Association();
		association1.setType("hasAnyAssociationOf");
		association1.setRelatedCode("2222");
		association1.setRelatedName("target2");
		Association association2 = new Association();
		association2.setType("hasSomeAssociationOf");
		association2.setRelatedCode("3333");
		association2.setRelatedName("target3");
		Association association3 = new Association();
		association3.setType("hasSomeAssociationOf");
		association3.setRelatedCode("4444");
		association3.setRelatedName("target4");
		Association association4 = new Association();
		association4.setType("hasAssociationOf");
		association4.setRelatedCode("5555");
		association4.setRelatedName("target5");
		Association association5 = new Association();
		association5.setType("hasAssociationOf");
		association5.setRelatedCode("6666");
		association5.setRelatedName("target6");
		associations.add(association);
		associations.add(association1);
		associations.add(association2);
		associations.add(association3);
		associations.add(association4);
		associations.add(association5);
		return associations;	
	}

}
