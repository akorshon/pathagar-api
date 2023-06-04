#!/bin/zsh

file=deployment/deployment.yml
buildApplication(){
  showLabel "build java application"
  export JAVA_HOME=$JAVA_17_HOME # Set environment for java 17
  #mvn clean package -DskipTests
  gradle build -x test
}


changeDeploymentVersion(){
    docker context use default
    showLabel   "changing deployment version"

    # Change version
    currentVersion=$(sed -n -e 's/^.*pathagar-api:v//p' $file)
    echo "current version ${currentVersion}"

    nextVersion=$((currentVersion + 1))
    echo "next version ${nextVersion}"

    # Change image
    sed -i '' "s/pathagar-api:v$currentVersion.*/pathagar-api:v$nextVersion/g" $file
    image=$(grep -o 'eu.gcr.io/marufh/pathagar-api.*' $file)
}

buildDockerImage() {
    docker context use default
    showLabel "building & pushing docker image:$image"
    docker buildx build --platform linux/amd64 -t $image --push .
}

apply(){
  kubectl apply -f $file
}

showLabel(){
  echo "
********************************************************************************
  $1
*********************************************************************************";
}

deploy(){
  buildApplication
  changeDeploymentVersion
  buildDockerImage
  apply
  cleanOldImage
}


cleanOldImage(){
  IMAGES_TO_KEEP="3"
  IMAGENAME="eu.gcr.io/marufh/pathagar-api"
  echo  "Checking ${IMAGENAME} for cleanup requirements"

 # Get all the digests for the tag ordered by timestamp (oldest first)
  DIGESTLIST=$(gcloud container images list-tags ${IMAGENAME} --sort-by timestamp --format='get(digest)')
  DIGESTLISTCOUNT=$(echo "${DIGESTLIST}" | wc -l)

  if [ ${IMAGES_TO_KEEP} -ge "${DIGESTLISTCOUNT}" ]; then
    echo -e "${YELL}Found ${DIGESTLISTCOUNT} digests, nothing to delete${NC}"
  else
    # Filter the ordered list to remove the most recent 3
    DIGESTLISTTOREMOVE=$(echo "${DIGESTLIST}" | ghead -n -${IMAGES_TO_KEEP})
    DIGESTLISTTOREMOVECOUNT=$(echo "${DIGESTLISTTOREMOVE}" | wc -l)
    echo -e "${YELL}Found ${DIGESTLISTCOUNT} digests, ${DIGESTLISTTOREMOVECOUNT} to delete${NC}"

    # Do deletion or say nothing to do
    if [ "${DIGESTLISTTOREMOVECOUNT}" -gt "0" ]; then
      echo -e "${YELL}Removing ${DIGESTLISTTOREMOVECOUNT} digests${NC}"
      while IFS= read -r LINE; do
        LINE=$(echo $LINE|tr -d '\r')
          gcloud container images delete ${IMAGENAME}@${LINE} --force-delete-tags --quiet
      done <<< "${DIGESTLISTTOREMOVE}"
    else
      echo -e "${YELL}No digests to remove${NC}"
    fi
  fi
}


option="${1}"
case ${option} in
  "deploy") deploy ;;
esac

