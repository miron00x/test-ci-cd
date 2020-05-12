pipeline {
   agent any

   stages {
      stage('Hello') {
         steps {
            echo 'Hello World'
         }
      }

      stage('Build') {
         steps {
            echo '--------------Buid-----------'
		sh "mvn -f /sa-webapp/pom.xml clean package"
         }
      }
   }
}
