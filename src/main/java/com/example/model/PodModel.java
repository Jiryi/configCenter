package com.example.model;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.ProtoClient;
import io.kubernetes.client.ProtoClient.ObjectOrStatus;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.models.V1Container;
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
 * 一个pod的属性
 */


public class PodModel {
    private String name;     //pod名字
    private String namespace;//名空间
    private String status;   //当前状态
    private Map<String, String> labels;         //标签
    private Map<String, String> annotations;    //注释
    private DateTime creationTime;  //创建时间
    private String message;  //关于当前状态的具体信息以及异常导致原因
    private String podIP;    //pod的IP地址
    private Integer restarts; //重启次数
    private String age;       //pod年龄
    private Float CPUs;     //CPU使用率
    private Float Memory;   //内存使用情况
    // private Integer containersNumber; //包含的容器数量
    // private List< String > containers;  //所有包含的容器名

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public String getPodIP() {
        return this.podIP;
    }

    public Integer getRestarts() {
        return this.restarts;
    }

    public String getAge() {
        return this.age;
    }

    public Float getCPUs() {
        return this.CPUs;
    }

    public Map<String, String> getLabels() {
        return this.labels;
    }

    public Float getMemory() {
        return this.Memory;
    }
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

    public void set(V1Pod v1Pod) {
        this.name = v1Pod.getMetadata().getName();
        this.namespace = v1Pod.getMetadata().getNamespace();
        this.status = v1Pod.getStatus().getPhase();
        this.message = v1Pod.getStatus().getMessage();
        this.podIP = v1Pod.getStatus().getPodIP();
        this.restarts = 0;
        this.age = calculateDuration(v1Pod.getMetadata().getCreationTimestamp());
//        this.CPUs = calculateCPU(v1Pod.getSpec().getContainers());     //CPU使用率
        this.CPUs = (float) 0;
        this.Memory = (float) 0;   //内存使用情况
    }

    public void queryDetails(V1Pod v1Pod) {
        this.name = v1Pod.getMetadata().getName();
        this.namespace = v1Pod.getMetadata().getNamespace();
        this.status = v1Pod.getStatus().getPhase();
        this.message = v1Pod.getStatus().getMessage();
        this.podIP = v1Pod.getStatus().getPodIP();
        this.age = calculateDuration(v1Pod.getMetadata().getCreationTimestamp());
        this.creationTime = v1Pod.getMetadata().getCreationTimestamp()；
        this.annotations = v1Pod.getMetadata().getAnnotations();
        this.labels = v1Pod.getMetadata().getLabels();
        this.restarts = 0;
        this.CPUs = (float) 0;
        this.Memory = (float) 0;
    }
} 


/**
<dependency>
   <groupId>joda-time</groupId>
   <artifactId>joda-time</artifactId>
   <version>2.9.8</version>
</dependency>

 */