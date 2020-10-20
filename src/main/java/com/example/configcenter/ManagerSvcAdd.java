package com.example.configcenter;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.ApiResponse;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.ProtoClient;
import io.kubernetes.client.ProtoClient.ObjectOrStatus;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.auth.ApiKeyAuth;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.models.*;
import io.kubernetes.client.proto.Meta.ObjectMeta;
import io.kubernetes.client.proto.V1.Namespace;
import io.kubernetes.client.proto.V1.NamespaceSpec;
import io.kubernetes.client.proto.V1.Pod;
import io.kubernetes.client.proto.V1.PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.DatabaseConnection;
import com.example.model.PodListModel;
import com.example.model.PodModel;
import com.example.model.ServiceListModel;
import com.example.model.ServiceModel;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
//import java.util.HashMap;
import java.util.Map.Entry;


class containerInstance {
    private String image;
    private String name;
    private String command;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}


@RestController

// @CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/post")
public class ManagerSvcAdd {
    public static long startTime = 0L;
    public static long endTime = 0L;
    public static final String addPodFailed = "-----------------------------------\n"
            + "           Add Pod Failed          \n"
            + "-----------------------------------\n";
    public static final String addSvcFailed = "-----------------------------------\n"
            + "         Add Service Failed        \n"
            + "-----------------------------------\n";

