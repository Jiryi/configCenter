package com.example.json;

import java.util.ArrayList;
import java.util.List;

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
public class MPodList {
    private List< MPod > mPodList = new ArrayList< MPod >();

    public List< MPod > getNames() {
        return mPodList;
    }

    public void set(V1PodList v1PodList) {
        for(int index = 0; index < v1PodList.getItems().size(); index++)
        {
        	MPod pod = new MPod();
        	pod.set(v1PodList.getItems().get(index));
        	mPodList.set(1, pod);
        }
    }
    
    public void add(V1PodList v1PodList) {
        for(int index = 0; index < v1PodList.getItems().size(); index++)
        {
        	MPod pod = new MPod();
        	pod.set(v1PodList.getItems().get(index));
        	System.out.println(pod);
        	if(mPodList == null) mPodList.set(1, pod);
        	else mPodList.add(pod);
        }
    }

    public JSONArray toJSON() {
        JSONArray json = new JSONArray();
        for(int index = 0; index < mPodList.size(); index++)
        {
            JSONObject jo = new JSONObject();
            jo.put("name",     mPodList.get(index).getName());
            jo.put("status",   mPodList.get(index).getStatus());
            jo.put("message",  mPodList.get(index).getMessage());
            jo.put("podIP",    mPodList.get(index).getPodIP());
            jo.put("restarts", mPodList.get(index).getRestarts());
            jo.put("age",      mPodList.get(index).getAge());
            jo.put("CPUS",     mPodList.get(index).getCPUS());
            jo.put("Memory",   mPodList.get(index).getMemory());
            json.put(jo);
        }

        return json;
    }

}