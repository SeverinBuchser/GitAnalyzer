#!/bin/bash

NC='\033[0m'
RED='\033[1;31m'
GREEN='\033[1;32m'
BLUE='\033[1;34m'
GRAY='\033[0;37m'

echo -ne "Building executable jar: "

# maven install to local repo
mvn clean install -q

# exit if mvn clean install failed
if [ $? -ne 0 ];
  then
    printf "${RED}Fail${NC}, exiting"
    exit $?
fi

printf "${GREEN}Success${NC}\n"

# extract info to where the executable jar was installed
localRepository=$(mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout)
printf "${GRAY}Found local maven repository: ${localRepository}${NC}\n"

groupId=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.groupId}' --non-recursive exec:exec 2>/dev/null)
printf "${GRAY}Found groupId: ${groupId}${NC}\n"

artifactId=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.artifactId}' --non-recursive exec:exec 2>/dev/null)
printf "${GRAY}Found artifactId: ${artifactId}${NC}\n"

version=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec 2>/dev/null)
printf "${GRAY}Found version: ${version}${NC}\n"

# location of the jar
jar="$localRepository"

# add group id as path
IFS='.' read -r -a array <<< "$groupId"
for element in "${array[@]}"
do
    jar+="/$element"
done

# add artifact id, version and jar name
jar+="/$artifactId/$version/$artifactId-$version-jar-with-dependencies.jar"

echo -ne "Building executable script: "

# create executable which runs "java -jar $jar" with the appropriate parameters
cat <<EOF >git-analyzer
#!/bin/bash
java -jar $jar \$@
EOF

# exit if cat failed
if [ $? -ne 0 ];
  then
    printf "${RED}Fail${NC}, exiting"
    exit $?
fi

printf "${GREEN}Success${NC}\n"

# make the executable actually executable
chmod +x git-analyzer

printf "Created executable, with jar at: ${BLUE}${jar}${NC}\n"