package gov.nih.nci.evs.report.exporter.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gov.nih.nci.evs.report.exporter.model.Association;
import gov.nih.nci.evs.report.exporter.model.Format;
import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyPrime;
import gov.nih.nci.evs.report.exporter.model.Rel;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.model.TypeListAndPositionTuple;

public class CommonServices {
	
	
	public static final String TOP_NODE = "TOP_NODE";
	public static final String PREFERRED_NAME = "Preferred_Name";
	public static final String SYNONYMS = "synonyms";
	public static final String DEFINITIONS = "definitions";
	public static final String MAPS = "Maps_To";
	private boolean noDefinitions = false;
	private boolean noSynonyms = false;
	private boolean noMaps= false;
	
	public enum Formats{JSON,CSV,TABD,EXCEL};
	
	public enum ResType{ENTITY,ROLE,ASSOC};
	
	public static Format[] formats = new Format[]
			{new Format(Formats.JSON.name(), "JavaScript Object Notation Format", "json" ),
			 new Format(Formats.CSV.name(), "Comma Separated Value Format", "csv" ),
			 new Format(Formats.TABD.name(), "Tab Delimited Value Format", "txt" ),
			 new Format(Formats.EXCEL.name(), "Microsoft Excel Format", "xlsx" )};
	
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
	
	public String calculateAndProduceSpacedRoles(Role role, String code, String name, String separator) {
		ArrayList<String> roleEle = new ArrayList<String>();
		roleEle.add(code);
		roleEle.add(name);
		roleEle.add(role.getType());
		roleEle.add(role.getRelatedCode());
		roleEle.add(role.getRelatedName());
		return roleEle.stream().collect(Collectors.joining(separator));
	}
	
	public String calculateAndProduceSpacedAssociations(Association assoc, String code, String name, String separator) {
		ArrayList<String> assocEle = new ArrayList<String>();
		assocEle.add(code);
		assocEle.add(name);
		assocEle.add(assoc.getType());
		assocEle.add(assoc.getRelatedCode());
		assocEle.add(assoc.getRelatedName());
		return assocEle.stream().collect(Collectors.joining(separator));
	}
	
	public String calculateAndProduceSpacedCSVAssociations(Association assoc, String code, String name, String separator) {
		ArrayList<String> assocEle = new ArrayList<String>();
		assocEle.add(code);
		assocEle.add(adjustTextForContainedComma(name));
		assocEle.add(adjustTextForContainedComma(assoc.getType()));
		assocEle.add(assoc.getRelatedCode());
		assocEle.add(adjustTextForContainedComma(assoc.getRelatedName()));
		return assocEle.stream().collect(Collectors.joining(separator));
	}
	
	public String adjustTextForContainedComma(String text) {
	 if(text.contains(",")) {
		 return "\"" + text + "\"";
	 }
	 else {return text;}
	}
	
	public Row calculateAndProduceSpacedXLSRoles(Row row, Role role, String code, String name, int internalIndex) {
		row.createCell(internalIndex++).setCellValue(code);
		row.createCell(internalIndex++).setCellValue(name);
		row.createCell(internalIndex++).setCellValue(role.getType());
		row.createCell(internalIndex++).setCellValue(role.getRelatedCode());
		row.createCell(internalIndex++).setCellValue(role.getRelatedName());
		return row;
	}
	
	public Row calculateAndProduceSpacedXLSAssociations(Row row, Association association, String code, String name, int internalIndex) {
		row.createCell(internalIndex++).setCellValue(code);
		row.createCell(internalIndex++).setCellValue(name);
		row.createCell(internalIndex++).setCellValue(association.getType());
		row.createCell(internalIndex++).setCellValue(association.getRelatedCode());
		row.createCell(internalIndex++).setCellValue(association.getRelatedName());
		return row;
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
		return list.stream().map(x -> removeAllNoSourceNoTypeSynonyms(x))
				.reduce("", (part, whole)-> 
				(whole == null 
				&& part == null
				)?
						(""):
							(part + (whole == null?"":"|" + whole)));
	}
	
	public List<String> filterHeadings(CommonServices services) {
		return Stream.of(FormatUtility.FIELDS)
		.filter(x -> x != getDefinitionHeaderForIndicator(services))
		.filter(x -> x != getSynonymHeaderForIndicator(services))
		.filter(x -> x != getMapHeaderForIndicator(services))
		.collect(Collectors.toList());
	}
	
	public List<String> getRoleHeadings() {
		return Stream.of(FormatUtility.ROLE_FIELDS)
		.collect(Collectors.toList());
	}
	
	public List<String> getAssocHeadings() {
		return Stream.of(FormatUtility.ASSOCIATION_FIELDS)
		.collect(Collectors.toList());
	}
	
	public String getDefinitionHeaderForIndicator(CommonServices services) {
		if(services.isNoDefinitions()) {return DEFINITIONS;} 
//		else if (flags.noEntitiesHaveDefs ) {return DEFINITIONS;}
		else
		{return null;}
	}
	
