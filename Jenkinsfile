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
		mvn --version
         }
      }
   }
}
