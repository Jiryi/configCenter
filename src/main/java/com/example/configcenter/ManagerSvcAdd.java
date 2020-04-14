package com.example.configcenter;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.ProtoClient;
import io.kubernetes.client.ProtoClient.ObjectOrStatus;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1ObjectMeta;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
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

// @CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/post")
public class ManagerSvcQuery {
@RequestMapping(value = "/addPod", method = RequestMethod.POST)
@ResponseBody
    public String addPod(@RequestBody Map<String, String> podInfo) throws ApiException, IOException {    
    	String kubeConfigPath = "C:\\Users\\jiryi\\config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
        String pretty = "true"; // String | If 'true', then the output is pretty printed.
        String dryRun = "All"; 

        System.out.println("-----------------------------------");
        System.out.println("              Add Pod              ");
        System.out.println("-----------------------------------");
        try {
          V1Namespace namespace;
		  String podName = null, podNamespace = null;
    	  if(podInfo.containsKey("name")){
          	podName = podInfo.get("name").toString();
    	  } else return "false";
        
		  if(podInfo.containsKey("namespace")){
          	podNamespace = podInfo.get("namespace").toString();
            namespace = api.readNamespace("name", null, null, null);
    	  } else return "false";

          if(namespace == null)
          {
              V1Namespace namespaceBody = new V1Namespace();
              namespace = api.createNamespace(namespaceBody, pretty, dryRun, (new DateTime()).toString());
          }

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
          V1Pod v1Pod = api.createNamespacedPod(namespace, pod, pretty, dryRun, (new DateTime()).toString());
          if(v1Pod == null) return "false";
    
        } catch (ApiException e) {
        //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          System.err.println("Response headers: " + e.getResponseHeaders());
          e.printStackTrace();
        }
        
        System.out.println("-----------------------------------");
        System.out.println("            Add Pod Done           ");
        System.out.println("-----------------------------------");

        return "true";
    }

// @CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/post")
public class ManagerSvcQuery {
@RequestMapping(value = "/addService", method = RequestMethod.POST)
@ResponseBody
    public String addPod(@RequestBody Map<String, String> podInfo) throws ApiException, IOException {    
    	String kubeConfigPath = "C:\\Users\\jiryi\\config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
        String pretty = "true"; // String | If 'true', then the output is pretty printed.
        String dryRun = "All"; 

        System.out.println("-----------------------------------");
        System.out.println("            Add Service            ");
        System.out.println("-----------------------------------");
        try {
          V1Namespace namespace;
		  String svcName = null, svcNamespace = null;
    	  if(podInfo.containsKey("name")){
          	svcName = podInfo.get("name").toString();
    	  } else return "false";
        
		  if(podInfo.containsKey("namespace")){
          	svcNamespace = podInfo.get("namespace").toString();
            namespace = api.readNamespace("name", null, null, null);
    	  } else return "false";

          if(namespace == null)
          {
              V1Namespace namespaceBody = new V1Namespace();
              namespaceBody.getMetadata().setName(svcNamespace);
              namespace = api.createNamespace(namespaceBody, pretty, dryRun, (new DateTime()).toString());
          }

          V1Service svc =new V1ServiceBuilder()
            .withNewMetadata()
            .withName(svcName)
            .endMetadata()
            .withNewSpec()
            .withSessionAffinity("ClientIP")
            .withType("NodePort")
            .addNewPort()
            .withProtocol("TCP")
            .withName("client")
            .withPort(8008)
            .withNodePort(8080)
            .withTargetPort(new IntOrString(8080))
            .endPort()
            .endSpec()
            .build();
          V1Service v1Svc = api.createNamespacedPod(namespace, svc, pretty, dryRun, (new DateTime()).toString());
          if(v1Svc == null) return "false";
    
        } catch (ApiException e) {
        //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          System.err.println("Response headers: " + e.getResponseHeaders());
          e.printStackTrace();
        }
        
        System.out.println("-----------------------------------");
        System.out.println("            Add Pod Done           ");
        System.out.println("-----------------------------------");

        return "true";
    }
}
