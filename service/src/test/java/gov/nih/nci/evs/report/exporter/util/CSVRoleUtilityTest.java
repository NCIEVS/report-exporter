package gov.nih.nci.evs.report.exporter.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.nih.nci.evs.report.exporter.model.RestRolesEntity;
import gov.nih.nci.evs.report.exporter.model.Role;

class CSVRoleUtilityTest {
	
	CSVRoleUtility utility;
	CommonServices services;
	
	@BeforeEach
	void setup(){
		utility = new CSVRoleUtility();
		services = new CommonServices();
	}

	@Test
	void test() {

		RestRolesEntity entity = new RestRolesEntity();
		entity.setCode("000000");
		entity.setName("Role Source");
		entity.setRoles(getRoleList());
		List<RestRolesEntity> roleEntities = new ArrayList<RestRolesEntity>();
		roleEntities.add(entity);
		System.out.println(utility.produceCSVOutputFromListWithHeading(roleEntities, entity.getCode(), entity.getName(), ","));
	}
	
	@Test
	void getSeparatedStringFromRoleTest() {
		
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("1111");
		role.setRelatedName("target1");
		String test = services.calculateAndProduceSpacedRoles(role,"C9999", "TestName", ",");
		System.out.println(test);
	}

	
	private List<Role> getRoleList() {
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("1111");
		role.setRelatedName("target1");
		Role role1 = new Role();
		role1.setType("hasAnyRoleOf");
		role1.setRelatedCode("2222");
		role1.setRelatedName("target2");
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("3333");
		role2.setRelatedName("target3");
		Role role3 = new Role();
		role3.setType("hasSomeRoleOf");
		role3.setRelatedCode("4444");
		role3.setRelatedName("target4");
		Role role4 = new Role();
		role4.setType("hasRoleOf");
		role4.setRelatedCode("5555");
		role4.setRelatedName("target5");
		Role role5 = new Role();
		role5.setType("hasRoleOf");
		role5.setRelatedCode("6666");
		role5.setRelatedName("target6");
		roles.add(role);
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		roles.add(role4);
		roles.add(role5);
		return roles;	
	}

}
