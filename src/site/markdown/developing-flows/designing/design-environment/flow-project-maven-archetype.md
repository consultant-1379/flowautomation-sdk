#Flow Project Maven Archetype

---

A **flowautomation-sdk** maven archetype has been provided that can be used for generating a new flow project. As a starting point for the new flow the generated project contains a basic "Hello world" flow, sample unit test cases for scripts, and a testsuite having a test case to test the "Hello world" flow execution.

- [Generate Sample Flow Project](#Generate_Sample_Flow_Project)
- [Internals of Flow Project Structure](#Internals_of_Flow_Project_Structure)
- [Designing the Flow](#Designing_the_Flow)
- [Testing Script Tasks](#Testing_Script_Tasks)
- [Testing the Flow](#Testing_the_Flow)
- [Packaging the Flow](#Packaging_the_Flow)

###Generate Sample Flow Project
Steps to generate a sample flow project is given below:

- Download and Install Maven. The version should be >3.0.5 .

    ````
        Note : Change the repository from /local/workspace/maven_repo to your own repository location, for example /local/repo
    ````

- Configure maven's settings.xml file. You can use [settings.xml](settings.xml) file. 
- The maven command to generate flow project is

    #####Maven command:

    ````
    mvn archetype:generate \
    -DarchetypeGroupId=com.ericsson.oss.services.flowautomation \
    -DarchetypeArtifactId=flow-archetype \
    -DarchetypeVersion=<SDK version> \
    -DgroupId=<group-id of the project> \
    -DartifactId=<artifact-id of the project> \
    -DCXP=<CXP number> \
    -DflowPackage=<flow Package Name> \
    -Dversion=<version>
    ````
    
    #####Example:
    
    ````
    mvn archetype:generate \
    -DarchetypeGroupId=com.ericsson.oss.services.flowautomation \
    -DarchetypeArtifactId=flow-archetype \
    -DarchetypeVersion=<SDK version> \
    -DgroupId=com.ericsson.oss.flow  \
    -DartifactId=npa \
    -DCXP=CXPXXXXXX \
    -DflowPackage=npaflow    
    ````
    
    #####Parameters:
    
    ````
    groupId: group id of the new flow project
    artifactId: artifact id of the new flow project
    version: version of new flow project (default value 1.0.0-SNAPSHOT)
    CXP:CXP number of the new flow project
    flowPackage: flow package of new flow project
    ````
    
    When the above maven command is executed, a confirmation will be asked. If the details provided for the Parameters are correct, then type 'Y' or press 'ENTER else type 'N'.
    
    #####Confirmation:
    
    ````
    Confirm properties configuration:
    groupId: com.ericsson.oss.flow
    artifactId: npa
    version: 1.0.0-SNAPSHOT
    package: com.ericsson.oss.flow
    CXP: CXPXXXXXX
    flowPackage: npaflow
     Y: :
    ````
    
    Once successfully executed, a project root directory with name matching the 'artifactId' will be created. For this example, **npa** directory is created.
 
###Internals of Flow Project Structure
Let's explore the structure of flow project.

  
#####Flow Project Structure:
   
   
  
   ````
   |-- ERICnpaflow_CXPXXXXXX
   |   |-- pom.xml
   |   `-- src
   |       `-- main
   |           |-- python
   |           |   `-- README.txt
   |           |-- resources
   |           |   `-- README.txt
   |           `-- scripts
   |-- npa-flow
   |   |-- pom.xml
   |   `-- src
   |       `-- main
   |           |-- assembly.xml
   |           `-- resources
   |               |-- execute
   |               |   `-- hello-execute.bpmn
   |               |-- flow-definition.json
   |               |-- report
   |               |   `-- flow-report-schema.json
   |               `-- setup
   |                   |-- flow-input-schema.json
   |                   `-- hello-setup.bpmn
   |       `-- test
   |           `-- java
   |               `-- execute
   |                   |-- ScriptTestInGroovyTest.groovy
   |                   `-- ScriptTestInJavaTest.java
   |-- pom.xml
   `-- npa-testsuite
       |-- pom.xml
       `-- src
           `-- test
               |-- java
               |   `-- com
               |       `-- ericsson
               |           `-- oss
               |               `-- flow
               |                   `-- HelloWorldTest.java
               `-- resources
   ````
   
   1. Navigate  into the newly created flow project. It contains 3 directories and a pom.xml file.
   2. **`<artifactId>-flow`** directory - for the example command above, it will be npa-flow directory.
        1. Navigate to src/main/resources directory. It contains the sample flow directories and files.
        2. Navigate to src/test/java directory. It contains unit tests for script tasks.
   3. **`<artifactId>-testsuite`** directory 
        1. Navigate to src/test/java/com/ericsson/oss/flow. It contains HelloWorldTest.java. A JUnit test case for testing the sample HelloWorld flow using Flow Automation Test Framework.
   4. **`ERIC<flowPackage>_<CXP>`**  directory - for the example command above, it will be ERICnpaflow_CXPXXXXXX.
   
###Designing the Flow
**`<artifactId>-flow`** directory contains setup, execute, report directories and flow-definition.json file.

The flow designer needs to use BPMN modeller tool to design the flows. We recommend to use Camunda modeller which is available for free on Camunda Official Website.

The flow designer can add new BPMN files into setup and execute directories.

###Testing Script Tasks
Unit tests for script tasks can be added to src/test/java. The tests case be written in Java or Groovy.
Script tasks which are to be unit tested must be configured in the BPMN as external resources and have a valid package declaration.
Sample tests are provided.
These tests can be executed from maven command line along with flow unit tests using `mvn clean install`

The tests can also be executed within an IDE such as Eclipse or IntelliJ. The scripts can also be debugged (breakpoints, stepping, examine variables, etc).
The following configurations changes are needed...
##### Eclipse
   1. Install Groovy plugin. Recommended plugin is Groovy Development Tools.
   2. Import newly-created flow project using Eclipse maven project import.
   3. Add the following to .classpath for the `<artifactId>-flow` project...  `<classpathentry kind="con" path="GROOVY_SUPPORT"/>`. Note that it will be necessary to perform this step any time a change is made to the maven config for this project (eg. Maven > Update Project).
   4. Execute `mvn clean install` using Eclipse. It should now be possible to execute the flow tests in the `<artifactId>-testsuite` directory.
   5. Select `<artifactId>-flow` project in Package Explorer > Properties > Source > `<artifactId>-flow/src/main/resources` > Excluded > Edit > remove \*\* exclusion pattern > Finish > Apply and Close.
   6. It should now be possible to execute and debug the example script unit tests in the `<artifactId>-flow/src/main/java/execute` directory.
##### IntelliJ
   1. Import newly-created flow project using IntelliJ maven project import.
   2. Execute `mvn clean install` using Eclipse. It should now be possible to execute the flow tests in the `<artifactId>-testsuite` directory.
   3. It may be necessary to use Add Framework Support for Groovy
   4. It should now be possible to execute and debug the example script unit tests in the `<artifactId>-flow/src/main/java/execute` directory.

###Testing the Flow
The flow designer can add new JUnit test cases into **`<artifactId>-testsuite`** and the test cases can leverage DSLs from Flow Automation Test Framework.

The test framework makes the testing and debugging of the flow easier.

Flow designer can import the project into any Java IDE, write test cases, execute and debug them.

###Packaging the Flow
Execute the command at project root directory:


#####Build Command:


````
mvn clean install
````

The build command compiles, executes the test cases against the flow, package the flow into zip file and generates the flow rpm.

````
[INFO] npa ................................................ SUCCESS [  1.255 s]
[INFO] [npa-flow] JAR module .............................. SUCCESS [  1.224 s]
[INFO] [npa] RPM module ................................... SUCCESS [  0.372 s]
[INFO] [npa-testsuite]..................................... SUCCESS [ 24.114 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
````

Lets explore the output of build command.

1. Navigate to **`<artifactId>-flow`** directory. A target directory is created which contains a flow zip file **`<flowPackage>-<version>.zip`**. For this sample flow project, npa-flow-1.0.0-SNAPSHOT.zip will be created.
2. Flow designer can import the zip file into Flow Automation from UI/NBI and execute the flow.
3. Navigate to **`ERIC<flowPackage>_<CXP>`**  directory. A rpm is created under target/rpm/ERICnpaflow_CXPXXXXXX/RPMS/noarch directory.
4. Flow designer can deliver this rpm into Nexus.
   
   
