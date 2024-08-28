package execute

import org.junit.Before
import org.junit.Test

import com.ericsson.oss.services.flowautomation.test.fwk.FlowAutomationScriptBaseTest

class ScriptTestInGroovyTest extends FlowAutomationScriptBaseTest {

    @Test 
    public void testScript() {
        def flowInput = [:]
        def message = [:]
        message.text = "Hello"
        flowInput.message = message;
        
        delegateExecution.setVariable("flowInput", flowInput)
        
        runFlowScript(delegateExecution, "execute/printMessage.groovy")
    }
}
