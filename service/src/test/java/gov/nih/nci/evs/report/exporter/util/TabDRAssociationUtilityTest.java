package gov.nih.nci.evs.report.exporter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Association;

class TabDRAssociationUtilityTest {
	
	DelimitedRoleOutputUtility utility;
	CommonServices services;
	
	@BeforeEach
	void setup(){
		utility = new DelimitedRoleOutputUtility();
		services = new CommonServices();
	}

	@Test
	void testAssociationEntityOutput() {

		RestEntity entity = new RestEntity();
		entity.setCode("C000000");
		entity.setName("Association Source");
		entity.setAssociations(getAssociationList());
		List<RestEntity> associationEntities = new ArrayList<RestEntity>();
		associationEntities.add(entity);
		String test = utility.produceDelimitedAssociationOutputFromListWithHeading(associationEntities, entity.getCode(), entity.getName(), "\t");
		String[] strings = StringUtils.split(test,"\n");
		assertEquals(strings[0],"concept code\tconcept name\tassociation\ttarget code\ttarget name\r");
		assertEquals(strings[1],"C000000\tAssociation Source\thasAssociationOf\tC1111\ttarget1\r");
		assertEquals(strings[2],"C000000\tAssociation Source\thasAnyAssociationOf\tC2222\ttarget2\r");
		assertEquals(strings[3],"C000000\tAssociation Source\thasSomeAssociationOf\tC3333\ttarget3\r");
		assertEquals(strings[4],"C000000\tAssociation Source\thasSomeAssociationOf\tC4444\ttarget4\r");
		assertEquals(strings[5], "C000000\tAssociation Source\thasAssociationOf\tC5555\ttarget5\r");
		assertEquals(strings[6],"C000000\tAssociation Source\thasAssociationOf\tC6666\ttarget6\r");
	}
	
	@Test
	void testMultiAssociationEntityOutput() {
		
		List<RestEntity> associationEntities = new ArrayList<RestEntity>();

		RestEntity entity = new RestEntity();
		entity.setCode("C000000");
		entity.setName("Association Source");
		entity.setAssociations(getAssociationList());
		RestEntity entity1 = new RestEntity();
		entity1.setCode("C000001");
		entity1.setName("Association Source1");
		entity1.setAssociations(getAssociationList1());
		RestEntity entity2 = new RestEntity();
		entity2.setCode("C000009");
		entity2.setName("Association Source9");
		entity2.setAssociations(getAssociationList2());
		associationEntities.add(entity);
		associationEntities.add(entity1);
		associationEntities.add(entity2);
		String test = utility.produceDelimitedAssociationOutputFromListWithHeading(associationEntities, "C000000", "hasAssociationOf", "\t");
		String[] strings = StringUtils.split(test,"\n");
		assertEquals(strings[0],"concept code\tconcept name\tassociation\ttarget code\ttarget name\r");
		assertEquals(strings[1],"C000000\tAssociation Source\thasAssociationOf\tC1111\ttarget1\r");
		assertEquals(strings[2],"C000000\tAssociation Source\thasAnyAssociationOf\tC2222\ttarget2\r");
		assertEquals(strings[3],"C000000\tAssociation Source\thasSomeAssociationOf\tC3333\ttarget3\r");
		assertEquals(strings[4],"C000000\tAssociation Source\thasSomeAssociationOf\tC4444\ttarget4\r");
		assertEquals(strings[5], "C000000\tAssociation Source\thasAssociationOf\tC5555\ttarget5\r");
		assertEquals(strings[6],"C000000\tAssociation Source\thasAssociationOf\tC6666\ttarget6\r");
		assertEquals(strings[7],"C000001\tAssociation Source1\thasAssociationOf\tC1112\ttarget12\r");
		assertEquals(strings[8],"C000001\tAssociation Source1\thasAnyAssociationOf\tC2223\ttarget23\r");
		assertEquals(strings[9],"C000001\tAssociation Source1\thasSomeAssociationOf\tC3334\ttarget34\r");
		assertEquals(strings[10],"C000001\tAssociation Source1\thasSomeAssociationOf\tC4445\ttarget45\r");
		assertEquals(strings[11],"C000001\tAssociation Source1\thasAssociationOf\tC5556\ttarget56\r");
		assertEquals(strings[12],"C000001\tAssociation Source1\thasUnrelatedRol\tC6667\ttarget67\r");
		assertEquals(strings[13],"C000009\tAssociation Source9\thasAssociationOf\tC1110\ttarget10\r");
		assertEquals(strings[14],"C000009\tAssociation Source9\thasAnyAssociationOf\tC2221\ttarget21\r");
		assertEquals(strings[15],"C000009\tAssociation Source9\thasSomeAssociationOf\tC3332\ttarget32\r");
		assertEquals(strings[16],"C000009\tAssociation Source9\thasSomeAssociationOf\tC4443\ttarget43\r");
		assertEquals(strings[17],"C000009\tAssociation Source9\thasAssociationOf\tC5554\ttarget54\r");
		assertEquals(strings[18],"C000009\tAssociation Source9\thasAssociationOf\tC6665\ttarget65\r");
		assertEquals(strings[19],"\r");
		assertEquals(strings[20],"\r");
		assertEquals(strings[21],"\r");
		assertEquals(strings[22],"Report Search Parameters: \r");
		assertEquals(strings[23],"\"|Input:  hasAssociationOf|\"\r");
		assertEquals(strings[24],"\"|Associations Selected: C000000|\"");
	}
	
	
	
