#scripttaskTestFlow
---

*(download flow package… [scripttaskTestFlow-1.0.1.zip](scripttaskTestFlow-1.0.1.zip))*

This example flow contains script tasks and can be used to demonstrate use of script unit tests.

<br/><img src="execute-process.png" alt="execute-process"/><br/>

The following are the scripts for each of the script tasks...

*Change variable - execute/changeVariable.groovy*
```groovy
package flows.scripttaskTestFlow.execute

// Change a variable
execution.setVariable("myvar", "value2")
```

*Throw error - execute/throwError.groovy*
```groovy
package flows.scripttaskTestFlow.execute

import org.camunda.bpm.engine.delegate.BpmnError

if (doThrowError == "true") {
    throw new org.camunda.bpm.engine.delegate.BpmnError("some-error")
}
```

*Update summary report - execute/updateSummaryReport.groovy*
```groovy
package flows.scripttaskTestFlow.execute

import com.ericsson.oss.services.flowautomation.flowapi.Reporter

// Use Reporter
Reporter.updateReportSummary(execution, "Reporting stuff")
```

*Log event - execute/logEvent.groovy*
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
<br/>
The following unit tests show how each of the script tasks can be tested.
> *<font size="2">...These tests are written in Groovy. They could also be written in Java.</font>*

```groovy
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
