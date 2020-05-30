package com.example.configcenter;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.ProtoClient;
import io.kubernetes.client.ProtoClient.ObjectOrStatus;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.models.V1DeleteOptions;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.models.V1Status;
import io.kubernetes.client.proto.Meta.ObjectMeta;
import io.kubernetes.client.proto.V1.Namespace;
import io.kubernetes.client.proto.V1.NamespaceSpec;
import io.kubernetes.client.proto.V1.Pod;
import io.kubernetes.client.proto.V1.PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

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

//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/post")
public class ManagerSvcUpdate {
    public static long startTime = 0L;
    public static long endTime = 0L;
    public static final String updatePodFailed = "-----------------------------------\n"
        									   + "         Update Pod Failed         \n"
        									   + "-----------------------------------\n";
    public static final String updateSvcFailed = "-----------------------------------\n"
    										   + "       Update Service Failed       \n"
    										   + "-----------------------------------\n";
@RequestMapping(value = "/updatePod", method = RequestMethod.POST)
@ResponseBody
    public String updatePod(@RequestBody Map<String, String> podInfo) throws ApiException, IOException {    
    	String kubeConfigPath = "config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
    	String pretty = "true"; 
        String dryRun = null; // String | When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed

        System.out.println("-----------------------------------");
        System.out.println("            Pod Update             ");
        System.out.println("-----------------------------------");
        startTime = System.currentTimeMillis();
        String podName = "degrade-low-74555f8bd8-wsks5", podNamespace = "default";
        String patchBody = "";
        try {
          if(podInfo.containsKey("name")){
          	podName = podInfo.get("name").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"name\".");
              endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(updatePodFailed);
              return "false";
          }

		  if(podInfo.containsKey("namespace")){
          	podNamespace = podInfo.get("namespace").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"namespace\".");
              endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(updatePodFailed);
              return "false";
          }

          if(podInfo.containsKey("patch")){
          	patchBody = podInfo.get("patch").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"patch\".");
              endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(updatePodFailed);
              return "false";
          }

		  V1Pod result = api.patchNamespacedPod(podName, podNamespace, patchBody, pretty, dryRun);

		  if(!result.getStatus().equals("Success"))
          {
        	  System.out.println(result);
          }
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#updateNamespacedPod");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
            endTime = System.currentTimeMillis();
            System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
            System.out.println(updatePodFailed);
            return "false";
        }
        
        endTime = System.currentTimeMillis();
        System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
        System.out.println("-----------------------------------");
        System.out.println("          Update Pod Done           ");
        System.out.println("-----------------------------------");
        return "true";
    }

@RequestMapping(value = "/updateService", method = RequestMethod.POST)
@ResponseBody
    public String updateService(@RequestBody Map<String, String> svcInfo) throws ApiException, IOException {    
    	String kubeConfigPath = "config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
    	String pretty = "true"; 
        String dryRun = null; // String | When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
                  
        System.out.println("-----------------------------------");
        System.out.println("          Sevice Update            ");
        System.out.println("-----------------------------------");
        startTime = System.currentTimeMillis();
        try {
		  String svcName = "degrade-low-74555f8bd8-wsks5", svcNamespace = "default";
          String patchBody = "";
    	  if(svcInfo.containsKey("name")){
          	svcName = svcInfo.get("name").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"name\".");
              endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(updateSvcFailed);
              return "false";
          }

		  if(svcInfo.containsKey("namespace")){
          	svcNamespace = svcInfo.get("namespace").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"namespace\".");
              endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(updateSvcFailed);
              return "false";
          }

          if(svcInfo.containsKey("patch")){
          	patchBody = svcInfo.get("patch").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"patch\".");
              endTime = System.currentTimeMillis();
    		  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    		  System.out.println(updatePodFailed);
              return "false";
          }
		  
          V1Service result = api.patchNamespacedService(svcName, svcNamespace, patchBody, pretty, dryRun);

          if(!result.getStatus().equals("Success"))
          {
        	  System.out.println(result);
          }
        } catch (ApiException e) {
        //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
          System.err.println("Exception when calling CoreV1Api#updateNamespacedService");
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          System.err.println("Response headers: " + e.getResponseHeaders());
          e.printStackTrace();
          endTime = System.currentTimeMillis();
    	  System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
    	  System.out.println(updateSvcFailed);
          return "false";
        }

        endTime = System.currentTimeMillis();
    	System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
        System.out.println("-----------------------------------");
        System.out.println("        Search Service Done        ");
        System.out.println("-----------------------------------");
        return "true";
    }

@RequestMapping(value = "/updsvc", method = RequestMethod.GET)
@ResponseBody
    public String hahaha() throws ApiException, IOException {    
        System.out.println("-----------------------------------");
        System.out.println("          Sevice Update            ");
        System.out.println("-----------------------------------");
        startTime = System.currentTimeMillis();
        
        try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        endTime = System.currentTimeMillis();
    	System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
        System.out.println("-----------------------------------");
        System.out.println("        Update Service Done        ");
        System.out.println("-----------------------------------");
        return "true";
    }
}