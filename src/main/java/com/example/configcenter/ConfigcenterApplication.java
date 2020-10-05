package com.example.configcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

@SpringBootApplication
public class ConfigcenterApplication {

    public static void main(String[] args) {
    	
//        try {
//            Yaml yaml = new Yaml();
////            URL url = ConfigcenterApplication.class.getClassLoader().getResource("jar:file:/D:/configCenter-dev/configCenter/target/classes/test.yaml");
////            FileInputStream file = new FileInputStream(inputstream);
////            System.out.println(ConfigcenterApplication.class.getClassLoader().getResource(""));
////            String yamlPath = ResourceUtils.getURL("classpath:").getPath();
////            System.out.println(url);
//            URL url = ConfigcenterApplication.class.getClassLoader().getResource("/test.yaml");
//            System.out.println(url);
//            if (url != null) {
////                Object obj =yaml.load(new FileInputStream(url.getFile()));
////                System.out.println(obj);
//            	FileInputStream file = new FileInputStream("D:/configCenter-dev/configCenter/target/classes/test.yaml");
//
//            	System.out.println("Find test.yaml");
//            	Map map =(Map)yaml.load(file);
//                System.out.println(map);
//
//            } else
//            {
//            	System.out.println("cannot find test.yaml");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    	
        SpringApplication.run(ConfigcenterApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
                registry.addMapping("/**").allowedOrigins("http://121.199.28.228:8088");
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

}
