package com.example.model;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Period;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;


@Component    
@PropertySource("thirdParty.properties")
public class ThirdParty {
    private String podStatus;     //pod状态信息，以字符串的形式存储，通过访问url返回值更新

    @Value("${thirdParty.podStatusUrl}")
    private String podStatusUrl;

    public String getPodStatusUrl() {
        return this.podStatusUrl;
    }

    public String getPodStatus() {
        return this.podStatus;
    }

    @Service
    public String updatePodStatus() {
        HttpMethod method = HttpMethod.GET;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<~>();
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.getForEntity(this.podStatusUrl, String.class);
        this.podStatus = response.getBody();

        return this.podStatus;
    }

        public string init() {
        Properties prop = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream("thirdParty.properties"));

            prop.load(in);
            in.close();

            String key = "podStatusUrl";
            if(prop.getProperty(key) == null || prop.getProperty(key) == "") {
                Syetem.out.println("cannot resolve properties: \"thirdParty.properties\". ");
                return "";
            }

            Syetem.out.println(prop.getProperty(key));

            return prop.getProperty(key);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

} 