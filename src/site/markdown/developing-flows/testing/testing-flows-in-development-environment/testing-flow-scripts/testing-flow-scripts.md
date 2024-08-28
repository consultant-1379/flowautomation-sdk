#Testing Flow Scripts

---
This section describes how scripts in flows can be tested in the development environment.

- [Introduction](#Introduction)
- [Capabilities and Limitations](#Capabilities_and_Limitations)
    - [Capabilities](#Capabilities)
    - [Limitations](#Limitations)
- [Writing a Flow Script Test Case](#Writing_a_Flow_Script_Test_Case)
    - [Example flow script](#Example_flow_script)
    - [Example flow script test case](#Example_flow_script_test_cases)

###Introduction
A Flow Script is a script which is referenced directly by process BPMN within a flow. This feature is referred to by Camunda as Scripting. The script source can be embedded in the process BPMN file or it can be referenced as an separate file. Camunda refers to the latter as an External Resource.

So we can distinguish between (i) Embedded Script, and (ii) External Resource Script.

When it comes to testing and debugging flow scripts different approaches are possible.

For embedded scripts the only feasible way is to execute the flow process which contains them. For simple scripts this can be effective. And for trivial scripts any type of focused testing is not even necessary, for example

```java
import com.ericsson.oss.services.flowautomation.flowapi.Reporter
  
Reporter.updateReportSummary(execution, "Script Files Retrieval In Progress")
```

But for non-trivial and complex scripts this approach can be painful. For example, this is a log from a flow execution which has an error in a script task..
````
[main] ERROR org.camunda.bpm.engine.context - ENGINE-16006 BPMN Stack Trace:
    Task_00jb0al (activity-execute, ProcessInstance[53])
    Task_00jb0al, name=Validate Input
      ^
      |
    usertask-selection, name=Usertask Selection
  
Caused by: javax.script.ScriptException: groovy.lang.MissingPropertyException: No such property: flowIfnput for class: Script1
    at org.codehaus.groovy.jsr223.GroovyScriptEngineImpl.eval(GroovyScriptEngineImpl.java:324)
    at org.codehaus.groovy.jsr223.GroovyCompiledScript.eval(GroovyCompiledScript.java:72)
    at javax.script.CompiledScript.eval(CompiledScript.java:92)
    at org.camunda.bpm.engine.impl.scripting.CompiledExecutableScript.evaluate(CompiledExecutableScript.java:55)
    ... 167 more
Caused by: groovy.lang.MissingPropertyException: No such property: flowIfnput for class: Script1
    at org.codehaus.groovy.runtime.ScriptBytecodeAdapter.unwrap(ScriptBytecodeAdapter.java:53)
    at org.codehaus.groovy.runtime.callsite.PogoGetPropertySite.getProperty(PogoGetPropertySite.java:52)
    at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callGroovyObjectGetProperty(AbstractCallSite.java:307)
    at Script1.run(Script1.groovy:1)
    at org.codehaus.groovy.jsr223.GroovyScriptEngineImpl.eval(GroovyScriptEngineImpl.java:321)
    ... 170 more
````

The BPMN stack trace is helpful in pinpointing the culprit script task in the process and the groovy exception does help identify the particular problem. However, it is necessary to execute the process in the first place, and given that the errors may be in script tasks which are rarely executed or are deep into a large process, this would make for a slow and painful debugging experience.

However, for external resource scripts there is a much better approach which can effectively make it possible to unit test and debug scripts. For this reason it is highly recommended that external resource scripts are used for all but trivial scripts.

Note that external resource scripts are not only used as source for script tasks. They can also be used in expressions, input/output mapping, and other situations.

###Capabilities and Limitations
####Capabilities
The following functionality is provided...

- unit test external resource script
- debug external resource script, set breakpoints, display variables

Other test frameworks can be added to increase the test functionality and the test coverage. For example, WireMock can be used to simulate REST endpoints.

####Limitations
- only Groovy supported currently

###Development Environment setup
See [Testing Script Tasks](../../../designing/design-environment/flow-project-maven-archetype.html#Testing_Script_Tasks)

###Writing a Flow Script Test Case
Flow script test cases can be located in any convenient location in the flow project structure. If the [Flow Project Maven Archetype](../../../designing/design-environment/flow-project-maven-archetype.html) is being used the recommended location is in the src/test of the &lt;artifactId>-flow module.

> *<font size="2">...These examples come from the example flow [scripttaskTestFlow](../../../../example-flows/scripttaskTestFlow/scripttaskTestFlow.html)</font>*

####Maven dependencies
````
    <dependency>
        <groupId>com.ericsson.oss.services.flowautomation</groupId>
        <artifactId>flowautomation-service-test-framework</artifactId>
        <version>1.42.8</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.11</version>
        <scope>test</scope>
    </dependency>
````

####Example flow script
```groovy
package flows.scripttaskTestFlow.execute

import com.ericsson.oss.services.flowautomation.flowapi.FlowExecutionContext
import com.ericsson.oss.services.flowautomation.flowapi.EventRecorder

// use FlowExecutionContext and EventRecorder
def context = FlowExecutionContext.create(execution)
def flowId = context.getFlowId()
def flowExecutionName = context.getFlowExecutionName()
EventRecorder.info(execution, "Executing:"+flowId+"-"+flowExecutionName)
```
Note that in order that the script is executable in this test framework it must have a valid package declaration (line 1) which matches the location of the script in the flow project resources. If the flowautomation-sdk maven archetype is being used the package will be setup for scripts which are part of the located in the setup directory and execute for scripts which are located in the execute directory. If your flow locates scripts in a different directory then make sure the package name is set appropriately in your external resource scripts.

####Example flow script test cases
```groovy
package com.ericsson.oss.services.flowautomation.flows.test.scripttaskTestFlow

import static org.junit.Assert.*

import org.camunda.bpm.engine.delegate.BpmnError
import org.junit.Test

import com.ericsson.oss.services.flowautomation.test.fwk.FlowAutomationScriptBaseTest

class ScriptTestInGroovyTest extends FlowAutomationScriptBaseTest {

    @Test
    public void testScriptChangeVariable() {
        delegateExecution.setVariable("myvar", "value1")
        runFlowScript(delegateExecution, "flows/scripttaskTestFlow/execute/changeVariable.groovy")
        assertEquals("value2", delegateExecution.getVariable("myvar"))
    }

    @Test(expected = BpmnError.class)
    public void testScriptBpmnError() {
        delegateExecution.setVariable("doThrowError", "true")
        runFlowScript(delegateExecution, "flows/scripttaskTestFlow/execute/throwError.groovy")
    }
    
    @Test
    public void testScriptUpdateSummaryReport() {
        runFlowScript(delegateExecution, "flows/scripttaskTestFlow/execute/updateSummaryReport.groovy")
        checkExecutionSummaryReport(flowExecution, "Reporting stuff")
    }
    
    @Test
    public void testScriptLogEvent() {
        runFlowScript(delegateExecution, "flows/scripttaskTestFlow/execute/logEvent.groovy")
        checkExecutionEventIsRecorded(flowExecution, "INFO", "Executing:"+flowExecution.getFlowId()+"-"+flowExecution.getName())
    }
}
```

The main class which exposes the framework is **FlowAutomationScriptBaseTest**. Flow script tests should extend this class and add script-specific tests.

> *<font size="2">...While the example test case above has been written in Groovy, tests can also be written in Java. Changes may need to be made to the development environment in order to execute and debug Groovy. These changes are described in [Testing Script Tasks](../../../designing/design-environment/flow-project-maven-archetype.html#Testing_Script_Tasks)</font>*

Each of these test cases is explained...

#####testScriptChangeVariable()
This test creates the flowInput variable sets it in the delegateExecution. The delegateExecution is a protected variable provided by the FlowAutomationScriptBaseTest.

The test runs the scripts using the runFlowScript(), passing the delegateExecution and the path to the script.

After the script runs the test checks that a variable has been changed by the script.

#####testScriptBpmnError()
This test is expecting a script to throw a BPMN error.

