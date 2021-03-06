package com.example.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class ServiceListModel {
    private List< ServiceModel > serviceList = new ArrayList< ServiceModel >();

    public void add(V1ServiceList v1SeriveList) {
        for(int index = 0; index < v1SeriveList.getItems().size(); index++)
        {
        	ServiceModel service = new ServiceModel();
        	service.set(v1SeriveList.getItems().get(index));
        	if(serviceList == null) serviceList.set(1, service);
        	else serviceList.add(service);
        }
    }

    public String toJSON() {
        JSONArray json = new JSONArray();
        try{
        for(int index = 0; index < serviceList.size(); index++)
        {
            JSONObject jo = new JSONObject();
            jo.put("name",     serviceList.get(index).getName());
            jo.put("namespace",serviceList.get(index).getNamespace());
            jo.put("type",     serviceList.get(index).getType());
            jo.put("clusterIP",serviceList.get(index).getClusterIP());
            jo.put("age",      serviceList.get(index).getAge());
            jo.put("label",    serviceList.get(index).getLabel());
            json.put(jo);
        }
        } catch (Exception e) {
          e.printStackTrace();
        }

        return json.toString();
    }
}