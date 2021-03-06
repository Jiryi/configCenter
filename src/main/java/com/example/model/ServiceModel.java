package com.example.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Period;
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
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.proto.Meta.ObjectMeta;
import io.kubernetes.client.proto.V1.Namespace;
import io.kubernetes.client.proto.V1.NamespaceSpec;
import io.kubernetes.client.proto.V1.Pod;
import io.kubernetes.client.proto.V1.PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class ServiceModel{
    private String name;         //service名字
    private String namespace;    //名空间
    private Map<String, String> labels;   //所有标签
    private Map<String, String> selector; //label的一个属性？
    private Map<String, String> annotations;
    private String type;         //service的访问类型,
                                 //include: ExternalName, ClusterIP, NodePort, and LoadBalancer.
    private String clusterIP;    //service的IP地址
    private String age;          //service年龄
    private DateTime creationTime;//创建时间
    private Map<String, String> pods;   //service包含的所有pod
    // private Float CPUs;       //CPU使用率
    // private Float Memory;     //内存使用情况
    // private Integer restarts; //重启次数
    // private Integer containersNumber; //包含的容器数量
    // private List< String > containers;  //所有包含的容器名

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getType() {
        return this.type;
    }

    public DateTime getCreationTime() {
        return this.creationTime;
    }
    
    public Map<String, String> getLabels() {
        return this.labels;
    }
    
    public Map<String, String> getPods()
    {
    	return this.pods;
    }
    
    public Map<String, String> getSelector() {
        return this.selector;
    }
    
    public Map<String, String> getAnnotations() {
        return this.annotations;
    }

    public String getClusterIP() {
        return this.clusterIP;
    }

    public String getAge() {
        return this.age;
    }

    public String getLabel() {
        if(labels != null && labels.entrySet().iterator().hasNext()) {
            return labels.entrySet().iterator().next().getValue();
        }
        return "-";
    }

    // public Float getCPUs() {
    //     return this.CPUs;
    // }

    // public Float getMemory() {
    //     return this.Memory;
    // }
    private String calculateDuration(DateTime creationTimestamp) {
        Period p = new Period(creationTimestamp, new DateTime());
        String ret = null;    
        if(p.getYears() != 0) {
            if(p.getYears() == 1)
                ret = p.getYears() + " year ago";
            else
                ret = p.getYears() + " years ago";
            return ret;
        }
        if(p.getMonths() != 0) {
            if(p.getMonths() == 1)
                ret = p.getMonths() + " month ago";
            else
                ret = p.getMonths() + " months ago";
            return ret;
        }
        if(p.getDays() != 0) {
            if(p.getDays() == 1)
                ret = p.getDays() + " day ago";
            else
                ret = p.getDays() + " days ago";
            return ret;
        }
        if(p.getHours() != 0) {
            if(p.getHours() == 1)
                ret = p.getHours() + " hour ago";
            else
                ret = p.getHours() + " hours ago";
            return ret;
        }

        if(p.getMinutes() == 0 || p.getMinutes() == 1)
            ret = p.getMinutes() + " minute ago";
        else
            ret = p.getMinutes() + " minutes ago";
        return ret;
    }

    private void getPods(List<V1Pod> v1Pods)
    {
        this.pods = new HashMap<String, String>();
        for(int index = 0; index < v1Pods.size(); index++)
        {
            this.pods.put(v1Pods.get(index).getMetadata().getName(), 
                                    v1Pods.get(index).getStatus().getPodIP());
        }
    }

    public void set(V1Service v1Service) {
        this.name = v1Service.getMetadata().getName();
        this.labels = v1Service.getMetadata().getLabels();
        this.namespace = v1Service.getMetadata().getNamespace();
        this.type = v1Service.getSpec().getType();
        this.clusterIP = v1Service.getSpec().getClusterIP();
        this.age = calculateDuration(v1Service.getMetadata().getCreationTimestamp());
    }

    public void queryDetails(V1Service v1Service, List<V1Pod> v1Pods) {
        this.name = v1Service.getMetadata().getName();
        this.labels = v1Service.getMetadata().getLabels();
        this.namespace = v1Service.getMetadata().getNamespace();
        this.type = v1Service.getSpec().getType();
        this.clusterIP = v1Service.getSpec().getClusterIP();
        this.age = calculateDuration(v1Service.getMetadata().getCreationTimestamp());
        this.creationTime = v1Service.getMetadata().getCreationTimestamp();
        this.annotations = v1Service.getMetadata().getAnnotations();
        this.selector = v1Service.getSpec().getSelector();
        getPods(v1Pods);
        // this.restarts = 0;
        // this.CPUs = (float) 0;
        // this.Memory = (float) 0;
    }
}