#Testing Flow Execution

---
This section describes how flow execution can be tested in the development environment.

- [Introduction](#Introduction)
- [Capabilities and Limitations](#Capabilities_and_Limitations)
    - [Capabilities](#Capabilities)
    - [Limitations](#Limitations)
- [Writing a Flow Execution Test Case](#Writing_a_Flow_Execution_Test_Case)
    - [Example flow setup phase](#Example_flow_setup_phase)
    - [Example flow execute phase](#Example_flow_execute_phase)
    - [Example flow execution test case](#Example_flow_execution_test_case)
    - [Import a flow](#Import_a_flow)
    - [Execute a flow](#Execute_a_flow)
    - [Interact with usertasks](#Interact_with_usertasks)
    - [Check report](#Check_report)
    - [Check events](#Check_events)
    - [Interact with building blocks](#Interact_with_building_blocks)
- [Javadoc](#Javadoc)

###Introduction
The test framework essentially allows test cases to be written which play the role of the Flow Automation user and user interface, and allow flows to be executed and user interactions to be performed.

###Capabilities and Limitations
####Capabilities
The following functionality is provided...

- import a flow
- execute a flow
- check flow is executing
- check if a usertask is active
- check contents of a usertask form
- render usertask form (simple text-based representation)
- complete a usertask
- check summary report
- get execution report
- render execution report (simple text-based representation)
- check events
- simulate a building block
- set security context - admin, operator, user

Other test frameworks can be added to increase the test functionality and the test coverage. For example, WireMock can be used to simulate REST endpoints.

Debug/trace information is displayed which can assist in the development, testing, and troubleshooting of flows. For example, all data transferred between the test case and Flow Automation (the FA Service) is logged and displayed. This allows the flow designer to see and verify the use of the various schemas (usertask, report) which have been designed as part of the flow, and data which is submitted to executing flows.

####Limitations
- Rendering of user interface uses a simple text-based representation, so final look&feel cannot be seen in development environment
- Client-side (user interface) validation of user inputs is not supported currently
- User interface actions and action framework are not supported currently

###Writing a Flow Execution Test Case
Flow execution test cases can be located in any convenient location in the flow project structure. If the [flowautomation-sdk maven archetype](../../../designing/design-environment/flow-project-maven-archetype.html) is being used the recommended location is in the &lt;artifactId>-testsuite module.

> *<font size="2">...This example flow can be found at [sdkBasicFlowExample](../../../../example-flows/sdkBasicFlowExample/sdkBasicFlowExample.html)</font>*

####Maven dependencies
````
    <dependency>
        <groupId>com.ericsson.oss.services.flowautomation</groupId>
        <artifactId>flowautomation-service-test-framework</artifactId>
        <version>1.42.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.11</version>
        <scope>test</scope>
    </dependency>
````

####Example flow setup phase

![](example-flow-setup-phase.png)

####Example flow execute phase

![](example-flow-execute-phase.png)

####Example flow execution test case
```java
package com.ericsson.oss.services.flowautomation.flows.test;
 
import static com.ericsson.oss.services.flowautomation.test.fwk.TestUtils.getFbbPackageFiles;
import static com.ericsson.oss.services.flowautomation.test.fwk.TestUtils.getFlowPackageBytes;
import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.equalTo;
 
import javax.json.Json;
 
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
 
import com.ericsson.oss.services.flowautomation.event.FbbRequestEvent;
import com.ericsson.oss.services.flowautomation.event.FbbResponseEvent;
import com.ericsson.oss.services.flowautomation.model.FlowDefinition;
import com.ericsson.oss.services.flowautomation.model.FlowExecution;
import com.ericsson.oss.services.flowautomation.test.fwk.FlowAutomationBaseTest;
import com.ericsson.oss.services.flowautomation.test.fwk.UsertaskCheckBuilder;
import com.ericsson.oss.services.flowautomation.test.fwk.UsertaskInputBuilder;
import com.ericsson.oss.services.flowautomation.test.fwk.fbb.FBBSimulator;
import com.ericsson.oss.services.flowautomation.test.fwk.fbb.FbbSuccessResponseBuilder;
 
/**
 * Test cases for SDK Basic Example flow.
 */
public class JseSdkBasicFlowExampleTest extends FlowAutomationBaseTest {
 
    String flowPackage = "sdkBasicFlowExample";
    String flowId = "com.ericsson.oss.fa.flows.sdkBasicFlowExample";
    FlowDefinition flowDefinition;
     
    String fbbType = "simpleFbb";
    FBBSimulator fbbSimulator;
 
    @Before
    public void before() {
        flowDefinition = importFlow(flowId, getFlowPackageBytes(flowPackage));
        fbbSimulator = createFbbSimulator(fbbType, getFbbPackageFiles(fbbType));
    }
 
    @After
    public void after() {
        removeFlow(flowId);
        deleteFbbSimulator(fbbSimulator);
    }
 
    @Test
    public void test() {
        // ==== Start flow execution ====
        String executionName = createUniqueInstanceName(flowId);
        FlowExecution flowExecution = startFlowExecution(flowDefinition, executionName);
         
        checkExecutionActive(flowExecution);
 
        // ==== Setup phase ====
        completeUsertaskChooseSetupInteractive(flowExecution);
 
        checkUsertaskActive(flowExecution, "Setup Config Part 1");
         
        checkUsertask(flowExecution, "Setup Config Part 1", new UsertaskCheckBuilder().
                check("Setup Config Part 1 > Parameter 1", "default-value"));
 
        completeUsertask(flowExecution, "Setup Config Part 1", new UsertaskInputBuilder().
                input("Setup Config Part 1 > Parameter 1", "value 1"));
 
        checkUsertaskActive(flowExecution, "Setup Config Part 2");
 
        checkUsertask(flowExecution, "Setup Config Part 2", new UsertaskCheckBuilder().
                check("Setup Config Part 2 > Parameter 2", "default-value"));
 
        completeUsertask(flowExecution, "Setup Config Part 2", new UsertaskInputBuilder().
                input("Setup Config Part 2 > Parameter 2", "value 2"));
 
        checkUsertaskReviewAndConfirm(flowExecution, new UsertaskCheckBuilder().
                check("Setup Config Part 1 > Parameter 1", "value 1").
                check("Setup Config Part 2 > Parameter 2", "value 2"));
         
        completeUsertaskReviewAndConfirm(flowExecution);
 
        // ==== Execute phase ====
        completeUsertask(flowExecution, "Ask If Should Execute Building Block", new UsertaskInputBuilder().
                input("Ask If Should Execute Building Block > Execute", true));
 
        // Get FBB execute request
        FbbRequestEvent fbbRequest = fbbSimulator.getExecuteRequest();
        with(fbbRequest.getJsonPayload()).assertThat("$.inProp1", equalTo("inVal1"));
         
        // Send FBB success response
        String fbbResponsePayload = Json.createObjectBuilder().add("outProp1", "outVal1").build().toString();
        FbbResponseEvent fbbSuccessResponse = new FbbSuccessResponseBuilder(fbbRequest).jsonPayload(fbbResponsePayload).build();
        fbbSimulator.sendResponse(fbbSuccessResponse);
         
        // ==== Flow execution completed ====
        checkExecutionExecuted(flowExecution);
         
        checkExecutionEventIsRecorded(flowExecution, "INFO", "Script Task executed");
         
        checkExecutionSummaryReport(flowExecution, "Flow Executed Successfully");
 
        checkExecutionReport(flowExecution);
 
        final String report = getExecutionReport(flowExecution);
        with(report).assertThat("$.header.status", equalTo("COMPLETED"));
        with(report).assertThat("$.body.executionVariable", equalTo("some value"));
    }
}
```

####Import a flow
The following snippet shows how a flow can be imported and removed.
```java
import static com.ericsson.oss.services.flowautomation.test.fwk.TestUtils.getFlowPackageBytes;
import com.ericsson.oss.services.flowautomation.model.FlowDefinition;
        .
        .
        String flowPackage = "sdkBasicFlowExample";
        String flowId = "com.ericsson.oss.fa.flows.sdkBasicFlowExample";
        .
        FlowDefinition flowDefinition = importFlow(flowId, getFlowPackageBytes(flowPackage));
        .
        .
        removeFlow(flowId);
        .
```
The _getFlowPackageBytes()_ utility can be used to find a named flow in the classpath, create a flow package zip from the located artifacts, and provide the zip as a byte array. This in turn can be passed to _importFlow()_. A _FlowDefinition_ is returned which can be used in other test methods.

_getFlowPackageBytes()_ expects to find the flow artifacts in a directory whose name matches the _flowPackage_ and is in turn located in a directory called flows at the root of the classpath. This directory structure will be created automatically if the [flowautomation-sdk maven archetype](../../../designing/design-environment/flow-project-maven-archetype.html) was used to create the flow project.

####Execute a flow
The following snippet shows how a flow can be executed and how the execution status can be checked.

```java
import com.ericsson.oss.services.flowautomation.model.FlowExecution;
        .
        .
        String executionName = createUniqueInstanceName(flowId);
        FlowExecution flowExecution = startFlowExecution(flowDefinition, executionName);
 
        checkExecutionActive(flowExecution);
        .
        .
        checkExecutionExecuted(flowExecution);
        .
```

####Interact with usertasks
The following snippet shows how interaction with usertasks can be performed in a test.

```java
import com.ericsson.oss.services.flowautomation.test.fwk.UsertaskCheckBuilder;
import com.ericsson.oss.services.flowautomation.test.fwk.UsertaskInputBuilder;
        .
        .
        completeUsertaskChooseSetupInteractive(flowExecution);
 
        checkUsertaskActive(flowExecution, "Setup Config Part 1");
          
        checkUsertask(flowExecution, "Setup Config Part 1", new UsertaskCheckBuilder().
                check("Setup Config Part 1 > Parameter 1", "default-value"));
  
        completeUsertask(flowExecution, "Setup Config Part 1", new UsertaskInputBuilder().
                input("Setup Config Part 1 > Parameter 1", "value 1"));
  
        checkUsertaskActive(flowExecution, "Setup Config Part 2");
  
        checkUsertask(flowExecution, "Setup Config Part 2", new UsertaskCheckBuilder().
                check("Setup Config Part 2 > Parameter 2", "default-value"));
  
        completeUsertask(flowExecution, "Setup Config Part 2", new UsertaskInputBuilder().
                input("Setup Config Part 2 > Parameter 2", "value 2"));
  
        checkUsertaskReviewAndConfirm(flowExecution, new UsertaskCheckBuilder().
                check("Setup Config Part 1 > Parameter 1", "value 1").
                check("Setup Config Part 2 > Parameter 2", "value 2"));
          
        completeUsertaskReviewAndConfirm(flowExecution);
        .
```

At the start of execution of a flow the user is presented with a usertask which allows the user to select between an interactive or a file-based setup. _completeUsertaskChooseSetupInteractive()_ can be used to select the interactive option. The alternative is _completeUsertaskChooseSetupFile()_.

_checkUsertaskActive()_ checks if the named usertask is active.

_checkUsertask()_ checks if the named usertask is active and if the usertask form contains the specified default data. Each of the form inputs are referenced using hierarchical name built from the names of the properties in the input schema. It also displays the usertask schema and a simple text-based representation of the usertask in the console.

_completeUsertask()_ can be used to complete the usertask with the specified input data. It also displays a simple text-based representation of the usertask in the console and the data sent to Flow Automation Service when the usertask is completed.

At the end of the setup phase the user is presented with the Review & Confirm usertask which allows the users to see all the inputs entered during the setup phase. _checkUsertaskReviewAndConfirm()_ checks if the Review & Confirm usertask is active and if the usertask form contains the specified input data. _completeUsertaskReviewAndConfirm()_ completes the Review & Confirm usertask and brings the flow into the execute phase.

The following shows examples of the console output.

````
[main] INFO com.ericsson.oss.services.flowautomation.flows.test.JseSdkBasicFlowExampleTest - Checking if usertask with name Setup Config Part 1 is active for flowId com.ericsson.oss.fa.flows.sdkBasicFlowExample executionName com.ericsson.oss.fa.flows.sdkBasicFlowExample-8b871613-3e84-42ee-916a-94c59d5fc64f
[main] INFO com.ericsson.oss.services.flowautomation.flow.usertasks.schema.builder.SchemaBuilder - Get schema : setup/flow-input-schema.json of deployment : 1 from cache
---------------------------------- Usertask Schema ---------------------------
{
  "$schema" : "http://json-schema.org/draft-04/schema#",
  "title" : "User Task schema",
  "name" : "Setup Config Part 1",
  "description" : "",
  "type" : "object",
  "action" : "Continue",
  "properties" : {
    "setupConfigPart1" : {
      "name" : "Setup Config Part 1",
      "type" : "object",
      "properties" : {
        "parameter1" : {
          "name" : "Parameter 1",
          "type" : "string",
          "default" : "default-value"
        }
      },
      "required" : [ "parameter1" ],
      "additionalProperties" : false
    }
  },
  "required" : [ "setupConfigPart1" ],
  "additionalProperties" : false
}
----------------------------------------------------------------------------
---------------------------------- Usertask ----------------------------------
|  Task: Setup Config Part 1
|    Parameter 1: default-value
| 
|    <Continue>
| 
----------------------------------------------------------------------------
[main] INFO com.ericsson.oss.services.flowautomation.flows.test.JseSdkBasicFlowExampleTest - Completing usertask Setup Config Part 1
-------------------------------- Usertask Submit ---------------------------
|  Task: Setup Config Part 1
|    Parameter 1: value 1
| 
|    <Continue>
| 
----------------------------------------------------------------------------
--------------------------- Usertask Completion Data -----------------------
{
  "setupConfigPart1" : {
    "parameter1" : "value 1"
  }
}
````

####Check report
The following snippet shows how the summary report can be checked, and the execution report rendered, retrieved and checked.

```java
import static com.jayway.jsonassert.JsonAssert.with;
        .
        .
        checkExecutionSummaryReport(flowExecution, "Flow Executed Successfully");
  
        checkExecutionReport(flowExecution);
  
        final String report = getExecutionReport(flowExecution);
        with(report).assertThat("$.header.status", equalTo("COMPLETED"));
        with(report).assertThat("$.body.executionVariable", equalTo("some value"));
        .
````

_checkExecutionSummaryReport()_ checks if the execution summary report matches the expected value. It also displays the summary report in the console.

_checkExecutionReport()_ displays a simple text-based representation of the the execution report in the console.

_getExecutionReport()_ retrieves the execution report which can then be examined by the test.

The following shows examples of the console output.

````
------------------------- Execution Summary Report --------------------------
Flow Executed Successfully
----------------------------------------------------------------------------
------------------------- Execution Report Object --------------------------
{
  "header" : {
    "reportTime" : "2020-11-10T11:58:29+0000",
    "flowId" : "com.ericsson.oss.fa.flows.sdkBasicFlowExample",
    "flowVersion" : "1.0.0",
    "flowName" : "SDK Basic Flow Example",
    "flowExecutionName" : "com.ericsson.oss.fa.flows.sdkBasicFlowExample-8b871613-3e84-42ee-916a-94c59d5fc64f",
    "startedBy" : "#Ericsson",
    "startTime" : "2020-11-10T11:57:59+0000",
    "endTime" : "2020-11-10T11:58:27+0000",
    "status" : "COMPLETED"
  },
  "body" : {
    "executionVariable" : "some value"
  }
}
----------------------------------------------------------------------------
[main] INFO com.ericsson.oss.services.flowautomation.flow.usertasks.schema.builder.SchemaBuilder - Get schema : report/flow-report-schema.json of deployment : 1 from cache
---------------------------- Execution Report ------------------------------
|  Execution Report
|  Flow Name: SDK Basic Flow Example
|  Flow Execution Name: com.ericsson.oss.fa.flows.sdkBasicFlowExample-8b871613-3e84-42ee-916a-94c59d5fc64f
|  Start Time: 2020-11-10T11:57:59+0000
|  End Time: 2020-11-10T11:58:27+0000
|  Started By: #Ericsson
|  Status: COMPLETED
|    Execution Variable: some value
----------------------------------------------------------------------------
````

####Check events
The following snippet shows how flow events can be checked.

````
.
checkExecutionEventIsRecorded(flowExecution, "INFO", "Task executed");
.
````

####Interact with building blocks
The following snippet shows how building blocks can be simulated.

````
import static com.ericsson.oss.services.flowautomation.test.fwk.TestUtils.getFbbPackageFiles;
import javax.json.Json;
import com.ericsson.oss.services.flowautomation.event.FbbRequestEvent;
import com.ericsson.oss.services.flowautomation.event.FbbResponseEvent;
import com.ericsson.oss.services.flowautomation.test.fwk.fbb.FBBSimulator;
import com.ericsson.oss.services.flowautomation.test.fwk.fbb.FbbSuccessResponseBuilder;
        .
        fbbSimulator = createFbbSimulator(fbbType, getFbbPackageFiles(fbbType));
        .
        // Get FBB execute request
        FbbRequestEvent fbbRequest = fbbSimulator.getExecuteRequest();
        with(fbbRequest.getJsonPayload()).assertThat("$.inProp1", equalTo("inVal1"));
         
        // Send FBB success response
        String fbbResponsePayload = Json.createObjectBuilder().add("outProp1", "outVal1").build().toString();
        FbbResponseEvent fbbSuccessResponse = new FbbSuccessResponseBuilder(fbbRequest).jsonPayload(fbbResponsePayload).build();
        fbbSimulator.sendResponse(fbbSuccessResponse);
        .
````

_createFbbSimulator()_ creates a building block simulator.

The _getFbbPackageBytes()_ utility can be used to find the input and output schemas for the named building block type in the classpath, create a zip from the located artifacts, and provide the zip as a byte array. This in turn can be passed to _createFbbSimulator()_. An _FBBSimulator_ is returned which can be used in other test methods.

_getFbbPackageBytes()_ expects to find the building block input and out schemas in a directory whose name matches the _fbbType_ and is in turn located in a directory called _fbbSims_ at the root of the classpath.

_fbbSimulator.getExecuteRequest()_ polls the building block simulator for a request event. This request would be generated when the flow executes the building block external task. The request event can be checked by the test to ensure it is as expected. The request event is also displayed in the console.

An appropriate building block response event _FbbResponseEvent_ is created using _FbbSuccessResponseBuilder_, supplying the required response data. In this example the type of response is _Success_. Other types of responses are _Acknowledge_, _Progress_, _Error_, _Overload_.

The response is sent back to the flow using _fbbSimulator.sendResponse()_. The response event is also displayed in the console.

> *<font size="2">...The building block which is simulated in this example is for demonstration purposes only, is not real, and will only work in this test framework. It is not intended for use in the Flow Automation Docker Design Runtime Environment or in the final runtime system.</font>*

###Javadoc
[Flow Execution Test API](../../../../javadoc/apidocs-sdk-flow-execution-test/index.html)
