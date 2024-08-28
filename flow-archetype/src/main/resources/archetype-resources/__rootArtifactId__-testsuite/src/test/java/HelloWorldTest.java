/*
 * ------------------------------------------------------------------------------
 *  *******************************************************************************
 *  * COPYRIGHT Ericsson 2020
 *  *
 *  * The copyright to the computer program(s) herein is the property of
 *  * Ericsson Inc. The programs may be used and/or copied only with written
 *  * permission from Ericsson Inc. or in accordance with the terms and
 *  * conditions stipulated in the agreement/contract under which the
 *  * program(s) have been supplied.
 *  *******************************************************************************
 *  *----------------------------------------------------------------------------
 */

package ${package};

import static com.ericsson.oss.services.flowautomation.test.fwk.TestUtils.getFlowPackageBytes;

import org.junit.Test;

import com.ericsson.oss.services.flowautomation.model.FlowDefinition;
import com.ericsson.oss.services.flowautomation.model.FlowExecution;
import com.ericsson.oss.services.flowautomation.test.fwk.FlowAutomationBaseTest;
import com.ericsson.oss.services.flowautomation.test.fwk.TestClientType;
import com.ericsson.oss.services.flowautomation.test.fwk.UsertaskInputBuilder;

/**
 * Test cases for Hello World flow using a simple DSL.
 */
public class HelloWorldTest extends FlowAutomationBaseTest {

    private final String flowPackage = "${rootArtifactId}-flow";
    private final String flowId = "${groupId}.${rootArtifactId}";

    @Test
    public void testFlowExecution() {

        FlowDefinition flowDefinition = importFlow(flowId, getFlowPackageBytes(flowPackage));

        FlowExecution flowExecution = startFlowExecution(flowDefinition, createUniqueInstanceName(flowId));

        completeUsertaskChooseSetupInteractive(flowExecution);

        completeUsertask(flowExecution, "Message", new UsertaskInputBuilder().input("Message > Text", "hello"));

        completeUsertaskReviewAndConfirm(flowExecution);

        checkExecutionExecuted(flowExecution);

        removeFlow(flowId);
    }

    @Override
    protected TestClientType selectClientType() {
        return TestClientType.JSE;
    }
}
