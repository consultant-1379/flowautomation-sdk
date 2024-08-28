#variableAndDataShowcase
---

This example flow demonstrates many aspects of variable handling. The description below can be read in combination with the SDK section on [Variables](../../developing-flows/designing/variables/variables.html).

*(download flow package… [variableAndDataShowcase-1.0.1.zip](variableAndDataShowcase-1.0.1.zip))*

- [Setup phase](#Setup_phase)
    - [Overview](#Overview)
    - [Flow input data model](#Flow_input_data_model)
    - [Input Data User Task](#Input_Data_User_Task)
    - [Input Data Validation](#Input_Data_Validation)
    - [Adding visible modeled data](#Adding_visible_modeled_data)
    - [Adding non-visible non-modeled data](#Adding_non-visible_non-modeled_data)
    - [Review and Confirm Execute](#Review_and_Confirm_Execute)
- [Execute phase](#Execute_phase)
    - [Showcase variables](#Showcase_variables)
    - [Showcase input output mapping](#Showcase_input_output_mapping)
    - [Showcase call activity variable passing](#Showcase_call_activity_variable_passing)
    - [Showcase scoping](#Showcase_scoping)
    - [Showcase multi-instance](#Showcase_multi-instance)

##Setup phase
###Overview
The following BPMN diagram shows the setup phase process. Note that the overall Flow process model which executes the setup phase process is described in the [Standard Flow Process Model](../../concepts/standard-flow-process-model/standard-flow-process-model.html)

![](setup-process.png)

The following points summarise the behaviour of this process...

- File input (top branch) and interactive input (bottom branch) options are supported as is the norm for all Flows.
- The file input is automatically validated using the flow input data model (performed before the setup phase process is executed) and the special flowInput variable created.
- The interactive input presents a User Task called Input Data which gathers inputs from the user. The flow-input-schema.json schema defines the flow input data model.
- For interactive input, the setup phase process must construct the flowInput variable contents by adding the necessary properties. These properties are typically produced by User Tasks.
- Both file and interactive input processing call a common reusable script task to perform additional validation on the input.
- If the validation fails for file input option an error is thrown which will fail the Flow Instance.
- If the validation fails for interactive input option an error is thrown by a script. This rolls back the execution to the User Task, and displays the error to the user. This allows the user to re-enter correct data.
- Additional data is added to the flow input which is derived from the initial input. This will be 'visible' in the sense that it will be visible in the Review and Confirm Execution User Task which will appear after the setup process instance completes. This data is modeled in the input data model.
- More data is added to the flow input which is not visible in the Review and Confirm Execution User Task. This is not part of he formal input interface of the flow and consequently is not modeled. Only modeled data will appear in the Review and Confirm Execution User Task.

The following sections describe in behaviour and variable handling in more detail.

###Flow input data model
The *flow-input-schema.json* data model is shown here, and discussed below.

```json
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "properties": {
    "inputData": {
      "name": "Input Data",
      "type": "object",
      "properties": {
        "booleanInput": {
          "name": "Boolean Input",
          "type": "boolean",
          "format": "checkbox",
          "default": false
        },
        "integerInput": {
          "name": "Integer Input",
          "type": "integer",
          "default": 0
        },
        "stringInput": {
          "name": "String Input",
          "type": "string",
          "default": ""
        },
        "dateTimeInput": {
          "name": "Date Time Input",
          "type": "string",
          "format": "date-time"
        },
        "fileInput": {
          "name": "File Input",
          "type": "object",
          "info": "File must contain a JSON object with 'foo' key",
          "format": "file",
            "properties": {
              "fileName": {
                "type": "string"
              }
            },
          "required": [ "fileName" ]
        }       
      },
      "required": [ "booleanInput", "integerInput", "stringInput", "dateTimeInput", "fileInput" ],
      "additionalProperties": false 
    },
    "visibleDerivedData": {
      "name": "Visible Derived Data",
      "type": "object",
      "properties": {
        "fooContents": {
          "name": "Foo Contents",
          "type": "string",
          "readOnly": true,
          "schemaGen": {
            "type": "default",
            "binding": "variable:local:fooContents"
          }
        }
      },
      "required": [ "fooContents" ],
      "additionalProperties": false 
    }   
  },
  "required": [ "inputData" ],
  "additionalProperties": false 
}
```

There are 2 modeled properties - *inputData* and *visibleDerivedData*. The first is used as the schema for the Input Data User Task. The second is data which is derived from the input data and cannot be directly set by the user - note the use of *readOnly* property.

###Input Data User Task
When the interactive input option is taken the following User Task is presented. The correspondence between the labels and input controls and the properties in the input data model shown above should be obvious.

![](input-data-form.png)

###Input Data Validation
The flow input data model allows for a significant amount of validation, in particular syntax, format, and legal input values. However additional validation, usually semantic in nature, may need to be applied in the flow.

The script which performs this validation is configured as an External Resource script as shown below. This is a reusable script which can be used in elsewhere in the process model - specifically, it is used for both file input and interactive input validation.

![](validate-input-data-properties.png)

<br/>
Input and output parameters are passed to this script using Input/Output mapping.

![](validate-input-data-io-mapping.png)

<br/>
The validation script *validateInputData.groovy* is shown below. In this example flow, the expectation is that the *fileInput* should be a JSON file with specific contents, in particular that there is a 'foo' property. This is the semantic validation applied by the flow.

```groovy
import groovy.json.JsonSlurper
import com.ericsson.oss.services.flowautomation.flowapi.EventRecorder
 
def inputData = execution.getVariableLocal("inputData")
def validationResult = [:]
validationResult.isValid = true
 
def fileContentsText = inputData.fileInput.value.text
def fileContentsMap = new JsonSlurper().parseText(fileContentsText)
 
if (!fileContentsMap.containsKey('foo')) {
  def errorMessage = "The file must contain a JSON object with 'foo' key"
  validationResult.isValid = false
  validationResult.message = errorMessage
  EventRecorder.error(execution, errorMessage)
}
 
execution.setVariableLocal("validationResult", validationResult)
```

Note the use of local input variables which are produced by the input mapping and the use of local output variables which are used by the output mapping to create the validationResult variable in the calling execution. This is the recommended approach for passing variables to and from reusable activities such as script tasks and building blocks. The reasons for this are provided later in [Showcase input output mapping](#Showcase_input_output_mapping)).

<br/>
The validation result is examined using an expression which references the *validationResult* variable.

![](validation-result-expression.png)

<br/>
For the interactive input option the input data must be explicitly added to the special *flowInput* variable (a Map). Note that this is not necessary for the file input option which automatically creates the *flowInput* variable when the data is structurally validated against the flow input model.

![](set-flow-input.png)
```groovy
flowInput.inputData = inputData
```

###Adding visible modeled data
The *visibleDerivedData* property which can be seen above in the flow input data model represents data which the flow derives from the input data and which will be presented to the user in the Review and Confirm Execute User Task.

![](add-visible-derived-data.png)
```groovy
import groovy.json.JsonSlurper
 
def fileContentsText = flowInput.inputData.fileInput.value.text
def fileContentsMap = new JsonSlurper().parseText(fileContentsText)
 
def fooContents = "Supplied JSON file 'foo' property with value - " + fileContentsMap.foo
def visibleDerivedData = [:]
visibleDerivedData.fooContents = fooContents
 
flowInput.visibleDerivedData = visibleDerivedData
```

###Adding non-visible non-modeled data
Flows may wish to derive additional data to be passed to the execute phase but not wish to show to users. This data is not modeled in the input data model.

![](add-nonvisible-derived-data.png)
```groovy
def nonVisibleDervivedData = [:]
nonVisibleDervivedData.someProperty = "some value"
 
flowInput.nonVisibleDervivedData = nonVisibleDervivedData
```

###Review and Confirm Execute
Here we see the input data and derived data being presented to the user for confirmation.

![](review-and-confirm-execute.png)

<br/>
##Execute phase
The following BPMN diagram shows the execute phase process. Note that the overall Flow process model which executes the execute phase process is described in the [Standard Flow Process Model](../../concepts/standard-flow-process-model/standard-flow-process-model.html)

![](execute-process.png)

This process calls subprocesses which in turn demonstrate key aspects of variable handling. Each are described in turn in the following sections. 

###Showcase variables
The following BPMN diagram shows the *Showcase variables* subprocess.

![](showcase-variables-process.png)

The top row of the process model demonstrates basic operations with all variable types supported by Flow Automation. The bottom row demonstrates the use of transient variables.

These are now described in more detail.

<br/>
The following task shows how variables are created using set methods of the [DelegateExecution](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/DelegateExecution.html) API and in particular the methods inherited from [VariableScope](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/VariableScope.html) which deal with variable handling. File variables are created using the [Typed Value API](${camundaManualBaseUrl}/user-guide/process-engine/variables/#typed-value-api).

![](create-all-variable-types.png)
```groovy
import org.camunda.bpm.engine.variable.Variables
 
execution.setVariable("booleanVar", true)
execution.setVariable("bytesVar", (byte[])[1,2,3,4,5])
execution.setVariable("shortVar", (short)42)
execution.setVariable("intVar", (int)442)
execution.setVariable("longVar", (long)4442)
execution.setVariable("doubleVar", (double)4.2)
execution.setVariable("dateVar", new Date())
execution.setVariable("stringVar", "foo")
execution.setVariable("mapVar", [key1: "val1", key2: "val2"])
execution.setVariable("listVar", [5,6,7,8])
execution.setVariable("fileVar", Variables.fileValue("myfile.txt").file("hello".bytes))
```

<br/>
The following task shows how variables can be access in script tasks using the names of the variables. Alternatively, the get methods of the [DelegateExecution](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/DelegateExecution.html) API can be used. File values are retrieved using the [Typed Value API](${camundaManualBaseUrl}/user-guide/process-engine/variables/#typed-value-api).

![](access-variables.png)
```groovy
assert booleanVar == true
assert bytesVar == (byte[])[1,2,3,4,5]
assert shortVar == 42
assert intVar == 442
assert longVar == 4442
assert doubleVar == 4.2
assert dateVar.getYear() != 0
assert stringVar == "foo"
assert mapVar.size() == 2
assert listVar[0] == 5
 
def fileVar = execution.getVariableTyped("fileVar")
assert fileVar.getValue().text == "hello"
```

<br/>
The following task shows how variables are modified using set methods of the [DelegateExecution](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/DelegateExecution.html) API and the [Typed Value API](${camundaManualBaseUrl}/user-guide/process-engine/variables/#typed-value-api).

![](modify-variables.png)
```groovy
import org.camunda.bpm.engine.variable.Variables
 
execution.setVariable("booleanVar", !booleanVar)
 
bytesVar[0] = (byte)42
execution.setVariable("bytesVar", bytesVar)
 
execution.setVariable("shortVar", shortVar+1)
execution.setVariable("intVar", intVar+1)
execution.setVariable("longVar", longVar+1)
execution.setVariable("doubleVar", doubleVar+1)
 
dateVar.setYear(0)
execution.setVariable("dateVar", dateVar)
 
execution.setVariable("stringVar", stringVar+"bar")
 
mapVar.put("key3", "val3")
execution.setVariable("mapVar", mapVar)
 
listVar[0] = 43
execution.setVariable("listVar", listVar)
 
def fileVar = execution.getVariableTyped("fileVar")
def newFileVar = Variables.fileValue(fileVar.getFilename()).file("goodbye".bytes)
execution.setVariable("fileVar", newFileVar)
```

<br/>
The following task simply reads the variables and checks for expected values.

![](check-variables-modified.png)
```groovy
assert booleanVar == false
assert bytesVar == (byte[])[42,2,3,4,5]
assert shortVar == 43
assert intVar == 443
assert longVar == 4443
assert doubleVar == 5.2
assert dateVar.getYear() == 0
assert stringVar == "foobar"
assert mapVar.size() == 3
assert listVar[0] == 43
 
def fileVar = execution.getVariableTyped("fileVar")
assert fileVar.getValue().text == "goodbye"
 
execution.setVariable("removeVariables", true)
```

<br/>
The following shows how a variable can be accessed in an expression.

![](remove-variables-expression.png)

<br/>
The following task shows how variables can be removed.

![](remove-variables.png)
```groovy
execution.removeVariable("booleanVar")
execution.removeVariable("bytesVar")
execution.removeVariable("shortVar")
execution.removeVariable("intVar")
execution.removeVariable("longVar")
execution.removeVariable("doubleVar")
execution.removeVariable("dateVar")
execution.removeVariable("stringVar")
execution.removeVariable("mapVar")
execution.removeVariable("listVar")
execution.removeVariable("fileVar")
```

<br/>
The following task accesses variables using the get methods of the [DelegateExecution](${camundaJavadocBaseUrl}/org/camunda/bpm/engine/delegate/DelegateExecution.html) API.

![](check-variables-do-not-exist.png)
```groovy
assert execution.getVariable("booleanVar") == null
assert execution.getVariable("bytesVar") == null
assert execution.getVariable("shortVar") == null
assert execution.getVariable("intVar") == null
assert execution.getVariable("longVar") == null
assert execution.getVariable("doubleVar") == null
assert execution.getVariable("dateVar") == null
assert execution.getVariable("stringVar") == null
assert execution.getVariable("mapVar") == null
assert execution.getVariable("listVar") == null
assert execution.getVariable("fileVar") == null
```

Note that it would not be possible to check for existence by simply referring the variable name directly, ie. the following would not work...
```groovy
assert stringVar == null   // !!!! this would not work
```

<br/>
The following task shows how [transient variables](${camundaManualBaseUrl}/user-guide/process-engine/variables/#transient-variables) can be created using the [Typed Value API](${camundaManualBaseUrl}/user-guide/process-engine/variables/#typed-value-api).

![](create-transient-variable.png)
```groovy
import org.camunda.bpm.engine.variable.Variables
 
def transientStringValue = Variables.stringValue("some value", true)
execution.setVariable("transientStringValue", transientStringValue)
```

<br/>
The following task shows how [transient variables](${camundaManualBaseUrl}/user-guide/process-engine/variables/#transient-variables) can be retrieved using the [Typed Value API](${camundaManualBaseUrl}/user-guide/process-engine/variables/#typed-value-api).

![](access-transient-variable.png)
```groovy
def transientStringValue = execution.getVariableTyped("transientStringValue")
assert transientStringValue.getValue() == "some value"
```

In order to demonstrate the transience of this variable it is necessary to create a [transaction boundary](${camundaManualBaseUrl}/user-guide/process-engine/transactions-in-processes/#transaction-boundaries) by applying an [asynchronous continuation](${camundaManualBaseUrl}/user-guide/process-engine/transactions-in-processes/#asynchronous-continuations)

![](commit-transaction-async-continuation.png)

This then has the effect of discarding the transient variable, ie. it is not persisted to the database. This is proved by the following task.

<br/>
![](check-transient-variable-does-not-exist.png)
```groovy
assert execution.getVariableTyped("transientStringValue") == null
```

###Showcase input output mapping
The following BPMN diagram shows the *Showcase input/output mapping* subprocess.

![](showcase-input-output-mapping.png)

The following points summarise the behaviour of this process...

- An integer variable is initialised to a number value
- A reusable script task is called which squares the number
- The reusable script task is called again with the previous result
- The final result is checked

[Input/Output mapping](${camundaManualBaseUrl}/user-guide/process-engine/variables/#input-output-variable-mapping) was already encountered and described briefly earlier in the setup phase [Input Data Validation](#Input_Data_Validation) section. In this section a more detailed example is provided.

Input mapping takes variables in the calling activity and produces local input variables in the called activity. Output mapping takes local output variables in the called activity and produces variables in the calling activity. This is the recommended approach for passing variables to and from reusable activities such as script tasks and building blocks. The reasons for this recommendation are

- to limit the scope of any variables created by reusable activities
- to encourage the use of variable names which make sense for the flow, and map these to the output variables produced by the reusable task which may have been named with different considerations in the mind of the author of the reusable task.

<br/>
An integer variable is initialised to a number value.

![](initialise-variable-iomapping.png)
```groovy
execution.setVariable("initialInputValue", 2)
```

<br/>
A reusable script task is called which squares the number. 

![](execute-square-script-properties.png)

<br/>
Input and output variables are mapped.

![](execute-square-script-io-mapping.png)

<br/>
The following reusable script task script *square.groovy* expects to receive local variable and produces a local variable
```groovy
def inputValue = execution.getVariableLocal("inputValue")
execution.setVariableLocal("outputValue", inputValue * inputValue)
```

<br/>
The input variable is mapped.

![](square-script-input-mapping.png)

<br/>
The output variable is mapped.

![](square-script-output-mapping.png)

<br/>
The final result is checked.

![](check-result-io-mapping.png)
```groovy
assert result2 == 16
```

###Showcase call activity variable passing
The following BPMN diagram shows the *Showcase call activity variable passing* subprocess.

![](showcase-call-activity-variable-passing-process.png)

The top row of the process model demonstrates basic variable passing to [Call Activity](${camundaManualBaseUrl}/reference/bpmn20/subprocesses/call-activity/). The following points summarise the process behaviour...

- An integer variable is initialised to a number value
- A reusable call activity is called which squares the number
- The result is checked

<br/>
The bottom row of the process model demonstrates the more complex topic of variable passing in [combination with input/output mapping](${camundaManualBaseUrl}/reference/bpmn20/subprocesses/call-activity/#combination-with-input-output-parameters). This is essential to handle the scoping of the variables on each of the 2 parallel branches to avoid concurrent modification problems. The following points summarise the process behaviour...

- An integer variable is initialised to a number value. A sum variable is initialised to zero.
- A [parallel gateway](${camundaManualBaseUrl}/reference/bpmn20/gateways/parallel-gateway/) is used to execute 2 branches in parallel.
- Each branch calls a call activity which squares the number, and the result is added to the sum. 
- The result is checked

<br/>
An integer variable is initialised to a number value.

![](initialise-variable-call-activity.png)
```groovy
execution.setVariable("inputValue", 2)
```
<br/>
A reusable call activity subprocess is called to square the number. Note that it is essential that the Business Key is passed to all call activities.

![](execute-square-script-call-activity-properties.png)

<br/>
Variables are passed to and from the subprocess.

![](execute-square-script-call-activity-variables.png)

<br/>
The result is checked.

![](check-result-call-activity.png)
```groovy
assert result == 4
```

<br/>
And now for the bottom row of the process model.

![](showcase-call-activity-variable-passing-bottom-row.png)

<br/>
An integer variable is initialised to a number value. A sum variable is initialise to zero.

![](initialise-variables-call-activity.png)
```groovy
execution.setVariable("sum", 0)
execution.setVariable("inputValue", 2)
```

<br/>
A reusable call activity subprocess is called to square the number. In this case output mapping must be used order to ensure that the output variable is scoped to the execution corresponding to the specific parallel branch. This is done my selecting *all* and *Local*.

![](execute-square-script-call-activity-parallel-properties.png)

<br/>
And this is used in [combination with input/output mapping](${camundaManualBaseUrl}/reference/bpmn20/subprocesses/call-activity/#combination-with-input-output-parameters). The output mapping is defined to map the *outputValue* to the variable *localResult* which will be local to the parallel execution. This is an example of use of variable scoping. This same scoping issue is the also subject of the following section which showcases scoping.

![](execute-square-script-call-activity-parallel-mapping.png)

<br/>
Each branch adds its *localResult* to the *sum*. Since the sum is shared across both parallel branches it is necessary to protect this script task from concurrent updates. This is done by using [asynchronous continuations](${camundaManualBaseUrl}/user-guide/process-engine/transactions-in-processes/#asynchronous-continuations).

![](add-to-sum-async.png)
```groovy
execution.setVariable("sum", sum + localResult)
```

<br/>
It necessary to add an asynchronous continuation to the joining parallel gateway to avoid locking exceptions as described by [Camunda best practice](https://camunda.com/best-practices/dealing-with-problems-and-exceptions/#__strong_do_strong_configure_a_savepoint_strong_before_strong).

![](parallel-join-async.png)

<br/>
The result is checked.

![](check-result-call-activity-parallel.png)
```groovy
assert sum == 8
```

###Showcase scoping
The following BPMN diagram shows the *Showcase scoping* subprocess.

![](showcase-scoping-process.png)

This is the same scenario as the bottom row of the previous process which demonstrates scoping in the context of call activity variable passing, but in this case a script task is used rather than a call activity. 

The following points summarise the process behaviour...

- An integer variable is initialised to a number value. A sum variable is initialised to zero.
- A [parallel gateway](${camundaManualBaseUrl}/reference/bpmn20/gateways/parallel-gateway/) is used to execute 2 branches in parallel.
- Each branch executes a script task which squares the number, the result is set as a local variable and added to the sum.
- The result is checked

<br/>
An integer variable is initialised to a number value. A sum variable is initialise to zero.

![](initialise-variables-scoping.png)
```groovy
execution.setVariable("sum", 0)
execution.setVariable("inputValue", 2)
```

<br/>
A script task squares the number setting the result as a local variable. This is necessary to ensure that the output variable is scoped to the execution corresponding to the specific parallel branch.

![](calculate-square-scoping.png)
```groovy
execution.setVariableLocal("localOutputValue", inputValue*inputValue)
```

<br/>
Each branch adds its *localResult* to the *sum*. Since the *sum* variable is shared across both parallel branches it is necessary to protect this script task from concurrent updates. This is done by using [asynchronous continuations](${camundaManualBaseUrl}/user-guide/process-engine/transactions-in-processes/#asynchronous-continuations).

![](add-to-sum-async-scoping.png)
```groovy
execution.setVariable("sum", sum + localOutputValue)
```

<br/>
It necessary to add an asynchronous continuation to the joining parallel gateway to avoid locking exceptions as described by [Camunda best practice](https://camunda.com/best-practices/dealing-with-problems-and-exceptions/#__strong_do_strong_configure_a_savepoint_strong_before_strong).

![](parallel-join-async.png)

<br/>
The result is checked.

![](check-result-call-activity-parallel.png)
```groovy
assert sum == 8
```

###Showcase multi-instance
The following BPMN diagram shows the *Showcase multi-instance* subprocess.

![](showcase-multi-instance.png)

This process performs the same function as the previous previous but uses a parallel [multi-instance](${camundaManualBaseUrl}/reference/bpmn20/tasks/task-markers/#multiple-instance) [embedded subprocess](${camundaManualBaseUrl}/reference/bpmn20/subprocesses/embedded-subprocess/) to achieve the parallel square+sum operations instead of using a parallel gateway. 

The following points summarise the process behaviour...

- A collection is initialised with input values for the multiple instances. A sum variable is initialised to zero.
- A parallel multi-instance embedded subprocess is used to execute 2 instances in parallel.
- Each instance executes a script task which squares the number, the result is set as a local variable and added to the sum.
- The result is checked

<br/>
A collection is initialised with input values for the multiple instances. A *sum* variable is initialised to zero.

![](initialise-variables-multi-instance.png)
```groovy
def inputValues = [2,2]
execution.setVariable("inputValues", inputValues)
execution.setVariable("sum", 0)
```

<br/>
A parallel multi-instance embedded subprocess is used to execute 2 instances in parallel.

![](parallel-multi-instance.png)

The *inputValues* list created in the previous task is used as the collection for the multi-instance. Each instance will take an element from the collection and name the variable *inputValue*.

Note that in some complex multi-instance scenarios it may be necessary to configure Multi Instance Asynchronous Before and/or Multi Instance Asynchronous After. This topic is discussed in [Asynchronous Continuations of Multi-Instance Activities](${camundaManualBaseUrl}/user-guide/process-engine/transactions-in-processes/#asynchronous-continuations-of-multi-instance-activities).

<br/>
A script task squares the number setting the result as a local variable. This is necessary to ensure that the output variable is scoped to the execution corresponding to the specific parallel branch.

![](calculate-square-scoping.png)
```groovy
execution.setVariableLocal("localOutputValue", inputValue*inputValue)
```

<br/>
Each instance adds its *localResult* to the *sum*. Since the *sum* variable is shared across both parallel instances it is necessary to protect this script task from concurrent updates. This is done by using [asynchronous continuations](${camundaManualBaseUrl}/user-guide/process-engine/transactions-in-processes/#asynchronous-continuations).

![](add-to-sum-async-scoping.png)
```groovy
execution.setVariable("sum", sum + localOutputValue)
```

<br/>
The result is checked.

![](check-result-call-activity-parallel.png)
```groovy
assert sum == 8
```
