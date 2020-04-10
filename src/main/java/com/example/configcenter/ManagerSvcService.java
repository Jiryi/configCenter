package com.example.configcenter;

import jdk.nashorn.api.scripting.JSObject;

import java.util.List;

public interface ManagerSvcService {
    List<String> showServices();
    List<String> showPods();
}
