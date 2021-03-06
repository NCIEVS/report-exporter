package gov.nih.nci.evs.report.exporter.serivce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.service.BranchResolutionService;
import gov.nih.nci.evs.report.exporter.service.EVSAPIBaseService;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@SpringBootTest
class BranchResolutionServiceTest {
	
	EVSAPIBaseService baseservice;
	
	
	BranchResolutionService service;

	
	@BeforeEach
	public  void setUp() {
		this.baseservice = mock(EVSAPIBaseService.class);
		service = new BranchResolutionService();
	}

 	@Test
	public void  branchResolutionFlatteningTest() {
		ChildEntity entity = new ChildEntity();
		List<ChildEntity> list = new ArrayList<ChildEntity>();
		List<ChildEntity> children = new ArrayList<ChildEntity>();
		List<ChildEntity> grandChildren = new ArrayList<ChildEntity>();
		List<ChildEntity> grandChildren2 = new ArrayList<ChildEntity>();

		ChildEntity child1 = new ChildEntity();
		child1.setCode("C00001");
		child1.setName("child1");
		child1.setLeaf(false);
		child1.setLevel("1");
		child1.setChildren(grandChildren);
		ChildEntity child2 = new ChildEntity();
		child2.setCode("C00002");
		child2.setName("child2");
		child2.setLeaf(true);
		child2.setLevel("1");
		ChildEntity child3 = new ChildEntity();
		child3.setCode("C00003");
		child3.setName("child3");
		child3.setLeaf(false);
		child3.setLevel("1");
		child3.setChildren(grandChildren2);
		ChildEntity grandchild1 = new ChildEntity();
		grandchild1.setCode("C00011");
		grandchild1.setName("grandchild1");
		grandchild1.setLeaf(true);
		grandchild1.setLevel("2");
		ChildEntity grandchild2 = new ChildEntity();
		grandchild2.setCode("C00012");
		grandchild2.setName("grandchild1");
		grandchild2.setLeaf(true);
		grandchild2.setLevel("2");
		ChildEntity grandchild3 = new ChildEntity();
		grandchild3.setCode("C00021");
		grandchild3.setName("grandchild3");
		grandchild3.setLeaf(true);
		grandchild3.setLevel("2");
		
		grandChildren.add(grandchild1);
		grandChildren.add(grandchild2);
		
		grandChildren2.add(grandchild3);
		
		children.add(child1);
		children.add(child2);
		children.add(child3);
		
		entity.setCode("C00000");
		entity.setName("parent");
		entity.setLeaf(false);
		entity.setLevel("0");
		entity.setChildren(children);

		
		List<Root> roots = new ArrayList<Root>();
		Root r1 = new Root();
		r1.setCode("C23423");
		r1.setName("Bird");
		Root r2 = new Root();
		r2.setCode("C8988");
		r2.setName("dog");
		Root r3 = new Root();
		r3.setCode("C534");
		r3.setName("Cat");
		roots.add(r1);
		roots.add(r2);
		roots.add(r3);
		entity.setParents(roots);
		//when(baseservice.getRestParents(Mockito.anyString())).thenReturn(roots);
		service.setService(baseservice);
		service.resolveChildEntityGraph( entity, list);
		assertEquals(7, list.size());
		list.stream()
		.filter(x -> x.getCode().equals("C00000"))
		.findAny()
		.get()
		.getParents()
		.stream()
		.filter(z -> z.getCode().equals("C23423"));
		list.stream()
		.filter(x -> x.getCode().equals("C00000"))
		.findAny()
		.get()
		.getParents()
		.stream()
		.filter(z -> z.getCode().equals("C534"));
		list.stream()
		.filter(x -> x.getCode().equals("C00000"))
		.findAny()
		.get()
		.getParents()
		.stream()
		.filter(z -> z.getName().equals("dog"));
	}


}