	@Test
	void getSeparatedStringFromAssociationTest() {
		
		Association association = new Association();
		association.setType("hasAssociationOf");
		association.setRelatedCode("1111");
		association.setRelatedName("target1");
		String test = services.calculateAndProduceSpacedAssociations(association,"C9999", "TestName", "\t");
		assertEquals(test, "C9999\tTestName\thasAssociationOf\t1111\ttarget1");

	}

	
	private List<Association> getAssociationList() {
		List<Association> associations = new ArrayList<Association>();
		Association association = new Association();
		association.setType("hasAssociationOf");
		association.setRelatedCode("C1111");
		association.setRelatedName("target1");
		Association association1 = new Association();
		association1.setType("hasAnyAssociationOf");
		association1.setRelatedCode("C2222");
		association1.setRelatedName("target2");
		Association association2 = new Association();
		association2.setType("hasSomeAssociationOf");
		association2.setRelatedCode("C3333");
		association2.setRelatedName("target3");
		Association association3 = new Association();
		association3.setType("hasSomeAssociationOf");
		association3.setRelatedCode("C4444");
		association3.setRelatedName("target4");
		Association association4 = new Association();
		association4.setType("hasAssociationOf");
		association4.setRelatedCode("C5555");
		association4.setRelatedName("target5");
		Association association5 = new Association();
		association5.setType("hasAssociationOf");
		association5.setRelatedCode("C6666");
		association5.setRelatedName("target6");
		associations.add(association);
		associations.add(association1);
		associations.add(association2);
		associations.add(association3);
		associations.add(association4);
		associations.add(association5);
		return associations;	
	}
	
	private List<Association> getAssociationList1() {
		List<Association> associations = new ArrayList<Association>();
		Association association = new Association();
		association.setType("hasAssociationOf");
		association.setRelatedCode("C1112");
		association.setRelatedName("target12");
		Association association1 = new Association();
		association1.setType("hasAnyAssociationOf");
		association1.setRelatedCode("C2223");
		association1.setRelatedName("target23");
		Association association2 = new Association();
		association2.setType("hasSomeAssociationOf");
		association2.setRelatedCode("C3334");
		association2.setRelatedName("target34");
		Association association3 = new Association();
		association3.setType("hasSomeAssociationOf");
		association3.setRelatedCode("C4445");
		association3.setRelatedName("target45");
		Association association4 = new Association();
		association4.setType("hasAssociationOf");
		association4.setRelatedCode("C5556");
		association4.setRelatedName("target56");
		Association association5 = new Association();
		association5.setType("hasUnrelatedRol");
		association5.setRelatedCode("C6667");
		association5.setRelatedName("target67");
		associations.add(association);
		associations.add(association1);
		associations.add(association2);
		associations.add(association3);
		associations.add(association4);
		associations.add(association5);
		return associations;	
	}
	
	private List<Association> getAssociationList2() {
		List<Association> associations = new ArrayList<Association>();
		Association association = new Association();
		association.setType("hasAssociationOf");
		association.setRelatedCode("C1110");
		association.setRelatedName("target10");
		Association association1 = new Association();
		association1.setType("hasAnyAssociationOf");
		association1.setRelatedCode("C2221");
		association1.setRelatedName("target21");
		Association association2 = new Association();
		association2.setType("hasSomeAssociationOf");
		association2.setRelatedCode("C3332");
		association2.setRelatedName("target32");
		Association association3 = new Association();
		association3.setType("hasSomeAssociationOf");
		association3.setRelatedCode("C4443");
		association3.setRelatedName("target43");
		Association association4 = new Association();
		association4.setType("hasAssociationOf");
		association4.setRelatedCode("C5554");
		association4.setRelatedName("target54");
		Association association5 = new Association();
		association5.setType("hasAssociationOf");
		association5.setRelatedCode("C6665");
		association5.setRelatedName("target65");
		associations.add(association);
		associations.add(association1);
		associations.add(association2);
		associations.add(association3);
		associations.add(association4);
		associations.add(association5);
		return associations;	
	}

}
