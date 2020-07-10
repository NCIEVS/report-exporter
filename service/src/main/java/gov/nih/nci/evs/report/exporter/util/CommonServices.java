package gov.nih.nci.evs.report.exporter.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.TypeListAndPositionTuple;

public class CommonServices {
	
	
	public static final String TOP_NODE = "TOP_NODE";
	
	private ConcurrentMap<String, TypeListAndPositionTuple> propHeaderMap;
	public CommonServices() {
		propHeaderMap = new ConcurrentHashMap<String, TypeListAndPositionTuple>();
	}
	public ConcurrentMap<String, TypeListAndPositionTuple> getPropHeaderMap() {
		return propHeaderMap;
	}

	public void setPropHeaderMap(ConcurrentMap<String, TypeListAndPositionTuple> propHeaderMap) {
		this.propHeaderMap = propHeaderMap;
	}
	
	public static <T> String getListValues(List<T> list) {
		return list != null && list.size() > 0?"\"" + getListValuesWithPipeDelimiter(list) + "|\"": "";
	}
	
	public static <T> String getListValuesForExcel(List<T> list) {
		return list != null && list.size() > 0?getListValuesWithPipeDelimiter(list) + "|": "";
	}
	
	public String getSortedIndexedPropertyListValues(
			List<Property> list, 
			String separator, 
			HashMap<String, List<Property>> propMap) {
		List<Property> sorted =list
				.stream()
				.sorted(Comparator
						.comparing(Property::getType))
						.collect(Collectors.toList());
		sorted.stream().forEach(x -> {addPropertyTypeAndPositionToCache(x);} );
		return null;
	}
	
	public TypeListAndPositionTuple addPropertyTypeAndPositionToCache(Property prop) {
		TypeListAndPositionTuple tuple = propHeaderMap.get(prop.getType());
		if(tuple != null) {tuple.getProperties().add(prop); return tuple; }
		else {
		int pos = propHeaderMap.keySet().size();
		List<Property> props = new ArrayList<Property>();
		props.add(prop);
		return propHeaderMap.put(prop.getType(), new TypeListAndPositionTuple(pos,prop.getType(),props));
		}
	}
	
	public String calculateAndProduceSpacedTerms(String separator) {
		return flattenListValues(
				getOrderedPropertyLists(propHeaderMap), separator);
	}
	
	public void clearPropertyListsFromHeaderMap() {
		getPropHeaderMap()
		.keySet()
		.forEach(x -> getPropHeaderMap()
				.get(x)
				.setProperties(new ArrayList<Property>()));
	}
	
	public Integer getPropertyPositionFromCache(String value) {
		return propHeaderMap.get(value).getPos();
	}
	
	public String flattenListValues(List<List<Property>> list, String separator) {
		return list
				.stream()
				.map(e -> getListValues(e))
				.reduce("", (part, whole)-> part + separator + whole)
				.replaceFirst(separator, "");
	}
	
	public int flattenListValuesIntoRowCells(List<List<Property>> list, Row row, int index) {	
		for(List<Property> prop: list) {
		row.createCell(index).setCellValue(getListValuesForExcel(prop)); index++;
		}
		return index;
	}
	
	public static <T> String getListValuesWithPipeDelimiter(List<T> list) {
		if(list == null || list.size() == 0) {return "";}
		return list.stream().map(x -> x.toString()).reduce("", (part, whole)-> part + "|" + whole);
	}
	
	public static String cleanListOutPut(String list){
		if (list == null)  return null;
		return list.replace("[", "").replace("]", "");
	}
	
	public Iterator<TypeListAndPositionTuple> iterateOnPostion(ConcurrentMap<String, TypeListAndPositionTuple > map) {
		return map.values()
				.stream()
				.sorted(Comparator
				.comparing(TypeListAndPositionTuple::getPos))
				.iterator();
	}
	
	//Start here Monday
	public List<List<Property>>  getOrderedPropertyLists(ConcurrentMap<String, TypeListAndPositionTuple > map) {
		return map.values()
				.stream()
				.sorted(Comparator
				.comparing(TypeListAndPositionTuple::getPos))
				.map(x -> validateAndOrCreateEmptyPropertyList(x))
						.collect(Collectors.toList());
	}
	
	public List<Property> validateAndOrCreateEmptyPropertyList(TypeListAndPositionTuple tuple){
		if(tuple.getPos() == null) {return new ArrayList<Property>();}
		else {return tuple.getProperties();}
	}
	
	public List<String> getHeadersByPosition(ConcurrentMap<String, TypeListAndPositionTuple> map) {
		return map.values()
				.stream()
				.sorted(Comparator
				.comparing(TypeListAndPositionTuple::getPos))
				.map(x -> x.getType())
				.collect(Collectors.toList());
	}
	
	public static List<String> splitInput(String codes){
		if(codes == null) {
			System.out.println("Input for code, property, or source cannot be null");
			return null;
		}
		return Arrays.asList(codes.split(","));
	}
	
	public static Gson getGsonForPrettyPrint() {
		return new GsonBuilder().setPrettyPrinting().create();
	}
	
	public static RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		builder.additionalMessageConverters(messageConverters);
		return builder.build();
	}
	
	public static RestTemplate getRestTemplate() {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		builder.additionalMessageConverters(messageConverters);
		return builder.build();
	}
	
	  public static List<String> getCodesListForCode(String code){
		  List<String> list = new ArrayList<String>();
		  list.add(code);
		  return list;
	  }
	  
	  public static boolean isChildParent(String parent, String code) {
		  if(parent == null || parent.equals(CommonServices.TOP_NODE)) return false;
		  String parentCode = parent.substring(0, parent.indexOf(':'));
		  return parentCode.equals(code);
	 }
	  
	  public int setPropertyRowOutPut(List<Property> properties, Row row, int index) {

			return this.flattenListValuesIntoRowCells(getOrderedPropertyLists(propHeaderMap), row, index);
	  }
	  
	 public static void main(String ...strings) {
		 
		 Property prop = new Property();
		 Property prop1 = new Property();
		 Property prop2 = new Property();
		 
		 prop.setType("oneType");
		 prop.setValue("prop");
		 prop1.setType("oneType");
		 prop1.setValue("prop1");
		 prop2.setType("notherType");
		 prop2.setValue("prop2");
		 List<Property> props = new ArrayList<Property>();
		 props.add(prop);
		 props.add(prop1);
		 props.add(prop2);
		 
	 }

}
