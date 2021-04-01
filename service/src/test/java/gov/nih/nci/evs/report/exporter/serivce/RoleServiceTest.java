package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import gov.nih.nci.evs.report.exporter.model.Rel;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.model.WeightedRel;
import gov.nih.nci.evs.report.exporter.service.RoleService;

class RoleServiceTest {
	
	RoleService service;

	@BeforeEach
	void setUp() throws Exception {

		service = new RoleService();
		}

	@Test
	void testRoleWeighting() {
		Hashtable<String, Rel> roles = new Hashtable<String, Rel>();
		Role role = new Role();
		role.setType("hasRoleOf");
		role.setRelatedCode("1111");
		role.setRelatedName("target1");
		Role role1 = new Role();
		role1.setType("hasRoleOf");
		role1.setRelatedCode("2222");
		role1.setRelatedName("target2");
		role.setWeight(1);
		roles.put(role.getType(), role);
		assertTrue(roles.size() == 1);
		assertEquals(roles.get(role.getType()).getWeight(), 1);
		assertEquals(roles.get(role1.getType()).getWeight(), 1);
		service.saveOrUpdateWeightedRels(role, roles);
		assertTrue(roles.size() == 1);
		assertEquals(roles.get(role.getType()).getWeight(), 2);
		assertEquals(roles.get(role1.getType()).getWeight(), 2);
		service.saveOrUpdateWeightedRels(role1, roles);
		assertTrue(roles.size() == 1);
		assertEquals(roles.get(role.getType()).getWeight(), 3);
		assertEquals(roles.get(role1.getType()).getWeight(), 3);
		Role role2 = new Role();
		role2.setType("hasSomeRoleOf");
		role2.setRelatedCode("3333");
		role2.setRelatedName("target3");
		service.saveOrUpdateWeightedRels(role2, roles);
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
		service.saveOrUpdateWeightedRels(role3, roles);
		assertTrue(roles.size() == 2);
		assertEquals(roles.get(role.getType()).getWeight(), 3);
		assertEquals(roles.get(role1.getType()).getWeight(), 3);
		assertEquals(roles.get(role2.getType()).getWeight(), 2);
		assertEquals(roles.get(role3.getType()).getWeight(), 2);
		assertEquals(roles.get(role4.getType()).getWeight(), 3);
		service.saveOrUpdateWeightedRels(role4, roles);
		assertTrue(roles.size() == 2);
		assertEquals(roles.get(role.getType()).getWeight(), 4);
		assertEquals(roles.get(role1.getType()).getWeight(), 4);
		assertEquals(roles.get(role2.getType()).getWeight(), 2);
		assertEquals(roles.get(role3.getType()).getWeight(), 2);
		assertEquals(roles.get(role4.getType()).getWeight(), 4);
		
	}
	
	@Test
	void testRoleSorting() {
		
		RoleService mockedRoleService = spy(service);
		doReturn(getRoleList())
		.when(mockedRoleService).getRolesForCodes(
				Mockito.anyString());
		List<Rel> wRoles = mockedRoleService.getDistinctWeightedRelsForEntityCodes(Mockito.anyString());
		List<Rel> roles = service.sortRoleListByWeight(wRoles);
		assertEquals(roles.size(), 3);
		assertEquals(roles.stream().filter(x -> x.getType().equals("hasRoleOf")).findAny().get().getType(), "hasRoleOf");
		assertEquals(roles.stream().filter(x -> x.getType().equals("hasAnyRoleOf")).findAny().get().getType(), "hasAnyRoleOf");
		assertEquals(roles.stream().filter(x -> x.getType().equals("hasSomeRoleOf")).findAny().get().getType(), "hasSomeRoleOf");
		assertEquals(roles.get(0).getType(), "hasRoleOf");
		assertEquals(roles.get(1).getType(), "hasSomeRoleOf");
		assertEquals(roles.get(2).getType(), "hasAnyRoleOf");
		roles.forEach(x ->System.out.println(x.toString()));
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
