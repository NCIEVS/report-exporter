package gov.nih.nci.evs.report.exporter.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.PropertyPrime;
import gov.nih.nci.evs.report.exporter.model.Synonym;
import gov.nih.nci.evs.report.exporter.model.TypeListAndPositionTuple;

public class CommonServices {
	
	
	public static final String TOP_NODE = "TOP_NODE";
	public static final String PREFERRED_NAME = "Preferred_Name";
	private boolean noDefinitions = false;
	private boolean noSynonyms = false;
	private boolean noMaps= false;
	
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
		return list.stream().map(x -> removeAllNoSourceNoTypeSynonyms(x))
				.reduce("", (part, whole)-> 
				(whole == null)?
						(""):
							(part + "|" + whole));
	}
	
	public List<String> filterHeadings(CommonServices services) {
		return Stream.of(FormatUtility.FIELDS)
		.filter(x -> x != getDefinitionHeaderForIndicator(services))
		.filter(x -> x != getSynonymHeaderForIndicator(services))
		.filter(x -> x != getMapHeaderForIndicator(services))
		.collect(Collectors.toList());
	}
	
	public String getDefinitionHeaderForIndicator(CommonServices services) {
		if(services.isNoDefinitions()) {return "definitions";}
		return null;
	}
	
	public String getSynonymHeaderForIndicator(CommonServices services) {
		if(services.isNoSynonyms()) {return "synonyms";}
		return null;
	}
	
	public String getMapHeaderForIndicator(CommonServices services) {
		if(services.isNoMaps()) {return "Maps_To";}
		return null;
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
			String separator, String propType, TripleBoolean bools) {
		if(propType == "synonyms"){
			if(isNoSynonyms()){ return "";}
			else if(existsCheck(x)){
				bools.noEntitiesHaveSyns = true; 
				 return "";}}
        if(propType == "definitions") {
        	if(isNoDefinitions()){ return "";}
			else if(existsCheck(x)){
				bools.noEntitiesHaveDefs = true; 
				 return "";}}
        if(propType == "Maps_To") {
        	if(isNoMaps()){ return "";}
			else if(existsCheck(x)){
				bools.noEntitiesHaveMaps = true; 
				 return "";}}

		return separator + CommonServices.cleanListOutPut(CommonServices.getListValues(x));
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
	
	public String cleanColumns(TripleBoolean flags, StringBuffer oneLine, String separator) {

		//All user valuers were entered so all columns are removed. Nothing
		//to do here.
		if(isNoSynonyms() && isNoDefinitions() && isNoMaps()) {return oneLine.toString();}
		
		String[] temp = oneLine.toString().split("/r/n");
		String result = Stream.of(temp)
				.map(x -> cleanColumn(x, flags, separator) + "/r/n")
				.collect(Collectors.joining());
		return result;
	}
	
	public String cleanColumn(String line, TripleBoolean flags, String separator) {
		List<String> listPrime = new ArrayList<String>(Arrays.asList(line.split(",")));


		//user flag no synonyms
		if(isNoSynonyms()) {

			//Condition user flag no synonyms and no definitions
			if(isNoDefinitions()) {

				if(//User flags for removing synonyms and definitions set
						//definitions and synonyms already removed -- do nothing
						!flags.noEntitiesHaveMaps) 
				{return line;}
				if(//None of the entities have maps -- remove the mapping column 
						//where the Synonyms used to be
						flags.noEntitiesHaveMaps) 
				{listPrime.remove(5);}
			}else if(isNoMaps()) {

				if(//User flags for removing synonyms and maps set
						//synonyms and maps already removed one or 
						//more definitions exist -- do nothing
						!flags.noEntitiesHaveDefs) 
				{return line;}
				if(//None of the entities have definitions -- remove 
						//the mapping column where the synonyms used to be
						flags.noEntitiesHaveDefs) 
				{listPrime.remove(5);}

			}else {

				//No other user flags are set
				if (//Synonym column is already removed
						!flags.noEntitiesHaveDefs && 
						!flags.noEntitiesHaveMaps) {
					//We have values here for both cols -- don't change anything
					return line;
				}
				if(//Synonym column is already removed -- but no definitions
						// exist so remove the column where synonyms usually are
						flags.noEntitiesHaveDefs && 
						!flags.noEntitiesHaveMaps) { 
					listPrime.remove(5);}
				if(//Synonym column is already removed -- but no definitions
						// exist so remove the column where definitions usually are
						!flags.noEntitiesHaveDefs && 
						flags.noEntitiesHaveMaps) { 
					listPrime.remove(6);}

			}

		}	

		//User flag set for at least no Definitions
		if(isNoDefinitions()) {

			//Condition user flag no definitions and no synonyms
			if(isNoSynonyms()) {

				if(//User flags for removing synonyms and defintionss set
						//synonyms and definitions already removed -- do nothing
						!flags.noEntitiesHaveMaps) 
							{return line;}
				if(//None of the entities have maps -- remove the mapping column 
						//where the Synonyms used to be
						flags.noEntitiesHaveMaps) 
							{listPrime.remove(5);}
			} else
				//Condition user flag no definitions and no maps
				if(isNoMaps()) {

					if(//User flags for removing definitions and maps set
							//definitions and maps already removed -- do nothing
							!flags.noEntitiesHaveSyns) 
							{return line;}
					if(//None of the entities have synonyms -- remove the mapping column 
							//where the Synonyms used to be
							flags.noEntitiesHaveSyns) 
							{listPrime.remove(5);}
				}else {

					if (//Definition column is already removed
							!flags.noEntitiesHaveSyns && 
							!flags.noEntitiesHaveMaps) {
								//We have values here for synonyms and maps
								//don't change anything
								return line;
					}
					if(//Definition column is already removed -- but no synonyms
							// exist so remove the column where synonyms usually are
							flags.noEntitiesHaveSyns && 
							!flags.noEntitiesHaveMaps) { 
								listPrime.remove(5);}
					if(//Definitions column is already removed -- but no maps
							// exist so remove the column where definitions usually are
							!flags.noEntitiesHaveSyns && 
							flags.noEntitiesHaveMaps) { 
								listPrime.remove(6);}
				}
		}

		//user flag for no Maps
		if(isNoMaps()) {

			//Condition user flags no maps and no synonyms
			if(isNoSynonyms()) {

				if(//User flags for removing synonyms and maps set
						//synonyms and maps already removed -- do nothing
						!flags.noEntitiesHaveDefs) 
							{return line;}
				if(//None of the entities have definitions -- remove the definitions column 
						//by deleting where the synonyms used to be
						flags.noEntitiesHaveDefs) 
							{listPrime.remove(5);}

			}else
				//Condition user flag no definitions and no maps
				if(isNoDefinitions()) {

					if(//User flags for removing definitions and maps set
							//definitions andmaps already removed -- do nothing
							!flags.noEntitiesHaveSyns) 
					{return line;}
					if(//None of the entities have synonyms  -- remove the mapping column 
							//where the Synonyms used to be
							flags.noEntitiesHaveSyns) 
					{listPrime.remove(5);}
				}else {

					if (//Maps column is already removed
							!flags.noEntitiesHaveSyns && 
							!flags.noEntitiesHaveDefs) {
						//We have values here for synonyms and maps -- don't change anything
						return line;
					}
					if(//Maps column is already removed -- but no synonyms
							// exist so remove the column where synonyms usually are
							flags.noEntitiesHaveSyns && 
							!flags.noEntitiesHaveDefs) { 
						listPrime.remove(5);}
					if(//maps column is already removed -- but no mapss
							// exist so remove the column where synonyms usually are
							!flags.noEntitiesHaveSyns && 
							flags.noEntitiesHaveDefs) { 
						listPrime.remove(6);}

				}
		}
		


		if(!isNoSynonyms() && 
				!isNoDefinitions() && !isNoMaps()) {
		
		if(flags.noEntitiesHaveSyns && 
				!flags.noEntitiesHaveDefs && 
				!flags.noEntitiesHaveMaps) { 
			listPrime.remove(5);}
		if(!flags.noEntitiesHaveSyns && 
				flags.noEntitiesHaveDefs && 
				!flags.noEntitiesHaveMaps) { 
					listPrime.remove(6);}
		if(!flags.noEntitiesHaveSyns && 
				!flags.noEntitiesHaveDefs && 
				 flags.noEntitiesHaveMaps) 
					{listPrime.remove(7);}
		if(!flags.noEntitiesHaveSyns && 
				flags.noEntitiesHaveDefs && 
				flags.noEntitiesHaveMaps) 
					{listPrime.remove(6);
					listPrime.remove(6);}
		if( flags.noEntitiesHaveSyns && 
				!flags.noEntitiesHaveDefs && 
				flags.noEntitiesHaveMaps) 
					{listPrime.remove(5);
					listPrime.remove(6);}
		if( flags.noEntitiesHaveSyns && 
				flags.noEntitiesHaveDefs && 
				 !flags.noEntitiesHaveMaps) 
					{listPrime.remove(5);
					listPrime.remove(5);}
		if(flags.noEntitiesHaveSyns && 
				flags.noEntitiesHaveDefs && 
				flags.noEntitiesHaveMaps)
					{listPrime.remove(5);
					listPrime.remove(5);
					listPrime.remove(5);}
		if( !flags.noEntitiesHaveSyns && 
				!flags.noEntitiesHaveDefs && 
				!flags.noEntitiesHaveMaps){
					return line;}
	}
		
		return listPrime.stream().collect(Collectors.joining(separator));

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
