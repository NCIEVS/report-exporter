package gov.nih.nci.evs.report.exporter.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gov.nih.nci.evs.report.exporter.model.Property;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.util.CSVUtility;
import gov.nih.nci.evs.report.exporter.util.ExcelUtility;
import gov.nih.nci.evs.report.exporter.util.TabDelUtility;

@Service
public class CodeReadService {
	
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		builder.additionalMessageConverters(messageConverters);
		return builder.build();
	}
	
	public List<RestEntity> getRestProperties(List<String> codes){
		List<RestEntity> propMeta = 
				codes.stream().map(code -> getRestTemplate(new RestTemplateBuilder())
				.getForObject(
				"https://api-evsrest-dev.nci.nih.gov/api/v1/concept/ncit/" + code + "?include=summary"
						, RestEntity.class)).collect(Collectors.toList());
		return propMeta;
	}
	
	public List<String> getCodes(String codes){
		if(codes == null) {
			System.out.println("Input for code, property, or source cannot be null");
			return null;
		}
		return Arrays.asList(codes.split(","));
	}
	
	public List<RestEntity> getEntitiesForPropertyNameFilter
	(List<RestEntity> list, List<String> propList){
		list.stream().forEach(
				entity -> entity.setProperties(
						filterProperties(entity.getProperties(), propList)));
		return list;
	}
	
	public InputStream getJsonBytesForRestParams(String codes, String props) {
		return new ByteArrayInputStream(
				getGsonForPrettyPrint().toJson(
						getEntitiesForPropertyNameFilter(
								getRestProperties( 
			getCodes(codes)), getCodes(props))).getBytes());
	}
	
	public InputStream getCSVBytesForRestParams(String codes, String props) {

						return new ByteArrayInputStream(new CSVUtility().produceCSVOutputFromListWithHeading(getEntitiesForPropertyNameFilter(
								getRestProperties( 
										getCodes(codes)), getCodes(props))).getBytes());

	}
	
	public InputStream getTabDelBytesForRestParams(String codes, String props) {

		return new ByteArrayInputStream(new TabDelUtility().produceTabDelOutputFromListWithHeading(getEntitiesForPropertyNameFilter(
				getRestProperties( 
						getCodes(codes)), getCodes(props))).getBytes());

}
	
	public ByteArrayInputStream getXSLBytesForRestParams(String codes, String props) {

		try {
			return new ByteArrayInputStream(new ExcelUtility().produceExcelOutputFromListWithHeading(getEntitiesForPropertyNameFilter(
					getRestProperties( 
							getCodes(codes)), getCodes(props))).toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

}
	public List<Property> filterProperties(List<Property> propList, List<String> list){
		return propList.stream().filter(
				x -> list.stream().anyMatch(y -> x.getType().equals(y)))
				.collect(Collectors.toList());
	}
	
	public Gson getGsonForPrettyPrint() {
		return new GsonBuilder().setPrettyPrinting().create();
	}
	
	public static void main(String ...args) {
		new CodeReadService().getCSVBytesForRestParams("C12434","Semantic_Type");
	}

}
