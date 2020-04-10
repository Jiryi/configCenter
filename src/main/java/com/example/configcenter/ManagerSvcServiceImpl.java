package com.example.configcenter;

import jdk.nashorn.api.scripting.JSObject;

import java.io.FileReader;
import java.util.List;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class ManagerSvcServiceImpl implements ManagerSvcService{
    @Override
    public List<String> showServices() {
        return null;
    }
    
    public List<String> showPods(){
    	return null;
    };
}
