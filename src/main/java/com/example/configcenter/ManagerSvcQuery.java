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

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/get")
public class ManagerSvcQuery {
@RequestMapping(value = "/queryPod", method = RequestMethod.GET)
@ResponseBody
    public PodModel queryPod(@RequestBody Map<String, String> podInfo) throws ApiException, IOException {    
    	String kubeConfigPath = "C:\\Users\\jiryi\\config";

    	ApiClient client =
    			ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
      
    	Configuration.setDefaultApiClient(client);

    	CoreV1Api api = new CoreV1Api(client);
    	String pretty = "true"; 
    	Boolean export = true;
		Boolean exact = true;
        
        PodListModel podListModel = new PodListModel();
        PodModel podModel = new PodModel();
        
        System.out.println("-----------------------------------");
        System.out.println("             Pod Query              ");
        System.out.println("-----------------------------------");
        try {
		  String podName = "degrade-low-74555f8bd8-wsks5", podNamespace = "default";
    	  if(podInfo.containsKey("name")){
          	podName = podInfo.get("name").toString();
    	  }

		  if(podInfo.containsKey("namespace")){
          	podNamespace = podInfo.get("namespace").toString();
    	  }

          V1Pod pod = api.readNamespacedPod(podName, podNamespace, pretty, exact, export);
		  if(pod != null)
			  podModel.queryDetails(pod);
        } catch (ApiException e) {
        //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
          System.err.println("Status code: " + e.getCode());
          System.err.println("Reason: " + e.getResponseBody());
          System.err.println("Response headers: " + e.getResponseHeaders());
          e.printStackTrace();
        }
        
        System.out.println("-----------------------------------");
        System.out.println("          Query Pod Done           ");
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

        return podModel;
    }

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/queryService", method = RequestMethod.GET)
@ResponseBody
    public String queryService(@RequestBody Map<String, String> serviceInfo) throws ApiException, IOException {
//    public ServiceModel queryService() throws ApiException, IOException {
		String kubeConfigPath = "C:\\Users\\jiryi\\config";

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
		Boolean export = true;
		Boolean exact = true;
		
		ServiceModel serviceModel = new ServiceModel();
        
        System.out.println("-----------------------------------");
        System.out.println("          Query Service            ");
        System.out.println("-----------------------------------");
        try {
			String serviceName = "degradeservices", serviceNamespace = "default";
    	  	if(serviceInfo.containsKey("name")){
          		serviceName = serviceInfo.get("name").toString();
    	  	}

			if(serviceInfo.containsKey("namespace")){
          		serviceNamespace = serviceInfo.get("namespace").toString();
    	  	}
			
			V1Service service = api.readNamespacedService(serviceName, serviceNamespace, pretty, exact, export);
			// Set<V1Pod> v1Pods = new HashSet<>();
			List<V1Pod> pods = new ArrayList<>();
			V1PodList podList = api.listNamespacedPod(serviceNamespace, allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, null, watch);
            
			Map<String, String> svclabels = service.getMetadata().getLabels();
			for(int podlistIndex = 0; podlistIndex < podList.getItems().size(); podlistIndex++)
			{
				Boolean flag = false;
				Map<String, String> podlabels = podList.getItems().get(podlistIndex).getMetadata().getLabels();
				
				Iterator<Entry<String, String>> svcIter = svclabels.entrySet().iterator();
//				Iterator<Entry<String, String>> podIter = podlabels.entrySet().iterator();
        		while(svcIter.hasNext()){
            		 Map.Entry<String, String> svcEntry = (Entry<String, String>) svcIter.next();
					 String svcLabel = svcEntry.getValue() == null?"":svcEntry.getValue();
            		 if(podlabels.containsValue(svcLabel))
            		 {
            			 flag = true;
            			 break;
            		 }
				}
				if(flag == true)
				{
					pods.add(podList.getItems().get(podlistIndex));
				}
			}
		  	if(service != null)
		  		serviceModel.queryDetails(service, pods);         
        	} catch (ApiException e) {
        	// System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
        	System.err.println("Status code: " + e.getCode());
        	System.err.println("Reason: " + e.getResponseBody());
        	System.err.println("Response headers: " + e.getResponseHeaders());
        	e.printStackTrace();
        }
        
        System.out.println("-----------------------------------");
        System.out.println("        Query Service Done         ");
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

        return serviceModel;
    }
}
