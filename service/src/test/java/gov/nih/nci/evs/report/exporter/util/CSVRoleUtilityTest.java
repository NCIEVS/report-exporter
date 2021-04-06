package gov.nih.nci.evs.report.exporter.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;

class CSVRoleUtilityTest {
	
	DelimitedRoleOutputUtility utility;
	CommonServices services;
	
	@BeforeEach
	void setup(){
		utility = new DelimitedRoleOutputUtility();
		services = new CommonServices();
	}

	@Test
	void testRoleEntityOutput() {

		RestEntity entity = new RestEntity();
		entity.setCode("C000000");
		entity.setName("Role Source");
		entity.setRoles(getRoleList());
		List<RestEntity> roleEntities = new ArrayList<RestEntity>();
		roleEntities.add(entity);
		System.out.println(utility.produceDelimitedOutputFromListWithHeading(roleEntities, entity.getCode(), entity.getName(), ","));
	}
	
	@Test
	void testMultiRoleEntityOutput() {
		
		List<RestEntity> roleEntities = new ArrayList<RestEntity>();

		RestEntity entity = new RestEntity();
		entity.setCode("C000000");
		entity.setName("Role Source");
		entity.setRoles(getRoleList());
		RestEntity entity1 = new RestEntity();
		entity1.setCode("C000001");
		entity1.setName("Role Source1");
		entity1.setRoles(getRoleList1());
		RestEntity entity2 = new RestEntity();
		entity2.setCode("C000009");
		entity2.setName("Role Source9");
		entity2.setRoles(getRoleList2());
		roleEntities.add(entity);
		roleEntities.add(entity1);
		roleEntities.add(entity2);
		String test = utility.produceDelimitedOutputFromListWithHeading(roleEntities, "C000000", "hasRoleOf", ",");
		System.out.println(test);
		String[] strings = StringUtils.split(test,"\n");
		assertEquals(strings[0],"concept code,concept name,role,target code,target name\r");
		assertEquals(strings[1],"C000000,Role Source,hasRoleOf,C1111,target1\r");
		assertEquals(strings[2],"C000000,Role Source,hasAnyRoleOf,C2222,target2\r");
		assertEquals(strings[3],"C000000,Role Source,hasSomeRoleOf,C3333,target3\r");
		assertEquals(strings[4],"C000000,Role Source,hasSomeRoleOf,C4444,target4\r");
		assertEquals(strings[5], "C000000,Role Source,hasRoleOf,C5555,target5\r");
		assertEquals(strings[6],"C000000,Role Source,hasRoleOf,C6666,target6\r");
		assertEquals(strings[7],"C000001,Role Source1,hasRoleOf,C1112,target12\r");
		assertEquals(strings[8],"C000001,Role Source1,hasAnyRoleOf,C2223,target23\r");
		assertEquals(strings[9],"C000001,Role Source1,hasSomeRoleOf,C3334,target34\r");
		assertEquals(strings[10],"C000001,Role Source1,hasSomeRoleOf,C4445,target45\r");
		assertEquals(strings[11],"C000001,Role Source1,hasRoleOf,C5556,target56\r");
		assertEquals(strings[12],"C000001,Role Source1,hasUnrelatedRol,C6667,target67\r");
		assertEquals(strings[13],"C000009,Role Source9,hasRoleOf,C1110,target10\r");
		assertEquals(strings[14],"C000009,Role Source9,hasAnyRoleOf,C2221,target21\r");
		assertEquals(strings[15],"C000009,Role Source9,hasSomeRoleOf,C3332,target32\r");
		assertEquals(strings[16],"C000009,Role Source9,hasSomeRoleOf,C4443,target43\r");
		assertEquals(strings[17],"C000009,Role Source9,hasRoleOf,C5554,target54\r");
		assertEquals(strings[18],"C000009,Role Source9,hasRoleOf,C6665,target65\r");
		assertEquals(strings[19],"\r");
		assertEquals(strings[20],"\r");
		assertEquals(strings[21],"\r");
		assertEquals(strings[22],"Report Search Parameters: \r");
		assertEquals(strings[23],"\"|Input:  hasRoleOf|\"\r");
		assertEquals(strings[24],"\"|Roles Selected: C000000|\"");

	}
	
	
	
	@Test
	void getSeparatedStringFromRoleTest() {
		
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("1111");
		role.setRelatedName("target1");
		String test = services.calculateAndProduceSpacedRoles(role,"C9999", "TestName", ",");
		System.out.println(test);
		assertEquals(test, "C9999,TestName,hasRoleOf,1111,target1");
	}

	
	private List<Role> getRoleList() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("C1111");
		role.setRelatedName("target1");
		Role role1 = new Role();
		role1.setType("hasAnyRoleOf");
		role1.setRelatedCode("C2222");
		role1.setRelatedName("target2");
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("C3333");
		role2.setRelatedName("target3");
		Role role3 = new Role();
		role3.setType("hasSomeRoleOf");
		role3.setRelatedCode("C4444");
		role3.setRelatedName("target4");
		Role role4 = new Role();
		role4.setType("hasRoleOf");
		role4.setRelatedCode("C5555");
		role4.setRelatedName("target5");
		Role role5 = new Role();
		role5.setType("hasRoleOf");
		role5.setRelatedCode("C6666");
		role5.setRelatedName("target6");
		roles.add(role);
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		roles.add(role4);
		roles.add(role5);
		return roles;	
	}
	
	private List<Role> getRoleList1() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("C1112");
		role.setRelatedName("target12");
		Role role1 = new Role();
		role1.setType("hasAnyRoleOf");
		role1.setRelatedCode("C2223");
		role1.setRelatedName("target23");
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("C3334");
		role2.setRelatedName("target34");
		Role role3 = new Role();
		role3.setType("hasSomeRoleOf");
		role3.setRelatedCode("C4445");
		role3.setRelatedName("target45");
		Role role4 = new Role();
		role4.setType("hasRoleOf");
		role4.setRelatedCode("C5556");
		role4.setRelatedName("target56");
		Role role5 = new Role();
		role5.setType("hasUnrelatedRol");
		role5.setRelatedCode("C6667");
		role5.setRelatedName("target67");
		roles.add(role);
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		roles.add(role4);
		roles.add(role5);
		return roles;	
	}
	
	private List<Role> getRoleList2() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("C1110");
		role.setRelatedName("target10");
		Role role1 = new Role();
		role1.setType("hasAnyRoleOf");
		role1.setRelatedCode("C2221");
		role1.setRelatedName("target21");
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("C3332");
		role2.setRelatedName("target32");
		Role role3 = new Role();
		role3.setType("hasSomeRoleOf");
		role3.setRelatedCode("C4443");
		role3.setRelatedName("target43");
		Role role4 = new Role();
		role4.setType("hasRoleOf");
		role4.setRelatedCode("C5554");
		role4.setRelatedName("target54");
		Role role5 = new Role();
		role5.setType("hasRoleOf");
		role5.setRelatedCode("C6665");
		role5.setRelatedName("target65");
		roles.add(role);
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		roles.add(role4);
		roles.add(role5);
		return roles;	
	}

}