	public String getSynonymHeaderForIndicator(CommonServices services) {
		if(services.isNoSynonyms()) {return SYNONYMS;} 
//		else if(flags.noEntitiesHaveSyns) {return SYNONYMS;} 
		else
		{return null;}
	}
	
	public String getMapHeaderForIndicator(CommonServices services) {
		if(services.isNoMaps()) {return MAPS;} 
//		else if(flags.noEntitiesHaveMaps) {return MAPS;} 
		else
		{return null;}
	}
	
	public static <T> String removeAllNoSourceNoTypeSynonyms(Object t) {
		if(t instanceof Synonym) {
			if(((Synonym) t).getType() == null) {return t.toString();}
			if(((Synonym) t).getType().equals(PREFERRED_NAME)) {return null;}
		}
		return t.toString();		
	}
	
	public static String cleanListOutPut(String list){
		if (list == null)  return null;
		return list.replace("[", "").replace("]", "");
	}
	
	public String fullyCuratedProperties(List<? extends PropertyPrime> x,
			String separator, String propType) {
		String list = separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x));
		if(propType == SYNONYMS){
			if(isNoSynonyms()){ return "";}}
//			else if(!existsCheck(x)){
//				bools.noEntitiesHaveSyns = false; }}
        if(propType == DEFINITIONS) {
        	if(isNoDefinitions()){ return "";}}
//			else if(!existsCheck(x)){
//				bools.noEntitiesHaveDefs = false; }}
        if(propType == MAPS) {
        	if(isNoMaps()){ return "";}}
//			else if(!existsCheck(x)){
//				bools.noEntitiesHaveMaps = false; }}

		return list;
	}
	
	public void fullyCuratedPropertiesForExcel(List<? extends PropertyPrime> x, IndexWrapper index,
			String propType, Row row) {

		if(propType == SYNONYMS){
			if(isNoSynonyms()){ 
				return;}}
//			else if(!existsCheck(x)){
//				bools.noEntitiesHaveSyns = false; }}
        if(propType == DEFINITIONS) {
        	if(isNoDefinitions()){ 
        		return;}}
//			else if(!existsCheck(x)){
//				bools.noEntitiesHaveDefs = false; }}
        if(propType == MAPS) {
        	if(isNoMaps()){ 
        		return;}}
//			else if(!existsCheck(x)){
//				bools.noEntitiesHaveMaps = false; }}
        
		createCellInExcelRow(x, index.getIndex(), row);
		index.increment();
	}
	
	public void createCellInExcelRow(List<? extends PropertyPrime> x, int index, Row row) {
		Cell cell = row.createCell(index);
        cell.setCellValue(CommonServices.cleanListOutPut(CommonServices.getListValuesForExcel(x)));
	}
	
	public boolean existsCheck(List<? extends PropertyPrime> x) {
		return x == null || x.size() == 0;
	}
	
	
	public Iterator<TypeListAndPositionTuple> iterateOnPostion(ConcurrentMap<String, TypeListAndPositionTuple > map) {
		return map.values()
				.stream()
				.sorted(Comparator
				.comparing(TypeListAndPositionTuple::getPos))
				.iterator();
	}
	
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
	  
	 public static void saveOrUpdateWeightedRels(Rel role, Hashtable<String, Rel> wRoles) {
			Rel rStored = wRoles.get(role.getType());
			role.setWeight(1);
			if(rStored == null){wRoles.put(role.getType(), role);
			}else {rStored.setWeight(rStored.getWeight() + 1);
			}
		}
	 
		
		public static List<Rel> getSortedRels(List<? extends Rel> rels){
			return CommonServices.sortRelListByWeight(getDistinctWeightedRelsForEntityCodes(rels));
		}
		
		public static List<Rel> getDistinctWeightedRelsForEntityCodes(List<? extends Rel> rawRels){
			Hashtable<String,Rel> distinctRels = new Hashtable<String,Rel>();	
			rawRels.stream().forEach(x -> CommonServices.saveOrUpdateWeightedRels(x, distinctRels));
			return distinctRels.values().stream().collect(Collectors.toList());
		}
	 
		public static List<Rel> sortRelListByWeight(List<Rel> rels){
			Collections.sort(rels, new Comparator<Rel>() {            @Override
	            public int compare(Rel r1, Rel r2) {
	            return r2.getWeight() - r1.getWeight();
	        }});
			return rels;
		}
	  
	  
	 public boolean isNoDefinitions() {
		return noDefinitions;
	}
	public void setNoDefinitions(boolean noDefinitions) {
		this.noDefinitions = noDefinitions;
	}
	public boolean isNoSynonyms() {
		return noSynonyms;
	}
	public void setNoSynonyms(boolean noSynonyms) {
		this.noSynonyms = noSynonyms;
	}
	public boolean isNoMaps() {
		return noMaps;
	}
	public void setNoMaps(boolean noMaps) {
		this.noMaps = noMaps;
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
