#Standard Flow Process Model

---

In order to provide a consistent user experience a Standard Flow Process Model is imposed on all flows. The Standard Flow Process Model provides options for how a flow instance executes, how inputs are supplied, and which execution paths are taken. The Standard Flow Process Model is implemented as a process model which wraps flow-specific setup and execution. This 'wrapper process' is created automatically by Flow Automation when a flow is deployed.

This section describes this process model.

* [Wrapper Process BPMN Model](#Wrapper_Process_BPMN_Model)
* [Setup and Execute Subprocesses](#Setup_and_Execute_Subprocesses)
* [Exposure of Process Model on Flow Automation UI](#Exposure_of_Process_Model_on_Flow_Automation_UI)
* [Execution Paths](#Execution_Paths)
   * [Execution Path: NBI and FA UI user executes flow which takes no inputs](#Execution_Path:_NBI_and_FA_UI_user_executes_flow_which_takes_no_inputs)
   * [Execution Path: NBI executes flow which takes inputs](#Execution_Path:_NBI_executes_flow_which_takes_inputs)
   * [Execution Path: FA UI user executes flow, supplies all inputs via file input](#fileInput)
   * [Execution Path: FA UI user executes flow, supplies all inputs via interactive input forms](#allInput)
   
###Wrapper Process BPMN Model
The following diagram shows the BPMN for the wrapper process.

![](WrapperProcessBPMNModel.png)

There are 2 main phases - Setup and Execute.

The setup phase is optional, meaning a flow does not need to have this phase. This can be seen in the first gateway in the diagram called 'Do you want Setup phase'. If the designer of the flow does not supply a setup phase process model this gateway will branch around the setup phase and proceed directly to the execute phase. The Flow Automation UI will not prompt the user for any inputs when a flow instance is created. The designer of the flow may ask the user for inputs in the execute phase, but generally it is recommended that inputs are gathered and validated using a setup phase. It is also highly recommended that the setup phase does not make any changes to external services or applications. After all, the user may decide to abandon a flow instance before the execute phase has been reached.

During the gathering and validation of inputs the setup subprocess builds a special variable called flowInput. A JSON schema must be defined by the flow designer. This schema constitutes a data model for the flow inputs which allows Flow Automation to structurally and syntactically validate the inputs. The schema also describes the input forms which can be presented to the user during the setup phase.

The flowInput variable is then passed to the execute phase.

###Setup and Execute Subprocesses
The flow designer must provide an execute phase subprocess. The following diagram illustrates how the setup and execute phase subprocesses are executed by the wrapper process. The Setup call activity invokes the setup subprocess (if provided) and the Execute call activity invokes the mandatory execute subprocess.

![](SetupandExecuteSubprocesses.png)

> *<font size="2">...This example flow can be found at [sdkBasicFlowExample](../../example-flows/sdkBasicFlowExample/sdkBasicFlowExample.html)</font>*

This example setup phase subprocess model shows 2 processing paths - one to handle the case where all the inputs have been supplied when the flow instance was created (upper path), and the other to handle the case where the inputs are supplied by the user via User Task input forms (lower path). The flow wrapper process and this example setup process will be used throughout this section to illustrate specific details.

![](SetupPhaseSubprocessModel.png)

###Exposure of Process Model on Flow Automation UI
When designing flows it is important to understand the Standard Flow Process Model and it's relationship to the setup and execute phase subprocesses. This is especially important when it comes to troubleshooting flow execution behaviour.

The Flow Automation UI provides a visual representation of the execution and progress of the executing and executed flow instance. The following diagrams show how this is presented in the UI. The Progress Diagram tab can show the definition of the flow in form of BPMN diagram. The activity of the instance can also be shown.

![](ProcessModelOnFAUI.png)

The user can navigate down and up the process instance hierarchy. In the following diagram the user has drilled down into the Setup subprocess.

![](ProcessDiagramDrillDown.png)

###Execution Paths
Flow instances can be created and interacted with via the FA NBI and the FA UI. Flow setup input can be provided in a number of different ways.

* FA NBI client application provides setup input when flow instance is created.
* FA UI user provides setup input in a single file.
* FA UI user provides setup input interactively via one or more user tasks

These options are all supported by the Standard Flow Process Model. It is helpful to explain these options in terms of 'execution paths' through the process model.

#### Execution Path: NBI and FA UI user executes flow which takes no inputs
A flow which has no need for inputs does not have a setup phase. Consequently the execution bypasses that part of the wrapper process.

![](NBIandFAUIUserExecutesFlowWhichTakesNoInputs.png)

####Execution Path: NBI executes flow which takes inputs
An NBI client application can execute a flow which requires inputs. These inputs are supplied in the execute request. The flow-specific setup phase is entered, the supplied setup data is syntactically and structurally validated by FA against the flow input schema, and the data is then symantically validated by the setup phase subprocess. The execution then proceeds to the execute phase.

![](NBIExecutesFlowWhichTakesInputs.png)
<a name="fileInput" />
####Execution Path: FA UI user executes flow, supplies all inputs via file input
An FA UI user can execute a flow which requires inputs. These inputs are supplied in this scenario via a file which is entered via the Choose Setup user task dialogue. The supplied setup data is syntactically and structurally validated by FA against the flow input schema, and the data is then symantically validated by the setup phase subprocess. Subsequently the Review and Confirm Execute user task dialogue is presented to the user allowing the user to check the supplied inputs before proceeding to the execute phase.

![](FAUIUserExecutesFlowSuppliesAllInputsViaFileInput.png)
<a name="allInput" />
####Execution Path: FA UI user executes flow, supplies all inputs via interactive input forms
An FA UI user can execute a flow which requires inputs. These inputs are supplied in this scenario via interactive user task input forms. In this scenario, in addition to providing the input data model the flow input schema provides the definition of the user task forms. Subsequently the Review and Confirm Execute user task dialogue is presented to the user allowing the user to check the supplied inputs before proceeding to the execute phase.

![](AllInputs.png)