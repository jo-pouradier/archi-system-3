package fr.dtos.common.utils;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Utils {

    private final static String apiSuperAdminKey = "tp";

    public static String getApiSuperAdminKey() {
        return apiSuperAdminKey;
    }

    public static boolean isSuperAdminKey(String key) {
        return apiSuperAdminKey.equals(key);
    }

    public static String getDefaultUrl(){
        return "http://localhost:8084";
    }

    public static <T, U> T requestService(EServices service, String path, U data, Class<T> responseType) {
        try{
            RestTemplate restTemplate = new RestTemplateBuilder().build();
//        String url = getDefaultUrl() + "/" + service.getMap() + "/" + path;
            String url = getDefaultUrl() + "/" + path;
            HttpEntity<U> request = new HttpEntity<U>(data);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, request, responseType);

            if (response.getStatusCode().is2xxSuccessful()) {
                T responseBody = response.getBody();
                System.out.println(responseBody);
                return responseBody;
            } else {
                System.out.println("La requête a échoué avec le code : " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Erreur dans la requête : " + e.getMessage());
        }
        return null;
    }



}
