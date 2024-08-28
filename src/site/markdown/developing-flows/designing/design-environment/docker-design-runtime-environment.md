#Docker Design Runtime Environment (DDRE)

---
The docker design runtime environment  is the offline flow-automation application and its not connected to production ENM environment. The flow designer can import and execute flows in this docker environment.

- [Docker Setup](#Docker_Setup)
- [Launch Flow Automation UI](#Launch_Flow_Automation_UI)
- [Flow Automation NBI](#Flow_Automation_NBI)
- [Shutdown DDRE](#Shutdown_DDRE)

###Docker Setup

- Install and setup Docker for your OS from Docker Official Website.
- Execute below command to make sure docker is installed successfully. The command should display the versions of Docker's Client and Server engine.
    ````
    docker version
    ```` 
- Execute below command to make sure docker-compose is installed successfully. The command should display the version of docker-compose.
    ````
    docker-compose version
    ````  
- Execute below command to login into Docker registry. When prompted for username and password, please give your credentials. If access is denied, please contact your administrator.
    ````
    docker login armdocker.rnd.ericsson.se
    ````  
- Once Docker setup is successfully done, download [docker-compose.yml](docker-compose.yml) file and save it in your preferred directory. Follow the below steps to setup DDRE.
- Navigate to the directory where docker-compose.yml file is stored on your machine.

- Execute any of the below options to bring up the docker containers
    - Option 1 : Using latest version of flow automation service rpm.
        ````
        docker-compose up --force-recreate -d
        ````
    - Option 2 : Using specific version of flow automation service rpm.
        Get flow automation service rpm version by executing below command on flow automation VM:
        ````
        [root@svc-1-flowautomation /]# rpm -qa | grep -i ERICflowautomationservice_CXP9036275
        ERICflowautomationservice_CXP9036275-1.41.2-1.noarch
        ````   
        
        The rpm version is 1.41.2.
        
        Instantiate the containers:        
        ````   
        version=1.41.2 docker-compose up --force-recreate -d
        ````   
  
- Execute the below command to check all the container are up and running.
    ````
    $ docker-compose ps
        Name                   Command               State                                              Ports                                           
    ------------------------------------------------------------------------------------------------------------------------------------------------------
    access-control   entrypoint.sh -INSP -s acc ...   Up      0.0.0.0:1389->1389/tcp, 0.0.0.0:1636->1636/tcp, 0.0.0.0:4444->4444/tcp                     
    apache           entrypoint.sh -INSP -s apa ...   Up      0.0.0.0:6666->6666/tcp, 0.0.0.0:8282->80/tcp                                               
    db               docker-entrypoint.sh postgres    Up      0.0.0.0:5431->5432/tcp                                                                     
    fa               entrypoint.sh -INSPJ -s jboss    Up      0.0.0.0:4447->4447/tcp, 0.0.0.0:8080->8080/tcp, 0.0.0.0:8787->8787/tcp,                    
                                                              0.0.0.0:9090->9090/tcp, 0.0.0.0:9990->9990/tcp, 0.0.0.0:9999->9999/tcp                      
    ````
  All the 4 containers should be up and running.

###Launch Flow Automation UI
- Launch flow automation ui using http://localhost:8282/#flow-automation from your browser.
- Flow automation internal flows and a sample predefined flow is already imported.
- You can import your flow and execute.

###Flow Automation NBI
- Flow automation NBI is available in this docker environment.
- The base endpoint is http://localhost:8282/flowautomation/
- You can refer [Flow Automation External REST](http://gask2web.ericsson.se/service/get?DocNo=1/15519-CNA4033544-1&Lang=EN&Rev=) NBI document.

###Shutdown DDRE
- Execute below command to stop the containers.
    ````
    docker-compose stop
    ````
- Execute below command to remove the containers.
    ````
    docker-compose rm -f -v
    ````
- Execute below command to make sure all the containers are removed successfully.
    ````
    docker-compose ps
    ````              