# Flow Data Modeling

---

Flow Automation is a highly model-driven application and framework. Flows are models. In addition, the data passed to and from flow instances on FA interfaces is modeled. Data models used in flows are defined using [JSON Schema](https://json-schema.org/).

This section introduces the use of data modeling in FA. More detailed descriptions of data modeling can be found in specific sections of the SDK where data models are used.

* [Overview](#Overview)
* [JSON Schema](#JSON_Schema)

### Overview
Flow instance data passed on FA interfaces is modeled. Data models allows FA to automatically validate data passed on these interfaces syntactically and structurally.

Data models are used for several features...

* Flow input specification
* Usertask forms
* Flow Execution Report
* Building Block input/output specification


Note that in the cases where data is received on input interfaces flows can apply additional semantic validation to input data which cannot be easily expressed in the data models.

Note also that data within the flow is not modeled, which means that [process variables](https://docs.camunda.org/manual/7.14/user-guide/process-engine/variables/) which are managed as part of process instance state are not modeled by FA.



The following diagram illustrates the use of data models on FA flow instance interfaces.

![](DataModels.png)

Flow inputs are modeled using the flow input schema. If necessary, NBI client applications can retrieve this schema to discover the interface to a flow. Currently this is not performed by any applications. Rather, the flow input specification is used by FA to validate the flow input data which is supplied when a flow instance is created. FA performs syntactic and structural validation using a JSON Schema validator which is part of FA.

The FA UI can present usertask forms to users when flow instances are executing. These forms are modeled using usertask schemas. When the user completes a usertask the data is submitted to FA which validates the data using the usertask schema.

FA can present a report for a flow instance. The report is modeled using a schema. The schema can be retrieved by an application. For example the FA UI retrieves the report schema so that it knows the structure and format of the report data which is separately retrieved.

Building Blocks interfaces are specified using schemas. FA has no prior knowledge of any BBs and only becomes aware of a BB when the BB registers with FA. When a BB registers it sends its input and output schema in the registration.

### JSON Schema
Data models used in flows are defined using JSON Schema. The version of JSON Schema supported is Draft 04. The specification is in 2 parts and can be found at

* JSON Schema: core definitions and terminology

* JSON Schema: interactive and non interactive validation


FA places some key restrictions on the use of JSON Schema for its models. FA also extends JSON Schema. These restrictions and extensions are described in the specific sections of the SDK where data models are used.



JSON Schema is a well-understood and widely-used technology. There are many useful tools to assist with editing and validating schemas. One particularly useful one is [jsonschemavalidator.net](https://www.jsonschemavalidator.net/)

The following screenshot shows this tool being used to validate data against a flow input schema.


![](JSONSchema.png)

> *<font size="2">...For illustration purposes this section uses the [sdkBasicFlowExample](../../example-flows/sdkBasicFlowExample/sdkBasicFlowExample.html)</font>*