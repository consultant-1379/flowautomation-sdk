The Getting Started section aims to show how simple import and execute flow actions could be performed on a user's computer using a docker runtime environment. Please follow the steps below:

   [Step 1. Prepare Docker Design Runtime Environment](#step1)<br/>
   [Step 2. Launch FA application UI](#step2)<br/>
   [Step 3. Import new flow into the application](#step3)<br/>
   [Step 4. Execute the imported flow](#step4)<br/>
   [Step 5. Install Camunda Modeler](#step5)<br/>
   [Step 6. Design a New Flow](#step6)<br/><br/>

<a name="step1"/>

####Step 1. Prepare Docker Design Runtime Environment

The first step is to prepare the Docker Design Runtime Environment (*abbr. DDRE*) on your computer.
 
DDRE is a self-sufficient and dockerized Flow Automation application with all required dependencies.

Using this special environment you will be able to import and execute flows on your development machine within a couple of minutes.
 
Please follow the instructions provided here: [Docker Design Runtime Environment (DDRE)](../developing-flows/designing/design-environment/docker-design-runtime-environment.html)
<br/><br/>

<a name="step2"/>

####Step 2. Launch FA application UI

At this point, you’ve installed the Docker Environment with the Flow Automation application on your development machine,<br/> 
and now we can proceed and open FA application UI in your preferred Web browser: [http://localhost:8282/#flow-automation](http://localhost:8282/#flow-automation)
  
<div style="padding-left: 50px;">
<br/><img src="FlowAutomationApp.png" alt="FlowAutomationApp" width="800"/><br/><br/>
</div>
  
<a name="step3"/>

####Step 3. Import new flow into the application

Once the application is loaded, click on the **View Flow Catalog** link to see System Predefined flows and to have the opportunity to import yours.

*Note: System Predefined flows are internal, and cannot be started by user.*

<div style="padding-left: 50px;">
<br/><img src="FlowCatalog.png" alt="FlowCatalog" width="800"/><br/><br/>
</div>
  
Click on the **Import Flow** button and import the provided zip file with the first example: [simpleCalculatorFlow-1.0.1.zip](../example-flows/simpleCalculatorFlow/simpleCalculatorFlow-1.0.1.zip)

<div style="padding-left: 50px;">
<br/><img src="ImportedFlow.png" alt="ImportedFlow" width="800"/><br/><br/>
</div>
  
After a successful import, you should see it in the **Imported Flows** table.<br/><br/>
 
<a name="step4"/>
 
####Step 4. Execute the imported flow

Now you can execute your first imported flow by selecting it in the table and by clicking on the **Start** button.

Follow the instructions and let your flow instance finish its execution, so that **State** will have the value **Executed**.
 
<div style="padding-left: 50px;">
<br/><img src="ExecutedFlow.png" alt="ExecutedFlow" width="800"/><br/><br/>
</div>

Flows are defined via the BPM approach and using [BPMN](https://en.wikipedia.org/wiki/Business_Process_Model_and_Notation) (Business Process Model and Notation) diagrams to illustrate a process.
 
A diagram example is provided above, and once Flow is executed, you can see the execution path on the **Process Diagram** tab.

Congratulations. You have successfully imported and executed your first simple flow with the Flow Automation application. 

<a name="step5"/>

####Step 5. Install Camunda Modeler

To be able to view, edit an existing diagram, or create a new diagram for a new flow - a special BPMN Modeler tool is needed.

We recommend the use of Camunda Modeler which is available for free on their official website: [https://camunda.com/download/modeler](https://camunda.com/download/modeler)

*Note: The Camunda Modeler is a third-party application and as such is not supported by Ericsson.*

Once you've install Modeler you can take a closer look at the example flow which you've imported and executed, and also other [Example Flows](../example-flows/example-flows.html).

<a name="step6"/>

####Step 6. Design a New Flow

You can now create a new flow project using the instructions provided at [Flow Project Maven Archetype](../developing-flows/designing/design-environment/flow-project-maven-archetype.html). This will create a sample flow project which you can use as a starting point for your new flow.
 
 