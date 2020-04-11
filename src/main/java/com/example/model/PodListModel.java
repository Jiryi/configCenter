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

/**
 * 返回pod列表
 *
 */
public class PodListModel {
    private List< PodModel > podList = new ArrayList< PodModel >();

    public List< PodModel > getPodList() {
        return podList;
    }

    public void set(V1PodList v1PodList) {
        for(int index = 0; index < v1PodList.getItems().size(); index++)
        {
        	PodModel pod = new PodModel();
        	pod.set(v1PodList.getItems().get(index));
        	podList.set(1, pod);
        }
    }
    
    public void add(V1PodList v1PodList) {
        for(int index = 0; index < v1PodList.getItems().size(); index++)
        {
        	PodModel pod = new PodModel();
        	pod.set(v1PodList.getItems().get(index));
        	if(podList == null) podList.set(1, pod);
        	else podList.add(pod);
        }
    }

    public String toJSON() {
        JSONArray json = new JSONArray();
        try{
        for(int index = 0; index < podList.size(); index++)
        {
            JSONObject jo = new JSONObject();
            jo.put("name",     podList.get(index).getName());
            jo.put("namespace",podList.get(index).getNamespace());
            jo.put("status",   podList.get(index).getStatus());
            jo.put("IP",       podList.get(index).getPodIP());
            jo.put("age",      podList.get(index).getAge());
            // jo.put("restarts", mPodList.get(index).getRestarts());
            // jo.put("message",  mPodList.get(index).getMessage());
            // jo.put("CPUs",     mPodList.get(index).getCPUs());
            // jo.put("Memory",   mPodList.get(index).getMemory());
            json.put(jo);
        }
        } catch (Exception e) {
          e.printStackTrace();
        }

        return json.toString();
    }

}