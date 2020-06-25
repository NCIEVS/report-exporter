package gov.nih.nci.evs.report.exporter.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommonServices {
	
	public static final String TOP_NODE = "TOP_NODE";
	
	public static <T> String getListValues(List<T> list) {
		return list != null?"\"" + getListValuesWithPipeDelimiter(list) + "|\"": null;
	}
	
	public static <T> String getListValuesWithPipeDelimiter(List<T> list) {
		return list.stream().map(x -> x.toString()).reduce("", (part, whole)-> part + "|" + whole);
	}
	
	public static String cleanListOutPut(String list){
		if (list == null)  return null;
		return list.replace("[", "").replace("]", "");
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

}
