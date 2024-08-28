#Variables
---

Variables, in the context of Flows, refer to data which is persisted with the Flow state. This section describes how variables are supported in Flow Automation. Note that the term variable is synonymous with the Camunda BPM term [Process Variable](${camundaManualBaseUrl}/user-guide/process-engine/variables/)

Flows are classless and do not allow custom Java objects to be used as variables. Consequently, this has an impact on the types of variables which are supported.

Variables can be created, accessed, modified, and removed in several ways. Their scope and visibility can be controlled. Care must be taken in the handling of variables to ensure correct concurrent execution within a Flow instance.

- [Flows are Classless](#Flows_are_Classless)
- [Process Variables](#Process_Variables)
- [Working with Variables](#Working_with_Variables)
    - [Creating Variables](#Creating_Variables)
    - [Accessing Variables](#Accessing_Variables)
    - [Modifying Variables](#Modifying_Variables)
    - [Removing Variables](#Removing_Variables)
    - [Passing Variables](#Passing_Variables)
    - [Scoping Variables](#Scoping_Variables)
    - [Concurrent variable modification](#Concurrent_variable_modification)

> *<font size="2">...The [Variable and Data Showcase example flow](../../../example-flows/variableAndDataShowcase/variableAndDataShowcase.html) demonstrates many of the aspects described here</font>*

###Flows are Classless
Flows can be lifecycle-managed independently of the FA application. This means that it is possible to deploy and undeploy Flows without needing to perform upgrade of FA or system software. This then allows Flows to be implemented and deployed by different organisations including PDUs, CUs, Services and customers.

In order to make this possible Flows need to be classless. This means that the Flow package contains no Java classes. All Flow logic is implemented using BPMN and scripting.

The significance of the classless nature of Flows is that some of the options and data types supported by Camunda BPM for handling process variables are not supported by FA. However, this constraint is relatively minor, and many variable type and handling options are still supported.

###Process Variables
The Camunda BPM [Process Variables](${camundaManualBaseUrl}/user-guide/process-engine/variables/) documentation is mostly applicable to FA SDK. The parts which are not applicable are the use of custom Java objects in the Java Object and Typed Value APIs. This is due to the fact that Flows are classless.

Supported variable types are described in Camunda BPM [Supported Variable Values](${camundaManualBaseUrl}/user-guide/process-engine/variables/#supported-variable-values). These are

- Primitive types - All are supported by FA. The Camunda BPM 4000 character limit for string length applies.
- File type - Supported via [FileValue](${camundaManualBaseUrl}/user-guide/process-engine/variables/#file-values) as part of the [Typed Value API](${camundaManualBaseUrl}/user-guide/process-engine/variables/#typed-value-api).
- Object type - FA only supports Java object types (classes) which are supported by Java libraries which are provided by FA runtime. This would include the Java 8 API and libraries mentioned in other parts of this SDK. It is highly recommended that Flows only use the following Java object types for variables
    - Primitive wrapping classes (eg. Integer, String)
    - Collection (eg. List) containing supported object types
    - Map whose entries are supported object types
The Camunda BPM 4000 character limit for string length applies. However, larger strings can be stored as an element in a Collection or an entry in a Map, and there is no practical limit on the size of a Collection or Map.

[Transient variables](${camundaManualBaseUrl}/user-guide/process-engine/variables/#transient-variables) are supported, but only for the supported variables types listed above.

###Working with Variables
####Creating Variables
Variables can be created in multiple ways...

- Supply 'file input' when creating a new Flow instance via FA GUI.
- Supply inputs interactively via User Tasks in Setup Phase when creating a new Flow instance via FA GUI.
- Supply inputs interactively via User Tasks in Execute Phase.
- Scripts in Script Tasks.
- Scripts in other parts of the process model (eg. output mapping)
- [Input/Output mapping](${camundaManualBaseUrl}/user-guide/process-engine/variables/#input-output-variable-mapping)
- [Call Activity inputs](${camundaManualBaseUrl}/reference/bpmn20/subprocesses/call-activity/#passing-variables)

####Accessing Variables
Variables can be accessed in multiple ways...

- [Scripts in Script Tasks](${camundaManualBaseUrl}/user-guide/process-engine/scripting/#variables-available-during-script-execution). Scripts access variables using the set and get methods of [DelegateExecution](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/DelegateExecution.html) API and in particular the methods inherited from [VariableScope](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/VariableScope.html) which deal with variable handling.
- Scripts in other parts of the process model
- [Expressions](${camundaManualBaseUrl}/user-guide/process-engine/expression-language/) in the process model

####Modifying Variables
Variables can be modified via...

- Scripts in Script Tasks

####Removing Variables
Variables can be modified via...

- Scripts in Script Tasks

####Passing Variables
Using [Input/Output mapping](${camundaManualBaseUrl}/user-guide/process-engine/variables/#input-output-variable-mapping) variables can be passed as inputs and outputs of...

- Script Tasks
- User Tasks
- Building Blocks
- Flow API Service Tasks

In addition Call Activities support a flexible combination of direct [variable passing](${camundaManualBaseUrl}/reference/bpmn20/subprocesses/call-activity/#passing-variables) and [input/output mapping](${camundaManualBaseUrl}/reference/bpmn20/subprocesses/call-activity/#combination-with-input-output-parameters).

####Scoping Variables
The concepts of variable scope and visibility are fundamental to Camunda BPM process variables and all Flow Designers should be very familiar with these concepts. In particular, the distinction between non-local variables and local variables should be well understood. These concepts are described in [Variable Scopes and Visibility](${camundaManualBaseUrl}/user-guide/process-engine/variables/#variable-scopes-and-variable-visibility). FA supports them fully.

####Concurrent variable modification
Care must be taken to ensure that concurrent modification of variables is handled correctly. The following topics are relevant in this regard. These are quite complex topics with which all Flow Designers must become very familiar.

- [Transactions in Processes](${camundaManualBaseUrl}/user-guide/process-engine/transactions-in-processes/)
- Best Practice - [Understanding Transactions in Processes](https://camunda.com/best-practices/dealing-with-problems-and-exceptions/#transactions-in-processes)
- Best Practice - [Demarcating Custom Transaction Boundaries](https://camunda.com/best-practices/dealing-with-problems-and-exceptions/#additional-save-points)
