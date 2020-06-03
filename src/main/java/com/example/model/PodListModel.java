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

    public String toJSON(String podStatusString) {
        JSONArray json = new JSONArray();
        try{
        String regex = "[a-z]+\\s+\\d+m\\s+\\d+Mi";
        List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(podStatusString);
		while (m.find()) {
            int i = 1;  
            list.add(m.group(i));  
            i++;
        }

        Map<String, Pair<Integer, Integer> > mapStatus = new HashMap<>(); 
        for (String str : list) {
            String[] name;
            String[] cpu;
            String[] memory;
            name = str.split("\\s");
            cpu = name[1].split("m");
            memory = name[2].split("Mi");

            mapStatus.put( name[0], new Pair<>(Integer.valueOf(cpu[0]), Integer.valueOf(memory[0])));
        }
        
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
            if(mapStatus.containsKey(jo.get("name")))
            {
                jo.put("Memory",   mapStatus.get(jo.get("name")).getValue());
                jo.put("CPUs",     mapStatus.get(jo.get("name")).getKey());
            } else 
            {
                jo.put("Memory",   -1);
                jo.put("CPUs",     -1);
            }
            
            json.put(jo);
        }
        } catch (Exception e) {
          e.printStackTrace();
        }

        return json.toString();
    }

}