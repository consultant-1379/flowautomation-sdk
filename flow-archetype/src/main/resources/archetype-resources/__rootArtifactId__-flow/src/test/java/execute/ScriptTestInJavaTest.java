package execute;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ericsson.oss.services.flowautomation.test.fwk.FlowAutomationScriptBaseTest;

public class ScriptTestInJavaTest extends FlowAutomationScriptBaseTest {

    @Test 
    public void testScript() {
        Map<String, Object> flowInput = new HashMap<String, Object>();
        Map<String, Object> message = new HashMap<String, Object>();
        flowInput.put("message", message);
        message.put("text", "Hello");
        
        delegateExecution.setVariable("flowInput", flowInput);
        
        runFlowScript(delegateExecution, "execute/printMessage.groovy");
    }
}
