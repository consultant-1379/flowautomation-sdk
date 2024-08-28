#Concepts

---
This section describes the main Flow Automation concepts.

- [Flow Automation](#Flow_Automation)
- [Flow](#Flow)
- [Flow Instance ](#Flow_Instance)
- [Standard Flow Process Model](#Standard_Flow_Process_Model)
- [Flow Data Modeling](#Flow_Data_Modeling)
- [Flow User Interaction](#Flow_User_Interaction)
- [Flow Scripting](#Flow_Scripting)
- [Flow Execution Report](#Flow_Execution_Report)
- [Flow Building Blocks](#Flow_Building_Blocks)
- [Flow API](#Flow_API)

###Flow Automation

**Flow Automation** is a generic workflow-based automation framework and application. It allows automation designers to automate tasks through the use of Flows.

In addition, and in order to make automation design easy and efficient, Flow Automation provides a suite of components.  For example...

* Flow User Tasks which allow flows to interact with users
* Flow Execution Report which can present results of flow execution to users
* Flow Building Blocks which are high-value components which typically provide reusable functionality and integration between flows and external services and applications
* Flow API and Flow Java Libraries which can be used to implement low level logic in combination with Flow Scripting

The Flow Automation application provides the run-time for flow execution. North-Bound Interface (NBI) and Graphical User Interface (GUI)  support deployment, execution, and monitoring of flows.

The following diagram helps with understanding the context and relationships between the main concepts.

![](concepts.png)

###Flow 

A **Flow** is a sequence of steps and operations used to automate a use case. A Flow is defined by a collection of BPMN processes organized in a predefined folder structure and packaged as a zip file.

The purpose of this SDK is to enable an automation design engineer to quickly and efficiently design and package a flow.

A flow package can be versioned and imported to the Flow Automation application. 

A flow package consists of multiple artifacts...

* BPMN Process Models 
* Data Models defining inputs, User Task forms, Reports 
* Flow Scripts
* Flow Resources
* Internationalization data
* Flow metadata

A Flow can be executed by creating a Flow Instance.

###Flow Instance 

A **Flow Instance** is a running instance of the flow. There can be multiple flow instances of a flow executing at the same time. The relationship between Flow and Flow Instance is similar to that between a Class and an Object in Object Oriented programming.

A flow instance can have different states during execution, e.g. Started, Executing, Executed, Stopped, Failed etc. While executing,  a flow instance can be stopped. A flow instance is considered to be completed when it reaches Executed, Stopped or Failed state.

The data created by a flow instance (e.g. Report) is retained after completion for a period of time (default 7 days) after which it is deleted.

###Standard Flow Process Model

In order to provide a consistent user experience a **Standard Flow Process Model** is imposed on all flows. Typically a Flow Instance will have two main phases. The Setup Phase gathers inputs and configuration data and the Execute Phase performs the main behavior of the automated use case. The Setup Phase is optional.

The Standard Flow Process Model provides options for how a flow instance executes, how inputs are supplied, and which execution paths are taken.

The Standard Flow Process Model is implemented as a process model which wraps flow-specific setup and execution. This process model is created automatically by Flow Automation when a flow is deployed.

For a more detailed description see [Standard Flow Process Model](standard-flow-process-model/standard-flow-process-model.html).

###Flow Data Modeling

Data Models are used in Flow Automation for multiple purposes. For example..

* Flow input specification
* User Task models
* Report modelEE
* Building bock interface definition

All the data models are based on JSON Schema.

For more details see [Flow Data Modeling](flow-data-modeling/flow-data-modeling.html)

###Flow User Interaction

Flows can have User tasks that are intended to be completed by the user during the execution of the flow instance. These User tasks are rendered as HTML forms. These forms can be constructed with a comprehensive set of HTML form controls, e.g.  text box, checkbox, radio button etc.

In order to create these forms a JSON Schema based approach is supported

* The user provides a JSON Schema file describing the html controls that need to be created in the User Task form and the input and output process variables.
* The Flow Automation user interface uses the schema to render the User Task HTML forms. 
* There are rules and constraints for creating these schemas

When a user submits a User Task form the inputs supplied are validated and validation errors presented to the user.

Standard Flow Process Model contains some predefined user tasks which allow a standardized user experience to be imposed.

###Flow Scripting

High level logic is probably best represented using BPMN elements, However lower level logic can be conveniently represented using scripting languages. The underlying process engine supports multiple scripting languages. The most commonly used is Groovy.

The script code can be inline in the BPMN process model or in separate files. 

###Flow Execution Report

A Flow Instance can produce output which can be consumed via the Flow Automation user interface or via the Flow Automation NBI. This is called Flow Execution Report.

In order to create the report  a JSON Schema based approach is supported

* The user provides a JSON Schema file describing the report content and format. 
* The Flow Automation user interface uses the schema to render the report.
* There are rules and constraints for creating the report schema

The report is produced live during the execution of flow instance and its content can change. The report does not change after the execution of the flow instance is completed. The report is retained after completion for a period of time.

###Flow Building Blocks

A *Flow Building Block* is a mechanism for providing high value reusable logic which allows integration with external services and applications. For example...

* Disable/Enable alarm supervision
* Activate node software upgrade
* Execute  remote script

The interface to an FBB is defined using JSON Schema and provides an asynchronous request-response interaction pattern.

###Flow API

Several APIs are provided which can be invoked from flow script tasks.

* Reporter - Allows the flow to record the data which will be part of the Flow Execution Report
* Event Recorder - Allows the flow to record significant events during the execution of the flow
* Flow Package Resources - Allows the flow to access resources which are part of flow package
* Email Sender - Allows the flow to send emails