node ("maven") {
    stage("Clone") {
        checkout scm
    }

    stage("Build") {
        sh "mvn clean install -DskipTests -Dquarkus.container-image.build=false"
    }

    stage("Helm") {
        dir ("chart") {
            sh "curl -L https://get.helm.sh/helm-v3.11.1-linux-amd64.tar.gz -o helm-linux-amd64.tar.gz"
            sh "tar -zxvf helm-linux-amd64.tar.gz"
            sh "rm -f helm-linux-amd64.tar.gz"

            withEnv(['PATH+HELM=linux-amd64']) {
                sh "helm upgrade --install v1 ."
            }
        }
    }

    stage("Container Build") {
        sh "oc start-build v1-cats-and-dogs-docker --from-dir='.' --follow"
    }

    stage("Rollout") {
        sh "oc rollout status deployment/v1-cats-and-dogs --watch=true"
    }
}