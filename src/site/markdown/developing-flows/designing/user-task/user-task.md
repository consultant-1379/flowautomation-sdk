#User Task

---
This section introduces User Task in Flow Automation. It describes what a User Task is, its use in the Flow and how a User Task is modeled.

- [User Task](#User_Task)
    - [Typical use of User Task in a Flow](#Typical_use_of_User_Task_in_a_Flow)
    - [Interfaces for interaction with User Tasks](#Interfaces_for_interaction_with_User_Tasks)
- [Modeling User Task](#Modeling_User_Task)
    - [Model User Task](#Model_User_Task)
    - [Design User Task data model - JSON Schema](#Design_User_Task_data_model_-_JSON_Schema)
    - [Configure User Task](#Configure_User_Task)

###User Task

A User Task is used to model work in the Flow that needs to be completed by the flow operator.

When the Flow Instance execution reaches a User Task in the Flow, the execution waits for the flow operator to interact with the User Task by providing input data and completing it.

The User Task is displayed as a form in Flow Automation UI. Flow Automation allows the Flow designer to model the User Task form using JSON schema. Flow automation supports many widgets that can be modeled in User Task's JSON schema. The schema is rendered as HTML controls by Flow Automation UI.

<br/>
A User Task in a Flow is modeled in BPMN as shown below

   ![](basic-user-task.png)

The form rendered by the FA UI from the [sdkBasicUserTaskFlowExample](../../../example-flows/sdkBasicUserTaskFlowExample/sdkBasicUserTaskFlowExample.html) flow is below

   ![](basic-user-task-preview.png)

<br/>
The form model for the above User Task is represented in JSON schema as shown below
######User Task JSON Schema

````json
    {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "type": "object",
      "description": "Basic User Task",
      "properties": {
        "basicUserTask": {
          "name": "Basic User Task",
          "type": "object",
          "properties": {
            "checkbox": {
              "name": "Check box",
              "description": "Select or Unselect the Check box",
              "type": "boolean",
              "format": "checkbox",
              "info": "This is a Check box"
            },
            "listbox": {
              "name": "List box",
              "description": "Only one item can be selected",
              "type": "string",
              "format": "select-list",
              "info": "This displays list of items. Single item can be selected in the list.",
              "enum": ["ITEM_1","ITEM_2","ITEM_3"],
              "enumNames": ["Item 1","Item 2","Item 3"],
              "default": "ITEM_1"
            }
          },
          "required": ["checkbox","listbox"],
          "additionalProperties": false
        }
      },
      "required": [ "basicUserTask"],
      "additionalProperties": false
    }
````
This form model has 2 widgets - Check box and List box with default value. Each widget describes its name, description, type, format, info and default properties which gets rendered as a HTML control. Some of the most common properties are described briefly here...

   * *name* property corresponds to label of the widget
   * *description* property corresponds to the content displayed when the user selects the ![](icon.png) icon
   * *type* property corresponds to type of data the widget supports
   * *format* property decides the actual widget that needs to be rendered on User Task's form  
   * *default* property corresponds to a default value that needs to presented when the widget is rendered
   
<br/>     
####Typical use of User Task in a Flow

A common pattern for designing a User Task in a Flow is

   ![](simple-user-task.png)
   
   * Prepare Input Data for the User Task. The Input Data is used to present default values for input controls in User Task's form on UI.
   * Configure the User Task with Input and Output parameters. The Input parameters correspond to User Task's form Input Data. The Output parameters correspond to User Task's form data submitted by the user.
   * Configure the User Task with JSON schema.
   * Validate the Output Data of the User Task. This Output Data corresponds to Output parameters of User Task. The Flow can use Output parameters to further validate and process it.

<br/>
The form rendered by UI with default values from the [sdkBasicUserTaskFlowExample](../../../example-flows/sdkBasicUserTaskFlowExample/sdkBasicUserTaskFlowExample.html) flow is below

   ![](simple-user-task-preview.png)
   
The form model for the above User Task as JSON schema is below
######User Task JSON Schema

````json
    {
      "$schema": "http://json-schema.org/draft-04/schema#",
      "type": "object",
      "description": "Simple User Task",
      "properties": {
        "simpleUserTask": {
          "name": "Simple User Task",
          "type": "object",
          "properties": {
            "note": {
              "name": "Note: First field is lower case and Second field is upper case. Values must match in both fields. Flow will validate it",
              "description": "Note",
              "type": "string",
              "format": "informational"
            },
            "parameter1DynamicDefault": {
              "name": "Text input 1 with default value",
              "description": "Minimum length is 4 and Maximum length is 40. Only lower case allowed",
              "type": "string",
              "pattern": "^[a-z]+$",
              "minLength": 4,
              "maxLength": 40,
              "schemaGen": {
                "type": "default",
                "binding": "variable:local:parameter1DynamicDefault"
              }
            },
            "parameter2DynamicDefault": {
              "name": "Text input 2 with default value",
              "description": "Minimum length is 4 and Maximum length is 40. Only upper case allowed",
              "type": "string",
              "pattern": "^[A-Z]+$",
              "minLength": 4,
              "maxLength": 40,
              "info": "",
              "schemaGen": {
                "type": "default",
                "binding": "variable:local:parameter2DynamicDefault"
              }
            }
          },
          "required": ["parameter1DynamicDefault","parameter2DynamicDefault"],
          "additionalProperties": false
        }
      },
      "required": [ "simpleUserTask"],
      "additionalProperties": false
    }
````
This example form model illustrates a few additional properties.

   * *minLength*, *maxLength*, *pattern* properties describe validation which will be performed by the UI form handling. Different validation properties apply to different widget types.
   * *schemaGen* property in the example above defines how default values will be set based on variables in the Flow. 

<br/>
After the completion of the User Task, the Flow may perform additional validation and can throw an error which will result an error message being displayed to the user in the form as shown below.
   
   ![](simple-user-task-error-preview.png)
   
<br/>
###Modeling User Task

Modeling the User Task involves following steps:

   * Model  User Task
   * Design User Task data model (JSON schema)
   * Configure User Task

These steps are now described.

####Model User Task

Camunda Modeler needs to installed before modeling the User Task. Refer to [Camunda Modeler](../../../getting-started/getting-started.html#step5) for installation procedure.

Launch Camunda Modeler once it is successfully installed. Follow the below steps to model a User Task in the Flow.

   * Drag and Drop the *Create Task* element from the available BPMN elements on *Left Panel*.
   * Click on the Task, then click on *Change type* and select *User Task*.
   * Click on the User Task and provide name for the User Task in the *Properties Panel→General* tab. An auto-generated value for *Id* field is assigned. Flow Automation doesn't support other properties in *Properties Panel→General* tab.

<br/>
A sample User Task in Camunda Modeler is shown below

   ![](user-task-modeling.png)
   
Flow Automation supports only configuration of *Properties Panel→Forms* and *Properties->Input/Output* tabs. The configuration of these tabs are explained later in below sections.

<br/>
####Design User Task data model (JSON schema)

The User Task data model is designed using JSON schema.
> *<font size="2">...Refer to [Flow Data Modeling](../../../concepts/flow-data-modeling/flow-data-modeling.html) for more details about JSON schema, tools to edit and validate the schemas.</font>*
  
The User Task schema contains widgets that needs to be rendered on Flow Automation UI. The properties from JSON schema are used to configure the *Properties Panel→Forms* and *Properties->Input/Output* tabs of the User Task in Camunda Modeler which will be explained in details in a following section.

<br/>
There are 2 main phases in a Flow - **Setup Phase** and **Execute Phase**. Flow Automation defines a flow package structure for each phase. All the BPMN files and User Task form JSON schema files must be placed in their respective *setup* and *execute* folders. The flow package structure for [sdkBasicUserTaskFlowExample](../../../example-flows/sdkBasicUserTaskFlowExample/sdkBasicUserTaskFlowExample.html) is given below

````
    |--flow-definition.json
    |-- execute
    |   `-- basicUserTask.json
    |   `-- simpleUserTask.json
    |   `-- sdkBasicUserTaskFlowExample-execute.bpmn
    |-- setup
    |   `-- flow-input-schema.json
    |   `-- userTaskUsingStandaloneSchemaInSetupPhase.json
    |   `-- sdkBasicUserTaskFlowExample-setup.bpmn
````

There are 2 options to model the User Task JSON schema.

   * Setup Phase User Task using **flow-input-schema.json**
   * Setup Phase and Execute Phase User Task using standalone schema

These are described below.

#####Setup Phase User Task using flow-input-schema.json

The user can provide inputs via User Tasks in Setup Phase. The *flow-input-schema.json* file constitutes a data model for the flow inputs which allows Flow Automation to structurally and syntactically validate the inputs. The schema describes the input forms which can be presented to the user during the Setup Phase. It is recommended that designer of the Flow ask the user for inputs in Setup Phase and validate them. The designer can model many User Tasks in *flow-input-schema.json*.

The output of each User Task is validated and accumulated by the Flow in a special variable called **flowInput**. When the Setup Phase is completed, a special **Review and Confirm Execute** task displays all the inputs provided by user through User Tasks in Setup Phase. When the user confirms the *Review and Confirm Execute* task, then the *flowInput* variable is passed to the Execute Phase. 

The schema is also used to validate inputs from NBI and the *File Input Setup* option.

It is highly recommended that the Setup Phase does not make any changes to external services or applications.

A typical User Task using *flow-input-schema.json* in Setup Phase is available in [sdkBasicUserTaskFlowExample](../../../example-flows/sdkBasicUserTaskFlowExample/sdkBasicUserTaskFlowExample.html).

#####Setup Phase and Execute phase User Task using standalone schema

A Flow designer can model User Task using a standalone schema file for User Tasks in Setup Phase and Execute Phase. The schema file is 'standalone' in the sense that the schema can only model one User Task.

The procedure to design the User Task model using standalone schema for Setup Phase and Execute Phase is the same. They differ when it comes to configuring the *Properties Panel→Forms* tab of the User Task in Camunda Modeler.

Note that the input data provided to a Setup Phase User Task using standalone schema is not available in *flowInput* variable, so it not dispayed in *Review and Confirm Execute* task.

Typical User Tasks using standalone schema in Setup Phase and Execute Phase are available in [sdkBasicUserTaskFlowExample](../../../example-flows/sdkBasicUserTaskFlowExample/sdkBasicUserTaskFlowExample.html).

<br/>
####Configure User Task

When the designing of data model for the User Task is done, it needs to be mapped to the User Task in Camunda Modeler. The procedure to configure the User Task in Camunda Modeler for Setup and Execute phases is explained below.

#####Setup Phase User Task using flow-input-schema.json

An example of a User Task using *flow-input-schema.json* in Setup Phase can be found in [sdkBasicUserTaskFlowExample](../../../example-flows/sdkBasicUserTaskFlowExample/sdkBasicUserTaskFlowExample.html)
   
The *Properties Panel→Forms* and *Properties->Input/Output* tabs of the above User Task in Camunda Modeler needs to configured with JSON properties from User Task JSON schema.

The *Properties Panel→Forms* tab has a *Form Key* property which is configured with information about the location of the User Task data model.

The *Properties->Input/Output* tab has *Input and Output Parameters* which represents the Input/Output mappings for the User Task.

The *flow-input-schema.json* for the example User Task and its relationship with Input/Output mappings, form key configuration of a User Task are explained below. The following diagram shows the Flow input schema and also the relevant properties to configure for the User Task in Modeler.

   ![](setup-phase-user-task-configuration.png)
   
The following key points explain the configuration of User Task properties...

   * The *flow-input-schema.json* mentioned above has defined a data model for a User Task named *User Task Using Flow Input Schema* which contains 3 widgets - *Text input with default value*, *Text input with dynamic default value*, and *Select box*.
   * Flow Automation uses the *Form Key* to identify the data model for the User Task in *flow-input-schema.json*. When a user creates a new Flow instance, the Flow Automation UI retrieves this model, uses it to render the User Task form, and control the data submitted to the flow instance when the user completes the User Task. The *Form Key* needs to be configured for each User Task. See *Form Key* configuration in the diagram. The Flow designer can configure the form key on a *Properties Panel → Forms* tab of a User Task in Camunda Modeler. The form key for Setup Phase User Task using *flow-input-schema.json* should be configured as *setup:User Task Top level JSON Property*. *Point 1* in the diagram illustrates this. Flow Automation doesn't support *Form Fields* in Forms tab.
   * The *Prepare Input Data* Script Task produces input data for the User Task. *Input Mapping* is shown in the diagram. The input data needs to be mapped to *Input Parameters* on a *Properties Panel → Input/Output* tab in Camunda Modeler. As shown in *Point 2*, the *Input Parameter Name - textInputWithDynamicDefault* is a JSON property from User Task data model in *flow-input-schema.json* which is mapped to *${textInputWithDynamicDefault}*. The *Prepare Input Data* Script Task produces  data for the placeholder *${textInputWithDynamicDefault}*. As shown in *Point 3*, the placeholder is mapped to a *variable:local:textInputWithDynamicDefault* using **schemaGen Data Binding**. Flow Automation looks for a local variable value for the variable named *textInputWithDynamicDefault* in the Flow Execution and binds the value to the default for JSON property *textInputWithDynamicDefault*, which in turn gets rendered on UI as default value for that widget.
   * The user can provide input data through the User Task form rendered on UI. Flow Automation performs schema validation for the user provided data against the User Task data model defined in *flow-input-schema.json* and completes the User Task. The data submitted by user needs to be mapped to *Output Parameters* on a *Properties Panel → Input/Output* tab in Camunda Modeler. This is shown in *Output Mapping* in the diagram. As shown in *Point 4*, the *Output Parameter Name* is *userTaskUsingFlowInputSchema* which is a top-level property in the input schema. As shown in *Point 5*, the *Output Parameter Value* has a placeholder variable which is same as *Output Parameter Name - userTaskUsingFlowInputSchema*. After successful completion of the User Task, the user-provided data for that User Task is available in *Output Parameter Name - userTaskUsingFlowInputSchema* as process variable in the Setup Phase process instance scope (meaning the Flow can simply refer to that variable by name).
   * The *Validate User Task Output* Script Task performs validation on user provided data of the previously completed and builds the *flowInput* variable. When the Flow encounters a validation error it can throw an error with a message to be displayed to the user.

The form rendered by UI with default values from the [sdkBasicUserTaskFlowExample](../../../example-flows/sdkBasicUserTaskFlowExample/sdkBasicUserTaskFlowExample.html) flow is shown below

   ![](setup-phase-user-task-preview.png)
   
<br/>
#####Setup phase and Execute phase User Task using standalone schema

An example of a User Task using standalone schema in Execute Phase is available in [sdkBasicUserTaskFlowExample](../../../example-flows/sdkBasicUserTaskFlowExample/sdkBasicUserTaskFlowExample.html).

The standalone schema for the Execute Phase User Task, its relationship with Input/Output mappings, and form key configuration of a User Task are explained below. The following diagram shows the standalone schema and also the relevant properties to configure for the User Task in Modeler.

   ![](simple-user-task-configuration.png)
    
The following key points explain the configuration of User Task properties

   * The standalone schema mentioned above has defined a data model for a User Task named *Simple User Task* in Execute Phase. The model contains 2 Text Input widgets with dynamic default values configured using *schemaGen Data Binding*. The schema is saved as *simpleUserTask.json*.
   * The *FormKey* for Setup Phase User Task using standalone schema should be configured as *setup:<standaloneschemafilename.json>*. The form key for Execute Phase User Task using standalone schema should be configured as *execute:<standaloneschemafilename.json>*. *Point 1* in the diagram illustrates this. The form key is configured as *execute:simpleUserTask.json*.
   * The *Prepare Input Data* Script Task produces input data for the User Task. *Input Mapping* is shown in the diagram. The input data needs to be mapped to *Input Parameters* on a *Properties Panel → Input/Output* tab in Camunda Modeler. As shown in *Point 2*, the *Input Parameter Name - parameter1DynamicDefault* is a JSON property from User Task data model in *simpleUserTask.json* which is mapped to *${parameter1DynamicDefault}*. The *Prepare Input Data* Script Task produces  data for the placeholder *${parameter1DynamicDefault}*. As shown in *Point 3*, the placeholder is mapped to a *variable:local:parameter1DynamicDefault* using *Data Binding*. Flow Automation looks for a value of *variable:local:parameter1DynamicDefault* in the Flow Execution and binds the value to JSON property *parameter1DynamicDefault*, which in turn gets rendered on UI as default value for that widget. The same explanation is applicable to *Input Parameter Name - parameter2DynamicDefault*.
   * The user can provide input data through the User Task form rendered on UI. Flow Automation performs schema validation for the user provided data against the User Task data model defined in *simpleUserTask.json* and completes the User Task. The data submitted by user needs to be mapped to *Output Parameters* on a *Properties Panel → Input/Output* tab in Camunda modeler. This is shown in *Output Mapping* in the diagram. As shown in *Point 4*, the *Output Parameter Name - simpleUserTask* is *User Task Top level JSON Property*. As shown in *Point 5*, the *Output Parameter Value* has a placeholder variable which is same as *Output Parameter Name - simpleUserTask*. After successful completion of the User Task, the user provided data for that User Task is available in *Output Parameter Name - simpleUserTask* as process variable in the Setup Phase process instance scope.
   * The *Validate User Task Output* Script Task performs validation on user-provided data of the previously completed. When Flow encounters a validation error it can throw an error with a message to be displayed to the user.
