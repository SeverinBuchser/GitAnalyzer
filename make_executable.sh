#!/bin/bash

# maven install to local repo
mvn clean install

# extract info to where the executable jar was installed
localRepository=$(mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout)
groupId=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.groupId}' --non-recursive exec:exec 2>/dev/null)
artifactId=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.artifactId}' --non-recursive exec:exec 2>/dev/null)
version=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec 2>/dev/null)

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

# create executable which runs "java -jar $jar" with the appropriate parameters
cat <<EOF >git-analyzer
#!/bin/bash
java -jar $jar \$@
EOF

# make the executable actually executable
chmod +x mcr