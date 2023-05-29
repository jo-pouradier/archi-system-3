package fr.dtos.common.utils;

import fr.dtos.common.auth.AuthType;
import fr.dtos.common.card.CardDTO;
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
import java.util.UUID;

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

    /**
     *
     * @param service from EServices
     * @param path uri of the service
     * @param data body of the request
     * @param responseType type of the response as dtos
     * @param method HttpMethod (GET, POST, PUT, DELETE)
     * @return the response as dtos
     * @param <T> Response type
     * @param <U> body type as dtos
     */
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
                System.out.println("Request service " + service.getName()+"("+path+"): "+ responseBody);
                return responseBody;
            } else {
                System.out.println("La requête a échoué avec le code : " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Request service " + service.getName()+"("+path+"): "+"Erreur dans la requête : " + e.getMessage());
        }
        return null;
    }
    public static <T, U> T requestService(EServices service, String path, U data, Class<T> responseType) {
        return requestService(service, path, data, responseType, HttpMethod.GET);
    }
    public static boolean isUserKey(String uuid) {
        UserDTO userDTO = getUser(uuid);
        return userDTO != null;
    }

    public static UserDTO getUser(String uuid) {
        return requestService(EServices.USER_SERVICE, "getUser/"+uuid, null, UserDTO.class);
    }
    public static UserDTO getUser(UUID uuid) {
        return getUser(uuid.toString());
    }
    public static CardDTO getCard(UUID uuid) {
        if (uuid == null) return null;
        return getCard(uuid.toString());
    }
    public static CardDTO getCard(String uuid) {
        return requestService(EServices.CARD_SERVICE, "getCard/"+uuid, null, CardDTO.class);
    }

    public static UserDTO getUserByEmail(String email) {
        return requestService(EServices.USER_SERVICE, "getUserByEmail/"+email, null, UserDTO.class);
    }

    public static UserDTO createUser(UserDTO user) {
        return requestService(EServices.USER_SERVICE, "createUser/", user, UserDTO.class, HttpMethod.POST);
    }

    public static CardDTO[] getOwnerCards(String uuid){
        return requestService(EServices.CARD_SERVICE, "getCardsByOwner/"+uuid, null, CardDTO[].class);
    }

    public static void debit(UUID uuid, float price) {
        requestService(EServices.USER_SERVICE, "debit?uuid="+uuid+"&amount="+price, null, UserDTO.class, HttpMethod.GET);
    }

    public static void depot(UUID uuid, float price) {
        requestService(EServices.USER_SERVICE, "depot?uuid="+uuid+"&amount="+price, null, UserDTO.class, HttpMethod.GET);
    }

    public static void changeOwner(CardDTO card, UserDTO to) {
        requestService(EServices.CARD_SERVICE, "changeOwner/"+card.getUuid()+"/"+to.getUUID(), null, CardDTO.class, HttpMethod.GET);
    }
}
