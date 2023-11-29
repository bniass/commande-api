def COLOR_MAP = [
    'SUCCESS': 'good',
    'FAILURE': 'danger',
]
pipeline{
	agent any
	tools {
		maven "MAVEN3"
		jdk "ORACLE_JDK17"
	}
	environment {
        DOCKER_IMAGE_NAME = "commande-api-app"
        SPRING_BOOT_APP_JAR = "target/commande-api-0.0.1-SNAPSHOT.jarr"
        DOCKER_HUB_CREDENTIALS = 'dockerhub'
        //REGISTRY = "bniass/commande-api"
    }
	stages {
		stage ('Fetch code') {
			steps {
				git branch: 'main', url: 'https://github.com/bniass/commande-api.git'
			}
		}
		stage('Build'){
			steps {
				sh 'mvn install -DskipsTest'
			}
			post {
				success {
					echo 'Archiving artefacts now'
					archiveArtifacts artifacts: '**/*'
				}
			}
		}
		stage('Unit Test') {
			steps {
				sh 'mvn test'
			}
		}
		stage('Checkstyle Analysis') {
		    steps {
                sh 'mvn checkstyle:checkstyle'
            }
		}
		stage('Sonar Analysis') {
            environment {
                scannerHome = tool 'sonar4.7'
            }
            steps {
               withSonarQubeEnv('server') {
                   sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=commande-api \
                   -Dsonar.projectName=commande-api \
                   -Dsonar.projectVersion=1.0 \
                   -Dsonar.sources=src/ \
                   -Dsonar.java.binaries=target/test-classes/com/ecole221/commandeapi/ \
                   -Dsonar.junit.reportsPath=target/surefire-reports/ \
                   -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                   -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
              }
            }
        }
        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    // Parameter indicates whether to set pipeline to UNSTABLE if Quality Gate fails
                    // true = set pipeline to UNSTABLE, false = don't
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage("UploadArtifact") {
            steps{
                nexusArtifactUploader(
                  nexusVersion: 'nexus3',
                  protocol: 'http',
                  nexusUrl: 'localhost:8082',
                  groupId: 'QA',
                  version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                  repository: 'my-micro-services-repo',
                  credentialsId: 'nexus-login',
                  artifacts: [
                    [artifactId: 'commande-api', classifier: '', file: 'target/commande-api-0.0.1-SNAPSHOT.jar', type: 'jar']
                  ]
               )
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    //dockerImage = docker.build REGISTRY + ":$BUILD_NUMBER"
                    withDockerRegistry([ credentialsId: DOCKER_HUB_CREDENTIALS, url: "" ]) {
                        // Build a Docker image for your Spring Boot application
                        docker.build("bniass/${DOCKER_IMAGE_NAME}:${BUILD_NUMBER}", "-f Dockerfile .")

                        //bat "docker push bniass/${DOCKER_IMAGE_NAME}:${BUILD_NUMBER}"
                        // Tag the image as 'latest'
                        docker.image("bniass/${DOCKER_IMAGE_NAME}:${BUILD_NUMBER}").push("latest")
                    }

                }
            }
        }
	}
	post {
        always {
            echo 'Slack Notifications.'
            slackSend channel: '#développement-logiciel',
                color: COLOR_MAP[currentBuild.currentResult],
                message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER} \n More info at: ${env.BUILD_URL}"
        }
    }
}