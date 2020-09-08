// initially used to set path for deploying to tomcat
//https://medium.com/@escuela.tech/deploy-vuejs-application-in-tomcat-b0010c2f8395


// NODE_ENV is pulled in from .env.development or .env.production file
// based on if the build mode (production/development).  The build command
// is specified in package.json.
module.exports = {
 publicPath: process.env.NODE_ENV === 'production'
  ? '/reportexporter/'
  : '/'
}
