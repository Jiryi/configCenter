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
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.proto.Meta.ObjectMeta;
import io.kubernetes.client.proto.V1.Namespace;
import io.kubernetes.client.proto.V1.NamespaceSpec;
import io.kubernetes.client.proto.V1.Pod;
import io.kubernetes.client.proto.V1.PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.PodListModel;
import com.example.model.ServiceListModel;
import com.example.model.ThirdParty;

import java.io.FileReader;
import java.io.IOException;
@RestController
@RequestMapping("/get")
public class ManagerSvcController {
	@Autowired
	  private ThirdParty thirdParty;	
	
@RequestMapping(value = "/getPods", method = RequestMethod.GET)
@ResponseBody
    public String getPods() throws ApiException, IOException {
		String kubeConfigPath = "config";
    
    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);

    	String pretty = "true"; 
    	Boolean allowWatchBookmarks = true; 
    	String _continue = null;
    	String fieldSelector = null; 
    	String labelSelector = null; 
    	Integer limit = null; 
    	String resourceVersion = null; 
    	Integer timeoutSeconds = 56; 
    	Boolean watch = false;
      String podStatusString = null;

        PodListModel podListModel = new PodListModel();
        
        System.out.println("-----------------------------------");
        System.out.println("             Pod List              ");
        System.out.println("-----------------------------------");
        try {
		    podStatusString = thirdParty.updatePodStatus();
  		  System.out.println(podStatusString);
			
          V1NamespaceList namespaceList = api.listNamespace(allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch);
          for(int namespaceIndex = 0; namespaceIndex < namespaceList.getItems().size(); namespaceIndex++)
          {
            V1PodList podList = api.listNamespacedPod(namespaceList.getItems().get(namespaceIndex).getMetadata().getName(),
                                                      allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, null, watch);
            podListModel.add(podList);
          }
                    
        } catch (ApiException e) {
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          System.err.println("Response headers: " + e.getResponseHeaders());
          e.printStackTrace();
        }
        
        System.out.println("-----------------------------------");
        System.out.println("               done                ");
        System.out.println("-----------------------------------");
        
//        try {
//            V1ServiceList result = api.listNamespacedService(namespace, allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch);     
//          } catch (ApiException e) {
//            System.err.println("Exception when calling CoreV1Api#listNamespacedService");
//            System.err.println("Status code: " + e.getCode());
//            System.err.println("Reason: " + e.getResponseBody());
//            System.err.println("Response headers: " + e.getResponseHeaders());
//            e.printStackTrace();
//          }

        return podListModel.toJSON(podStatusString);
    }

//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/getServices", method = RequestMethod.GET)
@ResponseBody
    public String getServices() throws ApiException, IOException {
		String kubeConfigPath = "config";

		ApiClient client =
				ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
  
		Configuration.setDefaultApiClient(client);

		CoreV1Api api = new CoreV1Api(client);
		String pretty = "true"; 
		Boolean allowWatchBookmarks = true; 
		String _continue = null;
		String fieldSelector = null; 
		String labelSelector = null; 
		Integer limit = null; 
		String resourceVersion = null; 
		Integer timeoutSeconds = 56; 
		Boolean watch = false;
		
		ServiceListModel serviceListModel = new ServiceListModel();
        
        System.out.println("-----------------------------------");
        System.out.println("           Service List            ");
        System.out.println("-----------------------------------");
        try {
          V1NamespaceList namespaceList = api.listNamespace(allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch);
          for(int namespaceIndex = 0; namespaceIndex < namespaceList.getItems().size(); namespaceIndex++)
          {
            V1ServiceList serviceList = api.listNamespacedService(
            		namespaceList.getItems().get(namespaceIndex).getMetadata().getName(),
                    allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector,
                    limit, resourceVersion, timeoutSeconds, watch);     
            serviceListModel.add(serviceList);
          }          
                    
        } catch (ApiException e) {
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          System.err.println("Response headers: " + e.getResponseHeaders());
          e.printStackTrace();
        }
        
        System.out.println("-----------------------------------");
        System.out.println("               done                ");
        System.out.println("-----------------------------------");
        
//        try {
//            V1ServiceList result = api.listNamespacedService(namespace, allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch);     
//          } catch (ApiException e) {
//            System.err.println("Exception when calling CoreV1Api#listNamespacedService");
//            System.err.println("Status code: " + e.getCode());
//            System.err.println("Reason: " + e.getResponseBody());
//            System.err.println("Response headers: " + e.getResponseHeaders());
//            e.printStackTrace();
//          }

        return serviceListModel.toJSON();
    }
}