    @RequestMapping(value = "/addPod", method = RequestMethod.POST)
    @ResponseBody
    public String addPod(@RequestBody Map<String, Object> podInfo) throws ApiException, IOException {
        System.out.println("-----------------------------------");
        System.out.println("              Add Pod              ");
        System.out.println("-----------------------------------");
        startTime = System.currentTimeMillis();
        String kubeConfigPath = "config";

        ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api(client);
        Boolean includeUninitialized = true;
        String pretty = "true"; // String | If 'true', then the output is pretty printed.
        String dryRun = null;

        V1Namespace namespace;
        String podName = null, podNamespace = "default";
        Map<String, String> labels = new HashMap<String, String>();
        String imageName = "nginx";
        String imagePullPolicy = "IfNotPresent";    //镜像拉取规则
        String containerName = "www";

//        List<String> list = new ArrayList<>();
////        list.add("/bin/sh");
//        list.add("sleep");
//        list.add("34000");
//
//        System.out.println(list.get(0));
//        System.out.println(list.get(1));

        List<String> commandList = new ArrayList<>();

        String defaultCommand = "/bin/bash cat \"helloworld\"";//sleep 36000
        String command = null;
        List<String> args = new LinkedList<>();

        List<Map<String, String>> containers = new ArrayList<>();
//1019
        List<Map<String, String>> ports = new ArrayList<>();

        Set<String> keys = (Set<String>) podInfo.keySet();
        for (String key : keys) {
            System.out.println(key + "\t" + podInfo.get(key));
        }
        try {
            if (podInfo.containsKey("name")) {
                podName = podInfo.get("name").toString();
            } else {
                endTime = System.currentTimeMillis();
                System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
                System.out.println(addPodFailed);
                return "false";
            }

            if (podInfo.containsKey("namespace")) {
                podNamespace = podInfo.get("namespace").toString();
                namespace = api.readNamespace(podNamespace, null, null, null);
            } else {
                endTime = System.currentTimeMillis();
                System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
                System.out.println(addPodFailed);
                return "false";
            }

            if (podInfo.containsKey("svclabelkey") && podInfo.get("svclabelkey").toString() != ""
                    && podInfo.containsKey("svclabelvalue") && podInfo.get("svclabelvalue").toString() != "") {
                labels.put(podInfo.get("svclabelkey").toString(), podInfo.get("svclabelvalue").toString());
            }

            if (podInfo.containsKey("containerrecords") && podInfo.get("containerrecords") != null) {
                Object containerObj = podInfo.get("containerrecords");
                System.out.println(containerObj.toString());
                if (containerObj instanceof List<?>) {
                    System.out.println("is a instance of List.");
                    for (Object o : (List<?>) containerObj) {
                        containers.add((Map<String, String>) o);
                    }
                }
            }

            if (podInfo.containsKey("portrecords") && podInfo.get("portrecords") != null) {
                Object portObj = podInfo.get("portrecords");
                System.out.println(portObj.toString());
                if (portObj instanceof List<?>) {
                    System.out.println("is a instance of List.");
                    for (Object o : (List<?>) portObj) {
                        ports.add((Map<String, String>) o);
                    }
                }
            }
            for (int i = 0; i < containers.size(); i++) {
                    String s = containers.get(i).get("command").toString() ;
            }
//
//            for (int i = 0; i < containers.size(); i++) {
//                for (int j = 0; j < ports.size(); j++) {
//                    if (containers.get(i).get("name").equals(ports.get(j).get("container")) ) {
//                        containers.get(i).putAll(ports.get(j));
//
//                    }
//                }
//            }
//            for(int i =0;i < containers.size();i++){
//                Set<String> a = (Set<String>) containers.get(i).keySet();
//                for (String key : a) {
//                    System.out.println(key + "\t" + containers.get(i).get(key));
//                }
//                System.out.println("\n");
//            }
//
//            if (podInfo.containsKey("image") && podInfo.get("image").toString() != "") {
//                imageName = podInfo.get("image").toString();
//            }

//            if (podInfo.containsKey("command") && podInfo.get("command").toString() != "") {
////                System.out.println("command111111="+podInfo.get("command").toString());
//                String commandString = podInfo.get("command").toString();
//                String[] commandSplit = commandString.split(" ");
////                List<String> list = new ArrayList<>();
////                List<String> commandlist = new ArrayList<>();
//                for(int i = 0;i < commandSplit.length;i++ ){
//                    commandList.add(commandSplit[i]);
//                    System.out.println(commandList.get(i));
//                }
//                if (commandList != null && commandList.size() != 0) {
//                    for (int index = 0; index < commandList.size(); index++) {
//                        if (index == 0) //command
//                        {
//                            command = commandSplit[index];
//                        } else {
//                            args.add(commandSplit[index]);
//                        }
//                    }
//                    for(int i = 0;i < commandList.size();i++ ){
//                        System.out.println(commandList.get(i));
//                    }
//                }else{
//                    commandList.add("sleep");
//                    commandList.add("34000");
//                }
//            }

            if (podInfo.containsKey("imagepullpolicy") && podInfo.get("imagepullpolicy").toString() != "") {
                imagePullPolicy = podInfo.get("imagepullpolicy").toString();
            }
        } catch (ApiException e) {
               System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
            System.err.println("Out Of Exception: " + e.getResponseBody());
            if (e.getCode() == 404) {
                System.out.println("Creating New Namespace: [ " + podNamespace + " ]");
                System.out.println("Loading...");
                startTime = System.currentTimeMillis();
                namespace = new V1NamespaceBuilder()
                        .withNewMetadata()
                        .withName(podNamespace)
                        .endMetadata()
                        .withNewApiVersion("v1")
                        .build();
                api.createNamespace(namespace, includeUninitialized, pretty, dryRun);
                endTime = System.currentTimeMillis();
                System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
                System.out.println("Create Namespace " + podNamespace + " Success.");
                System.out.println("Created.\n");
            }
        }

        try {
            V1Pod pod = null;
            V1PodBuilder podbuilder = new V1PodBuilder();
            podbuilder
                    .withNewMetadata()
                    .withName(podName)
                    .withLabels(labels)
                    .endMetadata();
            for (Map<String, String> ci : containers) {
                String localcommand = null;
                List<String> localargs = new LinkedList<>();
                String commandString = ci.get("command");
                String[] commandSplit = commandString.split(" ");
                if (commandSplit != null && commandSplit.length != 0) {
                    int splitLength = commandSplit.length;
                    for (int index = 0; index < splitLength; index++) {
                        System.out.println(commandSplit[index]);
                        localargs.add(commandSplit[index]);
                        /*if (index == 0) //command
//                        {
//                            localcommand = commandSplit[index];
//                        } else {
//                            localargs.add(commandSplit[index]);
//                        }

                         */
                    }
                }
//                for(int i = 0;i<localargs.size();i++){
//                    System.out.println("start      "+localargs.get(i));
//                }
                List<V1ContainerPort> portList=new ArrayList<>();
                for (Map<String, String> pi : ports){
                    if(pi.get("container").equals(ci.get("name"))){
                        V1ContainerPort v1ContainerPort = new V1ContainerPort();
                        v1ContainerPort.setContainerPort(Integer.parseInt(pi.get("start")));
                        v1ContainerPort.setHostPort(Integer.parseInt(pi.get("end")));
                        v1ContainerPort.setProtocol(pi.get("protocol"));
                        portList.add(v1ContainerPort);
                    }
                }


                podbuilder.editOrNewSpec()
                        .addNewContainer()
                        .withName(ci.get("name"))
                        .withImage(ci.get("image"))
                        .withImagePullPolicy(imagePullPolicy)
                        .withPorts(portList)
                        .withCommand(localargs)
                        .endContainer()
                        .endSpec();
            }
            pod = podbuilder.build();

            V1Pod v1pod = api.createNamespacedPod(podNamespace, pod, includeUninitialized, pretty, dryRun);
            endTime = System.currentTimeMillis();
            System.out.println("Create Pod " + v1pod.getMetadata().getName() + " Successful!");
            System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");

        }
        catch (ApiException e) {
            //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            endTime = System.currentTimeMillis();
            System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
            System.out.println(addPodFailed);
            return "false";
        }

        /* insert in database */


        System.out.println("-----------------------------------");
        System.out.println("            Add Pod Done           ");
        System.out.println("-----------------------------------");

        return "true";
    }

    // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/addService", method = RequestMethod.POST)
    @ResponseBody
    public String addService(@RequestBody Map<String, String> svcInfo) throws ApiException, IOException {
        System.out.println("-----------------------------------");
        System.out.println("            Add Service            ");
        System.out.println("-----------------------------------");
        startTime = System.currentTimeMillis();
        String kubeConfigPath = "config";

        ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api(client);
        String pretty = "true"; // String | If 'true', then the output is pretty printed.
        String dryRun = "All";
        Boolean includeUninitialized = true;
        Map<String, String> labels = new HashMap<String, String>();

        V1Namespace namespace;
        String svcName = null, svcNamespace = "default";
        String sessionAffinity = null;
        String type = "ClusterIP";
        String portName = "client";
        String protocol = "TCP";
        Integer port = 80;
        Integer targetPort = 8080;
        Integer nodePort = 8080;

        try {
            if (svcInfo.containsKey("name")) {
                svcName = svcInfo.get("name").toString();
            } else {
                endTime = System.currentTimeMillis();
                System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
                System.out.println(addSvcFailed);
                return "false";
            }

            if (svcInfo.containsKey("namespace")) {
                svcNamespace = svcInfo.get("namespace").toString();
                namespace = api.readNamespace(svcNamespace, null, null, null);
            } else {
                endTime = System.currentTimeMillis();
                System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
                System.out.println(addSvcFailed);
                return "false";
            }

            if (svcInfo.containsKey("labelkey") && svcInfo.get("labelkey").toString() != ""
                    && svcInfo.containsKey("labelvalue") && svcInfo.get("labelvalue").toString() != "") {
                labels.put(svcInfo.get("labelkey").toString(), svcInfo.get("labelvalue").toString());
            }

            if (svcInfo.containsKey("type") && svcInfo.get("type").toString() != "") {
                type = svcInfo.get("type").toString();
            }

            if (svcInfo.containsKey("sessionaffinity") && svcInfo.get("sessionaffinity").toString() != "") {
                sessionAffinity = svcInfo.get("sessionaffinity").toString();
            }

            if (svcInfo.containsKey("portname") && !svcInfo.get("portname").equals("")) {
                portName = svcInfo.get("portname").toString();
            }

            if (svcInfo.containsKey("protocol") && !svcInfo.get("protocol").equals("")) {
                protocol = svcInfo.get("protocol").toString();
            }

            if (svcInfo.containsKey("port") && !svcInfo.get("port").equals("")) {
                port = Integer.valueOf(svcInfo.get("port").toString());
            }

            if (svcInfo.containsKey("targetport") && !svcInfo.get("targetport").equals("")) {
                targetPort = Integer.valueOf(svcInfo.get("targetport").toString());
            }

            if (svcInfo.containsKey("nodeport") && !svcInfo.get("nodeport").equals("")) {
                nodePort = Integer.valueOf(svcInfo.get("nodeport").toString());
            }

//            nodePort = (nodePort < 30001) ? 30001 : nodePort;
//            nodePort = (nodePort > 32766) ? 32766 : nodePort;
//            System.out.println(type + " " + nodePort + " " + port);

        } catch (ApiException e) {
            //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
            System.err.println("Out Of Exception: " + e.getResponseBody());
//            System.err.println("Response headers: " + e.getResponseHeaders());
            if (e.getCode() == 404) {
                System.out.println("Creating New Namespace: [ " + svcNamespace + " ]");
                System.out.println("Loading...");
                namespace = new V1NamespaceBuilder()
                        .withNewMetadata()
                        .withName(svcNamespace)
                        .endMetadata()
                        .withNewApiVersion("v1")
                        .build();
                api.createNamespace(namespace, includeUninitialized, pretty, dryRun);
                endTime = System.currentTimeMillis();
                System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
                System.out.println("Create Namespace " + svcNamespace + " Success.");
                System.out.println("Created.\n");
            }
        }

        try {
            startTime = System.currentTimeMillis();
            V1ServiceBuilder serviceBuilder = new V1ServiceBuilder()
                    .withApiVersion("v1")
                    .withKind("Service")
                    .withNewMetadata()
                    .withName(svcName)
                    .withLabels(labels)
                    .endMetadata();

            if (type.equals("NodePort") || type.equals("LoadBalancer")) {
                serviceBuilder
                        .withNewSpec()
                        .withSessionAffinity(sessionAffinity)
                        .withSelector(labels)
                        .withType(type)
                        .addNewPort()
                        .withProtocol(protocol)
                        .withName(portName)
                        .withPort(port)
//                        .withNodePort(nodePort)
                        .withTargetPort(new IntOrString(targetPort))
                        .endPort()
                        .endSpec();
            } else {
                serviceBuilder
                        .withNewSpec()
                        .withSessionAffinity(sessionAffinity)
                        .withSelector(labels)
                        .withType(type)
                        .addNewPort()
                        .withProtocol(protocol)
                        .withName(portName)
                        .withPort(port)
                        .withTargetPort(new IntOrString(targetPort))
                        .endPort()
                        .endSpec();
            }

            V1Service svc = serviceBuilder.build();

            V1Service v1service = api.createNamespacedService(svcNamespace, svc, null, pretty, null);
            /* insert into database */
            /*
            DatabaseConnection databaseConnection = new DatabaseConnection();
            if (!databaseConnection.establishConnection()) {
                return "false";
            }

            databaseConnection.insertIntoDatabase(v1service);
//		  if(!databaseConnection.insertIntoDatabase(v1service)) {
//  			databaseConnection.closeConnection();
//			return "false";
//		  }

            databaseConnection.closeConnection();
    */
            endTime = System.currentTimeMillis();
            System.out.println("Create Service " + v1service.getMetadata().getName() + " Successful!");
            System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");

        } catch (Exception e) {
            //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
            //   System.err.println("Status code: " + e.getCode());
            //   System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
            endTime = System.currentTimeMillis();
            System.out.println("\nTime spend: " + (endTime - startTime) + " miliseconds.\n");
            System.out.println(addSvcFailed);
            return "false";
        }


        /* add if REAL SERVICE exist */
        long startTimeOfRealSvc = System.currentTimeMillis();

        Map<String, String> realLabels = new HashMap<String, String>();

        String realSvcName = null, realSvcNamespace = svcNamespace;
        String realType = type;
        String realPortName = portName;
        String realProtocol = protocol;
        Integer realPort = port;
        Integer realTargetPort = targetPort;
        Integer realNodePort = nodePort;

//        Set<String> keys = (Set<String>)svcInfo.keySet();
//        for(String key : keys)
//        {
//            System.out.println(key + "\t" + svcInfo.get(key));
//        }
        try {
            if (svcInfo.containsKey("rname") && !svcInfo.get("rname").equals("")) {
                realSvcName = svcInfo.get("rname").toString();
                System.out.println("realSvcName: " + realSvcName);
                /*
                if (svcInfo.containsKey("rnamespace") && !svcInfo.get("rnamespace").equals(""))
                {
                    realSvcNamespace = svcInfo.get("rnamespace").toString();
                }
                */

                if (svcInfo.containsKey("rtype") && !svcInfo.get("rtype").equals("")) {
                    realType = svcInfo.get("rtype").toString();
                }

                if (svcInfo.containsKey("rprotocol") && !svcInfo.get("rprotocol").equals("")) {
                    realProtocol = svcInfo.get("rprotocol").toString();
                }

                if (svcInfo.containsKey("rport") && !svcInfo.get("rport").equals("")) {
                    realPort = Integer.valueOf(svcInfo.get("rport").toString());
                }

                if (svcInfo.containsKey("rtargetport") && !svcInfo.get("rtargetport").equals("")) {
                    realTargetPort = Integer.valueOf(svcInfo.get("rtargetport").toString());
                }

                if (svcInfo.containsKey("rnodeport") && !svcInfo.get("rnodeport").equals("")) {
                    realNodePort = Integer.valueOf(svcInfo.get("rnodeport").toString());
                }

//                realNodePort = (realNodePort < 30001) ? 30001 : realNodePort;
//                realNodePort = (realNodePort > 32766) ? 32766 : realNodePort;
//                System.out.println(realType + " " + realNodePort + " " + realPort);

                if (svcInfo.containsKey("rlabelkey") && !svcInfo.get("rlabelkey").equals("")
                        && svcInfo.containsKey("rlabelvalue") && !svcInfo.get("rlabelvalue").equals("")) {
                    realLabels.put(svcInfo.get("labelkey").toString(), svcInfo.get("rlabelvalue").toString());
                } else {
                    realLabels = labels;
                }

                V1ServiceBuilder serviceBuilder = new V1ServiceBuilder()
                        .withApiVersion("v1")
                        .withKind("Service")
                        .withNewMetadata()
                        .withName(realSvcName)
                        .withLabels(realLabels)
                        .endMetadata();

                if (realType.equals("NodePort") || realType.equals("LoadBalancer")) {
                    serviceBuilder
                            .withNewSpec()
                            .withSessionAffinity(sessionAffinity)
                            .withSelector(realLabels)
                            .withType(realType)
                            .addNewPort()
                            .withProtocol(realProtocol)
                            .withName(realPortName)
                            .withPort(realPort)
//                            .withNodePort(realNodePort)
                            .withTargetPort(new IntOrString(realTargetPort))
                            .endPort()
                            .endSpec();
                } else {
                    serviceBuilder
                            .withNewSpec()
                            .withSessionAffinity(sessionAffinity)
                            .withSelector(realLabels)
                            .withType(realType)
                            .addNewPort()
                            .withProtocol(realProtocol)
                            .withName(realPortName)
                            .withPort(realPort)
                            .withTargetPort(new IntOrString(realTargetPort))
                            .endPort()
                            .endSpec();
                }

                V1Service svc = serviceBuilder.build();

                V1Service v1service = api.createNamespacedService(realSvcNamespace, svc, null, pretty, null);

                endTime = System.currentTimeMillis();
                System.out.println("Create Real Service " + v1service.getMetadata().getName() + " Successful!");
                System.out.println("\nTime spend: " + (endTime - startTimeOfRealSvc) + " miliseconds.\n");
            }
        } catch (Exception e) {
            //   System.err.println("Exception when calling CoreV1Api#listNamespacedPod");
            //   System.err.println("Status code: " + e.getCode());
            //   System.err.println("Reason: " + e.getResponseBody());
            e.printStackTrace();
            endTime = System.currentTimeMillis();
            System.out.println("\nTime spend: " + (endTime - startTimeOfRealSvc) + " miliseconds.\n");
            System.out.println(addSvcFailed);
            return "false";
        }

        System.out.println("-----------------------------------");
        System.out.println("          Add Service Done         ");
        System.out.println("-----------------------------------");

        return "true";
    }
}
