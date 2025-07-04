package gov.nih.nci.evs.report.exporter.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import gov.nih.nci.evs.report.exporter.model.Association;
import gov.nih.nci.evs.report.exporter.model.ChildEntity;
import gov.nih.nci.evs.report.exporter.model.RestEntity;
import gov.nih.nci.evs.report.exporter.model.RestPropertyMetadata;
import gov.nih.nci.evs.report.exporter.model.Role;
import gov.nih.nci.evs.report.exporter.model.Root;
import gov.nih.nci.evs.report.exporter.util.CommonServices;

@Service
public class EVSAPIBaseService {
	
	private Logger log = LoggerFactory.getLogger(TimedDeferredResultWrapper.class);
	
    @Value("${NODE_LIST}")
	private String curatedTopNodeList;
    
    @Value("${evs.api.url.baseurl}")
    private String baseURL;
    
    @Value("${evs.api.url.metadataurl}")
    private String baseMetaURL;
    
    @Value("${CHILDREN}")
    private String children;
    
    @Value("${DESCENDANTS}")
    private String descendants;
    
	@Value("${SUMMARY}")
	private String summary;
	
	@Value("${MAPS}")
	private String maps;
	
	@Value("${PARENTS}")
	private String parents;
	
	@Value("${ROLES}")
	private String roles;
	
	@Value("${PARENTS_PARAM}")
	private String parentsParam;
	
	@Value("${REST_PROP_URL}")
	private String propURL;
	
	@Value("${REST_SYN_URL}")
	private String synURL;
	
	@Value("${REST_DEF_URL}")
	private String defURL;
	
	@Value("${REST_PROP_FILTER_LIST}")
	private String filterList;
	
	@Value("${REST_ROOT_FILTER_LIST:C28428}")
	private String rootFilterList;

	@Value("${evs.mail.list}")
	private String emailList;
	
	@Value("${SMTP_SERVER}")
	private String smtpServer;
	
	@Value("${DEFAULT_FROM_EMAIL}")
	private String defaultEmail;

	@Value("${ASSOCIATIONS}")
	private String associations;
	
	
    
	public List<ChildEntity> getChildrenForBranchTopNode(List<String> codes){
		return 
				codes.stream().map(code -> CommonServices.getRestTemplate()
				.getForObject(
				baseURL  + code + children
						,ChildEntity[].class)).flatMap(Arrays::stream)
						.collect(Collectors.toList());
	}
	
	public List<ChildEntity> getUnprocessedChildrenForBranchTopNode(String code, String max){
		return 
				Arrays.asList(CommonServices.getRestTemplate()
				.getForObject(
				baseURL 
				+ code 
				+ descendants + max
						,ChildEntity[].class));
	}
	
	public List<Root> getRestParents(String code){
		List<Root> roots = Stream.of(WebClient
				.create()
				.get()
				.uri(baseURL + code + parents)
				.retrieve().bodyToMono(Root[].class)
				.block()).collect(Collectors.toList());			
		return roots;
	}
	
	public RestEntity getEntity(String code) throws URISyntaxException {	
		WebClient client = getNewWebClientWithBuffer();
			return client
					.get()
					.uri(new URI(baseURL + code + summary + "," + maps + "," + parentsParam))
					.retrieve()
					.bodyToMono(RestEntity.class)
					.block();
	}
	
	public RestEntity[] getEntities(String codes) {	
		
		WebClient client = getNewWebClientWithBuffer();
		try {
			return client
					.get()
					.uri(new URI(baseURL.replaceAll("/$", "") + summary + "," + maps + "," + parentsParam + "&list=" + codes))
					.retrieve()
					.bodyToMono(RestEntity[].class)
					.block();
		} catch (URISyntaxException e) {
			log.info("Bad Resource Request, check the URL for special characters: ", e);
			return null;
		}
	}
	
	public List<Role> getRestRole(String code) {	
		WebClient client = getNewWebClientWithBuffer();
		return Stream.of(client
				.get()
				.uri(baseURL + code + roles)
				.retrieve()
				.bodyToMono(Role[].class)
				.block()).collect(Collectors.toList());			
	}
	
	public List<Association> getRestAssociation(String code) {	
		WebClient client = getNewWebClientWithBuffer();
		return Stream.of(client
				.get()
				.uri(baseURL + code + associations)
				.retrieve()
				.bodyToMono(Association[].class)
				.block()).collect(Collectors.toList());			
	}
	
	public Root[] getRestRoots(RestTemplate template){
		Root[] roots = 
				 template
		.getForObject(
		 baseURL + "/roots"
				,Root[].class);
		return roots;
	}
	
	public RestPropertyMetadata[] getRestProperties(RestTemplate template){
		return template
		.getForObject(
		 baseMetaURL + propURL
				,RestPropertyMetadata[].class);

	}
	
	public RestPropertyMetadata[] getRestSynonyms(RestTemplate template){
		return template
		.getForObject(
		 baseMetaURL + synURL
				,RestPropertyMetadata[].class);

	}
	
	public RestPropertyMetadata[] getRestDefinitions(RestTemplate template){
		return template
		.getForObject(
		 baseMetaURL + defURL
				,RestPropertyMetadata[].class);

	}

	public String getCuratedTopNodeList() {
		return curatedTopNodeList;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public String getChildren() {
		return children;
	}

	public String getDescendants() {
		return descendants;
	}

	public String getSummary() {
		return summary;
	}

	public String getMaps() {
		return maps;
	}

	public String getParents() {
		return parents;
	}

	public String getPropURL() {
		return propURL;
	}

	public String getFilterList() {
		return filterList;
	}

	public String getRootFilterList() {
		return rootFilterList;
	}
	
	public String getEmailList() {
		return emailList;
	}

	public void setEmailList(String emailList) {
		this.emailList = emailList;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}


	public String getDefaultEmail() {
		return defaultEmail;
	}

	public void setDefaultEmail(String defaultEmail) {
		this.defaultEmail = defaultEmail;
	}

	public WebClient getNewWebClientWithBuffer() {
		
		return WebClient.builder().
		  exchangeStrategies(ExchangeStrategies.builder()
				    .codecs(configurer -> configurer
				      .defaultCodecs()
				      .maxInMemorySize(76 * 256 * 1024))
				    .build())
				  .build();
	}

}
