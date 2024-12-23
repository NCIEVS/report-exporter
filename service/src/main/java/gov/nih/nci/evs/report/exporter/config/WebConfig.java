package gov.nih.nci.evs.report.exporter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import gov.nih.nci.evs.report.exporter.service.AssociationService;
import gov.nih.nci.evs.report.exporter.service.CodeReadService;
import gov.nih.nci.evs.report.exporter.service.RoleService;


@Configuration
@ComponentScan
public class WebConfig{


	
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
    
    @Bean
    public CodeReadService crservice() {
    	return new CodeReadService();
    }
    
    @Bean
    public AssociationService associationService() {
    	return new AssociationService();
    }
    
    @Bean RoleService roleService() {
    	return new RoleService();
    }
}