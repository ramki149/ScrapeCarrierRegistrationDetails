pipeline {
	agent any
	stages {
		stage ('Compile Stage') {
			steps {
				withMaven(maven : 'apache-maven-3.6.1') {
					bat'mvn clean compile'
				}
			}
		}
		stage ('Testing Stage') {
			steps {
				withMaven(maven : 'apache-maven-3.6.1') {
					bat'mvn test'
				}
			}
		}
		stage ('Install Stage') {
			steps {
				withMaven(maven : 'apache-maven-3.6.1') {
					bat'mvn install'
				}
			}
		}
		stage ('Test Stage') {
			steps {
				withMaven(maven : 'apache-maven-3.6.1') {
					bat'copy "target\\cargointerview-0.0.1-SNAPSHOT.jar" .'
					bat'dir'
					bat'docker container prune --force'
					bat'docker image build -t jdk-image-from-dockerfile .'
					bat'docker container run -d -p 8081:8081 jdk-image-from-dockerfile'
				}
			}
		}
	}
}