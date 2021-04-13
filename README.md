# report-exporter
Export reports from the NCI Thesaurus.  The Report Exporter utilizes the EVS REST API for querying its data.
## Development Setup
### Report Exporter UI Setup
The UI is developed using [Vue.js](https://vuejs.org/). 
Vue.js can be installed with NPM.  NPM is available through [Node.js](https://nodejs.org/en/).

Once Vue.js and NMP are installed, the Report Exporter UI can be build and run the client in dev mode.  To do this, go to the **report-exporter/clientui** directory.

To build the UI, run the following command:

    npm run build
    
To run the UI, run the following command:

    npm run serve


### Report Exporter Service Setup
## Deployment Setup - Tomcat
The Report Exporter Application has been built and tested on a Tomcat 8 webserver.  Our instructions will focus on deploying to a Tomcat server.

### Report Exporter Build

At the root of the git project directory, run the Maven command that builds both the client and the server and encapsulates them in on war file.

**Maven builds for their respective tier**
* mvn clean install -DBuild_Env=build-dev
* mvn clean install -DBuild_Env=build-qa
* mvn clean install -DBuild_Env=build-stage
* mvn clean install -DBuild_Env=build-prod 

Add the war file from the Maven build to Tomcat webapps directory. The war file will be located under **report-exporter/service/target/**

#### Configure Tomcat Server
You will need to create a file called **Tomcat8/conf/Catalina/localhost/rewrite.config** and add the following contents in it.

    #-------------------------------------------------------------------------------
    # site that has example of this.
    # https://stackoverflow.com/questions/34619751/tomcat-8-url-rewrite-issues
    #-------------------------------------------------------------------------------
    #-------------------------------------------------------------------------------
    # Any subsequent file that the index.html file needs, needs to pass through.
    # This would be the js and css files along with other miscellaneous ones.
    #-------------------------------------------------------------------------------
    RewriteCond %{REQUEST_URI} .*\.(css|map|js|html|png|jpg|jpeg|gif|txt|ttf|json|woff|ico)$ [OR]

    #----------------------------------------
    # Allow these service calls to go through
    #----------------------------------------
    RewriteCond %{REQUEST_URI} ^(/reportexporter/download).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/properties).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/codereadrest).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/roots).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/curated).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/resolve\-).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/survey).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/sortedroles).*$ [OR]
    RewriteCond %{REQUEST_URI} ^(/reportexporter/sortedassociations).*$ [OR]

    RewriteRule ^(.*)$ - [L]
    RewriteRule ^(.*)$ /reportexporter/index.html


Update the **Tomcat8/conf/server.xml** file for Tomcat. Add the following **"RewriteValve"** entry to the Host section:

    <Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true">

          <Valve className="org.apache.catalina.valves.rewrite.RewriteValve" />
    </Host>
    
#### Content Security Policy (CSP) 

The following CSP settings were set to satisfy some app scan security issues.
 
On the apache server, add the following section to 
/local/content/apache/conf/vhosts-ssl/reportexporter.conf

	<IfModule headers_module>
	RequestHeader set X-HTTPS 1
		Header set Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline' www.googletagmanager.com www.google-analytics.com ajax.googleapis.com; connect-src 'self' www.google-analytics.com; img-src 'self' data:; style-src 'self' 'unsafe-inline'; base-uri 'self'; form-action 'self'"
	</IfModule>

#### Application Properties
    
Tier related settings must be adjusted using the application properties files specific to each tier.  Since there is a dependency upon the EVS REST API these settings would be specific EVS REST API instances on each tier.  So for the production tier the application-prod.properties file would contain URLs for both meta and concept resources.  The tomcat server would be started using a setenv.sh file with the prod version of one of the following additions to the CATALINA_OPTS environmental variable configuration:
 
    -Dspring.profiles.active=dev 
    -Dspring.profiles.active=qa 
    -Dspring.profiles.active=stage
    -Dspring.profiles.active=prod 
 
 Start the Tomcat server
 
 Open a browser and go to  http://localhost:8080/reportexporter
