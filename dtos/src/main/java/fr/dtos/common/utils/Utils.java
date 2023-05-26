package fr.dtos.common.utils;

import fr.dtos.common.auth.AuthType;
import fr.dtos.common.user.UserDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
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
        return "http://localhost:8000";
    }

    public static <T, U> T requestService(EServices service, String path, U data, Class<T> responseType, HttpMethod method) {
        try{
            RestTemplate restTemplate = new RestTemplate();
//        String url = getDefaultUrl() + "/" + service.getMap() + "/" + path;
            String url = getDefaultUrl() + "/" + service.getMap() + "/" + path;
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.COOKIE,"user=" + Utils.getApiSuperAdminKey());
            headers.add(HttpHeaders.ACCEPT,"application/json");
            HttpEntity<U> request = new HttpEntity<U>(data,headers);
            RequestEntity<U> requestEntity = new RequestEntity<U>(data,headers,method, new URL(url).toURI());
            // set cookie
            //request.getHeaders().add("Cookie", "user=" + Utils.getApiSuperAdminKey());

            ResponseEntity<T> response = restTemplate.exchange(url, method, request, responseType);

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
    public static <T, U> T requestService(EServices service, String path, U data, Class<T> responseType) {
        return requestService(service, path, data, responseType, HttpMethod.GET);
    }
    public static boolean isUserKey(String uuid) {
        UserDTO userDTO = requestService(EServices.USER_SERVICE, "getUser/"+uuid, null, UserDTO.class);
        return userDTO != null;
    }
}
