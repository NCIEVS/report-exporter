package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.model.WeightedRole;
import gov.nih.nci.evs.report.exporter.service.RoleService;

class RoleServiceTest {
	
	RoleService service;
	
	@BeforeEach
	void setUp() throws Exception {

		service = new RoleService();

		}

	@Test
	void test() {
		Hashtable<String, WeightedRole> roles = new Hashtable<String, WeightedRole>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("1111");
		role.setRelatedName("target1");
		Role role1 = new Role();
		role1.setType("hasRoleOf");
		role1.setRelatedCode("2222");
		role1.setRelatedName("target2");
		WeightedRole wRole = new WeightedRole(role, 1);
		roles.put(role.getType(), wRole);
		assertTrue(roles.size() == 1);
		assertEquals(roles.get(role.getType()).getWeight(), 1);
		assertEquals(roles.get(role1.getType()).getWeight(), 1);
		service.saveOrUpdateWeightedRoles(role, roles);
		assertTrue(roles.size() == 1);
		assertEquals(roles.get(role.getType()).getWeight(), 2);
		assertEquals(roles.get(role1.getType()).getWeight(), 2);
		service.saveOrUpdateWeightedRoles(role1, roles);
		assertTrue(roles.size() == 1);
		assertEquals(roles.get(role.getType()).getWeight(), 3);
		assertEquals(roles.get(role1.getType()).getWeight(), 3);
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("3333");
		role2.setRelatedName("target3");
		service.saveOrUpdateWeightedRoles(role2, roles);
		assertTrue(roles.size() == 2);
		assertEquals(roles.get(role.getType()).getWeight(), 3);
		assertEquals(roles.get(role1.getType()).getWeight(), 3);
		assertEquals(roles.get(role2.getType()).getWeight(), 1);
		Role role3 = new Role();
		role3.setType("hasSomeRoleOf");
		role3.setRelatedCode("4444");
		role3.setRelatedName("target4");
		Role role4 = new Role();
		role4.setType("hasRoleOf");
		role4.setRelatedCode("5555");
		role4.setRelatedName("target5");
		service.saveOrUpdateWeightedRoles(role3, roles);
		assertTrue(roles.size() == 2);
		assertEquals(roles.get(role.getType()).getWeight(), 3);
		assertEquals(roles.get(role1.getType()).getWeight(), 3);
		assertEquals(roles.get(role2.getType()).getWeight(), 2);
		assertEquals(roles.get(role3.getType()).getWeight(), 2);
		assertEquals(roles.get(role4.getType()).getWeight(), 3);
		service.saveOrUpdateWeightedRoles(role4, roles);
		assertTrue(roles.size() == 2);
		assertEquals(roles.get(role.getType()).getWeight(), 4);
		assertEquals(roles.get(role1.getType()).getWeight(), 4);
		assertEquals(roles.get(role2.getType()).getWeight(), 2);
		assertEquals(roles.get(role3.getType()).getWeight(), 2);
		assertEquals(roles.get(role4.getType()).getWeight(), 4);
		
	}

}
