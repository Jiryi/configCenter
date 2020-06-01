package com.example.model;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


@Component    
@PropertySource(value = "classpath:application.properties")
public class ThirdParty {
    private String podStatus;     //pod状态信息，以字符串的形式存储，通过访问url返回值更新

    @Value("${podStatusUrl}")
    private String podStatusUrl;

    public String getPodStatusUrl() {
        return this.podStatusUrl;
    }

    public String getPodStatus() {
        return this.podStatus;
    }

    public String updatePodStatus() {
//        HttpMethod method = HttpMethod.GET;
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<~>();
        System.out.println(this.podStatusUrl);
        try {
        	RestTemplate template = new RestTemplate();
        	ResponseEntity<String> response = template.getForEntity(this.podStatusUrl, String.class);
        	this.podStatus = response.getBody();
        } catch(Exception e)
        {
        	System.out.println(e);
        	this.podStatus = "";
        }
        return this.podStatus;
    }

        public String init() {
        Properties prop = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream("thirdParty.properties"));

            prop.load(in);
            in.close();

            String key = "podStatusUrl";
            if(prop.getProperty(key) == null || prop.getProperty(key) == "") {
                System.out.println("cannot resolve properties: \"thirdParty.properties\". ");
                return "";
            }

            System.out.println(prop.getProperty(key));

            return prop.getProperty(key);
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return "";
    }

} 