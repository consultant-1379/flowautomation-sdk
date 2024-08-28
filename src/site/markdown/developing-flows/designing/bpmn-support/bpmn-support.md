#BPMN Support

---
The primary artifact in the definition of Flows is the process model based on the BPMN standard. BPMN is introduced, briefly described, and resources provided to help get more familiar with this modeling.

Flow Automation uses Camunda BPM as the implementation of the BPMN process engine which is at FA's core. Camunda BPM has many useful features, many of which are leveraged by FA. However, due to way FA embeds Camunda BPM and the deployment approach taken, FA does not use all Camunda BPMN features. This is an extremely important point for Flow Designers who need to be aware which Camunda BPM features are available to them. These features are outlined here. More specific references to Camunda BPM features can be found in other parts of this SDK documentation.

- [BPMN](#BPMN)
    - [BPMN in a nutshell](#BPMN_in_a_nutshell)
    - [BPMN specification and resources](#BPMN_specification_and_resources)
- [Camunda BPM](#Camunda_BPM)
    - [Camunda BPM components](#Camunda_BPM_components)
    - [Camunda BPM support for BPMN](#Camunda_BPM_support_for_BPMN)
    - [Camunda Extensions](#Camunda_Extensions)
- [Camunda BPM Features Supported by Flow Automation](#Camunda_BPM_Features_Supported_by_Flow_Automation)
    - [Flow Automation support of Camunda BPM features](#Flow_Automation_support_of_Camunda_BPM_features)
- [Camunda Documentation Overview](#Camunda_Documentation_Overview)
    - [Camunda BPMN Tutorial](#Camunda_BPMN_Tutorial)
    - [Camunda BPM Manual](#Camunda_BPM_Manual)
    - [Camunda Best Practices](#Camunda_Best_Practices)

###BPMN
####BPMN in a nutshell
Business Process Model and Notation (BPMN) is a standard for business process modeling which is defined by the [Object Management Group](https://www.omg.org/index.htm). The OMG definition of [BPMN](https://www.omg.org/spec/BPMN/2.0.2/)...

Business Process Model and Notation has become the de-facto standard for business processes diagrams. It is intended to be used directly by the stakeholders who design, manage and realize business processes, but at the same time be precise enough to allow BPMN diagrams to be translated into software process components. BPMN has an easy-to-use flowchart-like notation that is independent of any particular implementation environment.

A BPMN process model is an XML document which consists of XML elements and attributes describing

the BPMN elements (tasks, gateways, and events)
interconnections between the BPMN elements, typically sequence flows (shown as arrows)
the visual representation and placement of the BPMN element symbols
In addition the process model for a process which can be executed will contain XML elements and attributes which are needed to describe the implementation of the BPMN elements, eg. scripts, class references, expressions, transaction configuration

####BPMN specification and resources
The BPMN specification can be found [here](https://www.omg.org/spec/BPMN/2.0.2/PDF). This is a very large document which is difficult to read. Granted, the likely primary audience for the specification document is implementers of BPMN platform. However, for those interested in using BPMN there are many resources available which describe BPMN is a must more approachable way. For example, the [Camunda BPMN Tutorial](https://camunda.com/bpmn/) is a good place to start and provides...

- An introduction to BPMN
- An extensive [reference](https://camunda.com/bpmn/reference/) to the BPMN symbols
- Modeling examples
- Access to an [online BPMN modeling tool](https://camunda.com/bpmn/tool/). This is a useful place to start getting familiar with BPMN modeling, but if you intend to design Flows it will be necessary to use the standalone [Modeler](${camundaManualBaseUrl}/modeler/) application (more on that later)

Some more useful resources...

- [BPMN Method & Style with BPMN Implementer's Guide](https://www.amazon.com/Bpmn-Method-Style-Implementers-Guide/dp/0982368119/), Bruce Silver
- [Real-Life BPMN](https://www.amazon.co.uk/Real-Life-BPMN-4th-introduction-DMN/dp/1086302095/), Camunda

###Camunda BPM
At the core of Flow Automation can be found [Camunda BPM](${camundaManualBaseUrl}).

Camunda BPM is a primarily open-source platform, meaning that the majority of the features and implementation is open-source. While it is possible for non-Camunda employees to contribute, the vast majority of the features are implemented by Camunda engineers. The open-source platform is known as the Community Edition.

####Camunda BPM components
Camunda BPM implements the BPMN standard and is made up of several components. Flow Automation makes use of some of these components...

- Process Engine - component within FA to which process models are deployed and which executes these process models
- Modeler - used to design process models

There are also some components (eg. Camunda Optimize) and features (eg. Cockpit history) which are not open-source. These are part of the Enterprise Edition. These are clearly highlighted in Camunda BPM documentation. Flow Automation does not make use of, or expose, any Enterprise Edition components or features.

####Camunda BPM support for BPMN
Camunda BPM implements a significant part of the BPMN standard. Implementers of the BPMN standard (Camunda is just one) have determined that some parts of the BPMN standard are not essential and have chosen to implement subsets of the standard which they believe to be sufficient for most real-world process modeling demands.

Camunda BPM documentation clearly identifies which parts of the BPMN standard are supported. This can be seen using the orange markings in the [Camunda BPM reference](${camundaManualBaseUrl}/reference/bpmn20/).

####Camunda Extensions
Camunda BPM adds extensions to the BPMN which make it easier to implement executable process models. Ideally, extensions wouldn't be needed and process models could theoretically be portable across different process engine implementations. However, this ideal is not practical, and one key consequence of the need for implementation-specific extensions is that process models are not portable to different process engines.

Camunda BPM documents extensions to BPMN [elements](${camundaManualBaseUrl}/reference/bpmn20/custom-extensions/extension-elements/) and [attributes](${camundaManualBaseUrl}/reference/bpmn20/custom-extensions/extension-attributes/).

In addition, Camunda BPM introduces [process variables](${camundaManualBaseUrl}/user-guide/process-engine/variables/) as a convenient way to manage process data.

###Camunda BPM Features Supported by Flow Automation
As previously mentioned, Camunda BPM implements a significant subset of the BPMN standard, and adds custom extensions and features. This is illustrated in the diagram below. An example of a BPMN feature supported by Camunda BPM is Compensation event. An example of a BPMN feature not supported by Camunda BPM is Multiple event. This can be seen in the [Camunda BPM reference](${camundaManualBaseUrl}/reference/bpmn20/).

![](bpmn-camunda-fa-feature-subsets.png)

Flow Automation uses a subset of the BPMN features supported by Camunda BPM and also a subset of the custom extensions and features. Flow Automation adds additional custom features. This is also illustrated in the diagram above.

####Flow Automation support of Camunda BPM features
The reasons why Flow Automation does not support certain Camunda BPM features depends on the feature. The following are some examples...

- Java Delegate Service Task - This is not supported by FA because Flows need to be classless (ie. contain no Java classes) in order to be lifecycle-managed independently of the FA application (ie. able to deploy and undeploy Flows without needing to perform system upgrade).
- Process Application - This is not supported by FA because Flows need to be classless
- Process Engine REST API - This API is hidden by FA and is not exposed
- Certain Process Engine Java APIs - Many Java APIs are not exposed because it is not expected that they would be used by Flows. Some key Java APIs are exposed for use by Flows, eg. DelegateExecution used for getting and setting variables.

The following table indicates which Camunda BPM BPMN features are supported by FA. It may help to refer to the [Camunda BPM reference](${camundaManualBaseUrl}/reference/bpmn20/).

| Category       | Feature                               | FA Support ? | Discussion                                                                            |
|----------------|---------------------------------------|--------------|---------------------------------------------------------------------------------------|
| Subprocesses   | Subprocess                            | Y            |                                                                                       |
|                | Call Activity                         | Y            |                                                                                       |
|                | Event Subprocess                      | Y            |                                                                                       |
|                | Transaction Subprocess                | Y            |                                                                                       |
| Tasks          | Service Task - custom Java Class      | N            | Flows are classless                                                                   |
|                | Service Task - FA-supplied Java Class | Y            | FA provides some builtin framework Java tasks.                                        |
|                | Service Task - Expression             | Y            |                                                                                       |
|                | Service Task - Delegate Expression    | N            | Flows are classless.                                                                  |
|                | Service Task - External               | Y            | This is how Flow Building Blocks are called.                                          |
|                | Service Task - Connector              | ?            |                                                                                       |
|                | User Task                             | Y            |                                                                                       |
|                | Script Task                           | Y            | Groovy and Javascript supported.                                                      |
|                | Manual Task                           | Y            |                                                                                       |
|                | Receive Task                          | N            | This is equivalent to a Message event catch. FA does not support Message event.       |
|                | Send Task                             | N            | This is equivalent to a Message event throw, FA does not support Message event.       |
| Gateways       | All gateways                          | Y            |                                                                                       |
| Events         | None                                  | Y            |                                                                                       |
|                | Message                               | N            | FA does not support Message event.                                                    |
|                | Timer                                 | Y            |                                                                                       |
|                | Conditional                           | Y            | Most of this feature is supported. However, Conditional Start Event is not supported. |
|                | Link                                  | Y            |                                                                                       |
|                | Signal                                | Y            | It is recommended that signals are sent using BPMN Signal throw event.                |
|                | Error                                 | Y            |                                                                                       |
|                | Escalation                            | Y            |                                                                                       |
|                | Termination                           | Y            |                                                                                       |
|                | Compensation                          | Y            |                                                                                       |
|                | Cancel                                | Y            |                                                                                       |
| Multi-Instance | Multi-Instance Subprocess             | Y            |                                                                                       |
|                | Multi-Instance Call Activity          | Y            |                                                                                       |
|                | Multi-Instance Transaction Subprocess | Y            |                                                                                       |
|                | Multi-Instance Task                   | Y            |                                                                                       |

###Camunda Documentation Overview
The following is a brief survey of relevant Camunda documentation.

####[Camunda BPMN Tutorial](https://camunda.com/bpmn/)
This is a good starting point for those wishing to get familiar with BPMN

####[Camunda BPM Manual](${camundaManualBaseUrl})
This is a large set of documentation which makes up the Camunda BPM manual. Due to the way Flow Automation embeds and uses Camunda BPM some of the sections of the manual are more applicable than others. The following table discusses sections which have some relevance to FA SDK users.

| Section                                                 | Discussion                                                                                                                                                                                                                                                                                                                                                                                                      |
|---------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| User Guide - Process Engine - Process Variables         | Describes the concept of variables in processes, including topics such as scoping, visibility, supported variable value types, and input/output mapping.                                                                                                                                                                     |
| User Guide - Process Engine - Expression Language       | Camunda BPM supports a Unified Expression Language and allows useful small script-like expressions to be used in many places in the process model, eg. conditions examining variables, input/output mapping.                                                                                                                                                                                                    |
| User Guide - Process Engine - Scripting                 | Camunda BPM supports multiple script languages. Scripts can be used in many places in the process model, eg. behaviour in script tasks, conditions. Scripts can access and manipulate variables.                                                                                                                                                                                                                |
| User Guide - Process Engine - Transactions in Processes | While this is a quite technically-involved section it is essential that all Flow Designers are fully aware of how technical container transactions are handled and can be controlled. This fundamental topic is discussed in detail in the Transactions section of this SDK. Note that this should not to be confused with the BPMN Transaction construct which is a separate concept.                          |
| User Guide - Process Engine - The Job Executor          | Processes can be executed in threads in two distinct ways 1. Client threads, and 2. Camunda BPM Job Executor threads. The latter are used when processes are to be executed asynchronously in the background. This is the most common situation. This section explains the operation of the Job Executor. The only relevant topic is Concurrent Job Execution, including the subtopic Exclusive Jobs.           |
| User Guide - Process Engine - Error Handling            | Describes basic strategies to handle errors and exceptions within processes.                                                                                                                                                                                                                                                                                                                                    |
| User Guide - Data Formats                               | Explains how to work with data formats such as XML or JSON in Camunda BPM using Camunda Spin, a wrapper around well-known libraries for processing XML and JSON. Note that these are discussed in more detail in Camunda BPM Reference - Spin Dataformats.                                                                                                                                                      |
| User Guide - Data Formats - XML                         | Some details on using Spin for XML.                                                                                                                                                                                                                                                                                                                                                                             |
| User Guide - Data Formats - JSON                        | Some details on using Spin for JSON.                                                                                                                                                                                                                                                                                                                                                                            |
| Reference - Javadoc                                     | The vast majority of the APIs listed are not relevant. They contain Camunda BPM internals (for some reason Camunda do not filter these out) and also APIs which Flow Automation uses and are not exposed to Flow Designers. The key Camunda BPM API which is important for flow design is [DelegateExecution](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/DelegateExecution.html) and in particular the methods inherited from [VariableScope](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/VariableScope.html) which deal with variable handling. |
| Reference - BPMN 2.0                                    | This is one of the most important references for Flow Designers. The relevance of the individual sections is discussed above in [Flow Automation support of Camunda BPM features](#Flow_Automation_support_of_Camunda_BPM_features).                                                                                                                                                                                                    |
| Reference - Spin Dataformats                            | A detailed reference for using Spin for XML and JSON. Note that the sections on Mapping are not relevant since they related to mapping to custom Java objects which are not supported in Flow Automation.                                                                                                                                                                                                       |
| Installation - Camunda Modeler                          | Explains how to install the Camunda Modeler for modeling BPMN diagrams.                                                                                                                                                                                                                                                                                                                                         |
| Modeler - Camunda Modeler                               | A very basic user manual for Modeler.                                                                                                                                                                                                                                                                                                                                                                           |
| Web Applications - Cockpit                              | A useful GUI application for deep troubleshooting of deployed processes and process instance execution.                                                                                                                                                                                                                                                                                                         |

####[Camunda Best Practices](https://camunda.com/best-practices/_/)
There are quite a few best practices described. Due to the way Flow Automation embeds and uses Camunda BPM some of the sections of the manual are more applicable than others. The following table discusses sections which have some relevance to FA SDK users.

| Best Practice                         | Discussion                                                                                                                                                                                                                    |
|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Creating Readable Process Models      | All the recommendations are relevant. It is also important to emphasise the recommendation to avoid using lanes. These are useful for doing high level strategic modeling but are not useful when designing individual flows. |
| Naming BPMN Elements                  | All the recommendations are relevant.                                                                                                                                                                                         |
| Modeling with Situation Patterns      | Useful set of patterns. Any content relating to Message events is not relevant because these are not supported by Flow Automation.                                                                                            |
| Building Flexibility into BPMN Models | How to build flexibility into process models to deal with expected or unexpected operational problems or to allow for humans to intervene.                                                                                    |
| Modeling Beyond the Happy Path        | A good approach to building out the functionality in a flow, and ensuring that errors, exceptions, timeouts are considered and handled.                                                                                       |
| Dealing With Problems and Exceptions  | This describes the concepts of business and technical transactions and is essential reading for all Flow Designers.                                                                                                           |
