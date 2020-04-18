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

//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/post")
public class ManagerSvcDelete {
    public static long startTime = 0L;
    public static long endTime = 0L;
    public static final String deletePodFailed = "-----------------------------------\n"
        									   + "         Delete Pod Failed         \n"
        									   + "-----------------------------------\n";
    public static final String deleteSvcFailed = "-----------------------------------\n"
    										   + "       Delete Service Failed       \n"
    										   + "-----------------------------------\n";
@RequestMapping(value = "/deletePod", method = RequestMethod.POST)
@ResponseBody
    public PodModel deletePod(@RequestBody Map<String, String> podInfo) throws ApiException, IOException {    
    	String kubeConfigPath = "C:\\Users\\jiryi\\config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
    	String pretty = "true"; 
        String dryRun = null; // String | When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
        Integer gracePeriodSeconds = 0; // Integer | The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
        Boolean orphanDependents = true; // Boolean | Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the \"orphan\" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
        String propagationPolicy = "Background"; // String | Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
                
        System.out.println("-----------------------------------");
        System.out.println("            Pod Delete             ");
        System.out.println("-----------------------------------");
        try {
		  String podName = "degrade-low-74555f8bd8-wsks5", podNamespace = "default";
    	  if(podInfo.containsKey("name")){
          	podName = podInfo.get("name").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"name\".");
              System.out.println(deletePodFailed);
              return "false";
          }

		  if(podInfo.containsKey("namespace")){
          	podNamespace = podInfo.get("namespace").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"namespace\".");
              System.out.println(deletePodFailed);
              return "false";
          }

		  V1Status result = api.deleteNamespacedPod(podName, podNamespace, pretty, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, null);
          System.out.println(result);
        } catch (ApiException e) {
        //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
          System.err.println("Exception when calling CoreV1Api#deleteNamespacedPod");
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          System.err.println("Response headers: " + e.getResponseHeaders());
          e.printStackTrace();
          return "false";
        }
        
        System.out.println("-----------------------------------");
        System.out.println("          Delete Pod Done           ");
        System.out.println("-----------------------------------");
        return "true";
    }

@RequestMapping(value = "/deleteService", method = RequestMethod.POST)
@ResponseBody
    public PodModel deleteService(@RequestBody Map<String, String> svcInfo) throws ApiException, IOException {    
    	String kubeConfigPath = "C:\\Users\\jiryi\\config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
    	String pretty = "true"; 
        String dryRun = null; // String | When present, indicates that modifications should not be persisted. An invalid or unrecognized dryRun directive will result in an error response and no further processing of the request. Valid values are: - All: all dry run stages will be processed
        Integer gracePeriodSeconds = 0; // Integer | The duration in seconds before the object should be deleted. Value must be non-negative integer. The value zero indicates delete immediately. If this value is nil, the default grace period for the specified type will be used. Defaults to a per object value if not specified. zero means delete immediately.
        Boolean orphanDependents = true; // Boolean | Deprecated: please use the PropagationPolicy, this field will be deprecated in 1.7. Should the dependent objects be orphaned. If true/false, the \"orphan\" finalizer will be added to/removed from the object's finalizers list. Either this field or PropagationPolicy may be set, but not both.
        String propagationPolicy = "Background"; // String | Whether and how garbage collection will be performed. Either this field or OrphanDependents may be set, but not both. The default policy is decided by the existing finalizer set in the metadata.finalizers and the resource-specific default policy. Acceptable values are: 'Orphan' - orphan the dependents; 'Background' - allow the garbage collector to delete the dependents in the background; 'Foreground' - a cascading policy that deletes all dependents in the foreground.
                
        System.out.println("-----------------------------------");
        System.out.println("          Sevice Delete            ");
        System.out.println("-----------------------------------");
        try {
		  String svcName = "degrade-low-74555f8bd8-wsks5", svcNamespace = "default";
    	  if(svcInfo.containsKey("name")){
          	svcName = svcInfo.get("name").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"name\".");
              System.out.println(deleteSvcFailed);
              return "false";
          }

		  if(svcInfo.containsKey("namespace")){
          	svcNamespace = svcInfo.get("namespace").toString();
    	  } else {
              System.out.println("Cannot Find Or Resolve Field: \"namespace\".");
              System.out.println(deleteSvcFailed);
              return "false";
          }

		  V1Status result = api.deleteNamespacedService(svcName, svcNamespace, pretty, dryRun, gracePeriodSeconds, orphanDependents, propagationPolicy, null);
          System.out.println(result);
        } catch (ApiException e) {
        //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
          System.err.println("Exception when calling CoreV1Api#deleteNamespacedService");
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          System.err.println("Response headers: " + e.getResponseHeaders());
          e.printStackTrace();
          return "false";
        }
        
        System.out.println("-----------------------------------");
        System.out.println("        Delete Service Done        ");
        System.out.println("-----------------------------------");
        return "true";
    }