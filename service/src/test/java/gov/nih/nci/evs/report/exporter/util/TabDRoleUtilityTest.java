package gov.nih.nci.evs.report.exporter.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.Role;

class TabDRoleUtilityTest {
	
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
		System.out.println(utility.produceDelimitedOutputFromListWithHeading(roleEntities, entity.getCode(), entity.getName(), "\t"));
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
		System.out.println(utility.produceDelimitedOutputFromListWithHeading(roleEntities, entity.getCode(), entity.getName(), "\t"));
	}
	
	
	
	@Test
	void getSeparatedStringFromRoleTest() {
		
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("1111");
		role.setRelatedName("target1");
		String test = services.calculateAndProduceSpacedRoles(role,"C9999", "TestName", "\t");
		System.out.println(test);
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
