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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.json.MPodList;

import java.io.FileReader;
import java.io.IOException;
@RestController

@RequestMapping("/get")
public class ManagerSvcController {

@RequestMapping(value = "/getPods", method = RequestMethod.GET)
    public String getPods() throws ApiException, IOException {
        //IOException 是操作输入流和输出流时可能出现的异常，ApiException
    	
    	// file path to your KubeConfig，（若直接在项目下可以直接写文件名）
    	String kubeConfigPath = "C:\\Users\\jiryi\\config";
    	
    	// loading the out-of-cluster config, a kubeconfig from file-system
    	//加载k8s,config
    	ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        
    	//将加载config的client设置为默认的client
    	Configuration.setDefaultApiClient(client);
        
    	  // Configure API key authorization: BearerToken
        // ApiKeyAuth BearerToken = (ApiKeyAuth) client.getAuthentication("BearerToken");
        // BearerToken.setApiKey("YOUR API KEY");
        // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
        //BearerToken.setApiKeyPrefix("Token");
        //  String namespace = "default"; 
        // String namespace1 = "istio-system";
        // String namespace2 = "istio-test";
        // String namespace3 = "kube-node-lease";
        // String namespace4 = "kube-public";
        // String namespace5 = "kube-system";
        
        
        // the CoreV1Api loads default api-client from global configuration.
        //创建一个api,这里用的是加了参数的，
        CoreV1Api api = new CoreV1Api(client);
        //CoreV1Api apiInstance = new CoreV1Api();在这里有无括号都一样
        //String namespace = "default";
        String pretty = "true"; 
        Boolean allowWatchBookmarks = true; 
        String _continue = null;
        String fieldSelector = null; 
        String labelSelector = null; 
        Integer limit = null; 
        String resourceVersion = null; 
        Integer timeoutSeconds = 56; 
        Boolean watch = false;
        
        MPodList mpodlist = new MPodList();
        
        System.out.println("-----------------------------------");
        System.out.println("              start                ");
        System.out.println("-----------------------------------");
        try {
          V1NamespaceList namespaceList = api.listNamespace(allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, timeoutSeconds, watch);
          for(int namespaceIndex = 0; namespaceIndex < namespaceList.getItems().size(); namespaceIndex++)
          {
            V1PodList podList = api.listNamespacedPod(namespaceList.getItems().get(namespaceIndex).getMetadata().getName(),
                                                      allowWatchBookmarks, pretty, _continue, fieldSelector, labelSelector, limit, resourceVersion, null, watch);
            mpodlist.add(podList);
          }          
                    
        } catch (ApiException e) {
          System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
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

        return mpodlist.toJSON();
    }
}
