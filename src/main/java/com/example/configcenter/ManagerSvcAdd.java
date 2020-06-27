package com.example.configcenter;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.ProtoClient;
import io.kubernetes.client.ProtoClient.ObjectOrStatus;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceBuilder;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodBuilder;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceBuilder;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.proto.Meta.ObjectMeta;
import io.kubernetes.client.proto.V1.Namespace;
import io.kubernetes.client.proto.V1.NamespaceSpec;
import io.kubernetes.client.proto.V1.Pod;
import io.kubernetes.client.proto.V1.PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.PodListModel;
import com.example.model.PodModel;
import com.example.model.ServiceListModel;
import com.example.model.ServiceModel;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
@RestController

// @CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/post")
public class ManagerSvcAdd {
	public static long startTime = 0L;
	public static long endTime = 0L;
	public static final String addPodFailed = "-----------------------------------\n"
											+ "           Add Pod Failed          \n"
											+ "-----------------------------------\n";
	public static final String addSvcFailed = "-----------------------------------\n"
											+ "         Add Service Failed        \n"
											+ "-----------------------------------\n";
@RequestMapping(value = "/addPod", method = RequestMethod.POST)
@ResponseBody
    	public String addPod(@RequestBody Map<String, String> podInfo) throws ApiException, IOException {   
		System.out.println("-----------------------------------");
    	System.out.println("              Add Pod              ");
    	System.out.println("-----------------------------------");
    	startTime = System.currentTimeMillis();
    	String kubeConfigPath = "config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
    	Boolean includeUninitialized = true;
        String pretty = "true"; // String | If 'true', then the output is pretty printed.
        String dryRun = null; 

        V1Namespace namespace;
        String podName = null, podNamespace = null;
        try {
    	  if(podInfo.containsKey("name")){
    		  podName = podInfo.get("name").toString();
    	  } else {
    		  endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(addPodFailed);
    		  return "false";
    	  }
        
		  if(podInfo.containsKey("namespace")){
          	podNamespace = podInfo.get("namespace").toString();
            namespace = api.readNamespace(podNamespace, null, null, null);
    	  } else {
    		  endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(addPodFailed);
    		  return "false";
    	  }
        } catch (ApiException e) {
            //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
            System.err.println("Out Of Exception: " + e.getResponseBody());
//            System.err.println("Response headers: " + e.getResponseHeaders());
            if(e.getCode() == 404)
            {
            	System.out.println("Creating New Namespace: [ " + podNamespace + " ]");
            	System.out.println("Loading...");
            	startTime = System.currentTimeMillis();
            	namespace = new V1NamespaceBuilder()
            			.withNewMetadata()
            			.withName(podNamespace)
            			.endMetadata()
            			.withNewApiVersion("v1")
            			.build();
            	api.createNamespace(namespace, includeUninitialized, pretty, dryRun);
            	endTime = System.currentTimeMillis();
            	System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
            	System.out.println("Create Namespace " + podNamespace + " Success.");
            	System.out.println("Created.\n");
            }
          }
          
          try {
          V1Pod pod = new V1PodBuilder()
                        .withNewMetadata()
                        .withName(podName)
                        .endMetadata()
                        .withNewSpec()
                        .addNewContainer()
                        .withName("www")
                        .withImage("nginx")
                        .endContainer()
                        .endSpec()
                        .build();
          V1Pod v1pod = api.createNamespacedPod(podNamespace, pod, includeUninitialized, pretty, dryRun);
          endTime = System.currentTimeMillis();
          System.out.println("Create Pod " + v1pod.getMetadata().getName() + " Successful!");
          System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    
        } catch (ApiException e) {
        //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          endTime = System.currentTimeMillis();
		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    	  System.out.println(addPodFailed);
    	  return "false";
		}
		
		/* insert in database */
		
        
        System.out.println("-----------------------------------");
        System.out.println("            Add Pod Done           ");
        System.out.println("-----------------------------------");

        return "true";
    }

// @CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/addService", method = RequestMethod.POST)
@ResponseBody
    public String addService(@RequestBody Map<String, String> svcInfo) throws ApiException, IOException {   
		System.out.println("-----------------------------------");
    	System.out.println("            Add Service            ");
    	System.out.println("-----------------------------------");
    	startTime = System.currentTimeMillis();
    	String kubeConfigPath = "config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
        String pretty = "true"; // String | If 'true', then the output is pretty printed.
        String dryRun = "All"; 
        Boolean includeUninitialized = true;

        V1Namespace namespace;
        String svcName = null, svcNamespace = null;
        try {
    	  if(svcInfo.containsKey("name")){
          	svcName = svcInfo.get("name").toString();
    	  } else {
    		  endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(addSvcFailed);
    		  return "false";
    	  }
        
		  if(svcInfo.containsKey("namespace")){
          	svcNamespace = svcInfo.get("namespace").toString();
            namespace = api.readNamespace(svcNamespace, null, null, null);
    	  } else {
    		  endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(addSvcFailed);
    		  return "false";
    	  }
        } catch (ApiException e) {
            //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
            System.err.println("Out Of Exception: " + e.getResponseBody());
//            System.err.println("Response headers: " + e.getResponseHeaders());
            if(e.getCode() == 404)
            {
            	System.out.println("Creating New Namespace: [ " + svcNamespace + " ]");
            	System.out.println("Loading...");
            	namespace = new V1NamespaceBuilder()
            			.withNewMetadata()
            			.withName(svcNamespace)
            			.endMetadata()
            			.withNewApiVersion("v1")
            			.build();
            	api.createNamespace(namespace, includeUninitialized, pretty, dryRun);
            	endTime = System.currentTimeMillis();
            	System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
            	System.out.println("Create Namespace " + svcNamespace + " Success.");
            	System.out.println("Created.\n");
            }
          }

		try {
		  startTime = System.currentTimeMillis();
		  V1ServiceBuilder serviceBuilder = new V1ServiceBuilder()
		  		.withApiVersion("v1")
		  		.withKind("Service")
		  		.withNewMetadata()
		  		.withName(svcName)
		  		.endMetadata();

		  if(svcInfo.containsKey("NodePort")){
			serviceBuilder
				.withNewSpec()
            	.withSessionAffinity("ClientIP")
            	.withType("NodePort")
            	.addNewPort()
            	.withProtocol("TCP")
            	.withName("client")
				.withPort(8008)
				.withNodePort(Integer.valueOf(svcInfo.get("NodePort").toString()))
            	.withTargetPort(new IntOrString(8080))
            	.endPort()
            	.endSpec();
		} else {
			serviceBuilder
				.withNewSpec()
            	.withSessionAffinity("ClientIP")
            	.withType("ClusterIP")
            	.addNewPort()
            	.withProtocol("TCP")
            	.withName("client")
				.withPort(8008)
            	.withTargetPort(new IntOrString(8080))
            	.endPort()
            	.endSpec();
		}

          V1Service svc = serviceBuilder.build();

		  V1Service v1service = api.createNamespacedService(svcNamespace, svc, null, pretty, null);
		  
		  /* insert into database */
		  DatabaseConnection databaseConnection = new DatabaseConnection();
		  if(!databaseConnection.establishConnection()) {
			return "false";
		  }

		  if(!databaseConnection.insertIntoDatabase()) {
  			databaseConnection.closeConnection();
			return "false";
		  }

		  databaseConnection.closeConnection();

          endTime = System.currentTimeMillis();
          System.out.println("Create Service " + v1service.getMetadata().getName() + " Successful!");
          System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    
        } catch (ApiException e) {
        //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          endTime = System.currentTimeMillis();
		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
          System.out.println(addSvcFailed);
          return "false";
        }
        
        System.out.println("-----------------------------------");
        System.out.println("          Add Service Done         ");
        System.out.println("-----------------------------------");

        return "true";
    }
}
