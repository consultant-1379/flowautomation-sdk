#Testing Flows in Development Environment

---
This section describes techniques and tools which can be used to test flows in the development environment.

Typically, the design of a flow begins by creating a project structure using a maven archetype such as the [flowautomation-sdk maven archetype](../../designing/design-environment/flow-project-maven-archetype.html). This project structure can be imported into an IDE such as Eclipse or IntelliJ IDEA. This is a very convenient environment for designing non-BPMN artifacts (BPMN artifacts are designed using Camunda Modeler which is external to the IDE environment).

A flow designer can then proceed to test the flow in this development environment with the help of the Flow Automation Test Framework. This framework makes it possible to test

- overall execution of the flow, including usertasks and building block tasks
- individual flow scripts

However, it is important to realise that the testing performed in this environment will not exactly replicate the conditions which would be present for a flow which is deployed to a real system. The type of testing described here involves executing flows and scripts in 'isolation' from the final runtime system. Having said that, it will be possible to test many scenarios with realistic data. The resulting tests could then represent a reasonable degree of automated regression, which ideally should be supplemented by other tests involving the final runtime system.

- [Testing Flow Execution](testing-flow-execution/testing-flow-execution.md)
- [Testing Flow Scripts](testing-flow-scripts/testing-flow-scripts.md)
