package com.machava.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.machava.demo.entities.Photo;
import com.machava.demo.entities.User;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

@Service
public class FbApi {

    // long lived access token:
    // /oauth/access_token?grant_type=fb_exchange_token&client_id=1344964768940939&client_secret=4821c6cf25b5abb3f0211025ee698e22&fb_exchange_token=EAATHPQNJw4sBALcUpccDfmZBFC3ZCFKaUZCH1rBg2FYDdMahiBZAiw8AjsZAguJ7RV0Om9ZAuf1008oC65ds6coxjIsZCxPkYEolQl2EDvsMvELcn9zfFgg2u2BNVakvoGwVQ4OMVoXKw2jawN1R77YCOCee1NPrlZBLbHinGgZB5IQ0wZBGMVFCm1uuOzre6kuB0ZD

    private static String apiUrl = "https://graph.facebook.com/v3.3/";

    // PhotoDto me/photos?fields=id,picture.width(800)
    // UserDto me?fields=id,name
    // Debug FbToken debug_token?input_token={{FbToken}}&access_token=1344964768940939|_c_00YpU-mhtlroMpAdN2ftV_w8

    public static Boolean verifyToken(String fbToken) {

        List<String> requiredPermissionsList = List.of("public_profile","user_photos");

        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(apiUrl + "debug_token")
                    .queryString("input_token", fbToken)
                    .queryString("access_token", "1344964768940939|_c_00YpU-mhtlroMpAdN2ftV_w8")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }

        assert response != null;
        JSONObject responseObject = response.getBody().getObject().getJSONObject("data");

        boolean isTokenValid = responseObject.getBoolean("is_valid");
        JSONArray scopesJsonArray = responseObject.getJSONArray("scopes");

        List<String> scopesList = new ArrayList<>();
        for (int i = 0; i < scopesJsonArray.length(); i++) {
            scopesList.add(scopesJsonArray.getString(i));
        }

        boolean permissions = scopesList.containsAll(requiredPermissionsList);

        if (responseObject.has("error")) {
            System.out.println(responseObject.getJSONObject("error").getString("message")); // TODO change outputs to exceptions
            return false;
        } else if (!isTokenValid) {
            System.out.println("Token is invalid");
            return false;
        } else if (!permissions) {
            System.out.println("Missing permissions");
            return false;
        } else {
            return true;
        }
    }

    public static User getUserDetails(String fbToken) throws IOException {

        User user = null;
        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(apiUrl + "me")
                    .queryString("fields", "id,name,gender,picture.width(800)")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        JSONObject responseObject = response.getBody().getObject();

        if (responseObject.has("error")) {
            throw new IllegalArgumentException("Response code: " + response.getStatusText() +
                    "FB error says: " + responseObject.getJSONObject("error").getString("message"));
        } else if (response.getStatus() == 200) {

            user = User.builder()
                    .id(responseObject.getLong("id"))
                    .name(responseObject.getString("name"))
                    .gender(responseObject.getString("gender"))
                    .picture(responseObject.getJSONObject("picture").getJSONObject("data").getString("url"))
                    .build();
        } else {
            throw new IllegalArgumentException("Unexpected Error occurred.");
        }

        return user;
    }

    public static List<Photo> getUserPhotos(String fbToken) {

        HttpResponse<JsonNode> response;

        try {
            response = Unirest.get(apiUrl + "me/photos")
                    .queryString("fields", "id,picture.width(800)")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject responseObject = response.getBody().getObject();

        if (responseObject.has("error")) {
            throw new IllegalArgumentException("Response code: " + response.getStatusText() +
                    "FB error says: " + responseObject.getJSONObject("error").getString("message"));
        } else if (response.getStatus() == 200) {
            List<Photo> photoList = null;
            JSONArray responseArray = responseObject.getJSONArray("data");

            ObjectMapper mapper = new ObjectMapper();
            try {
                photoList = mapper.readValue(String.valueOf(responseArray), new TypeReference<List<Photo>>() {});
            } catch (IOException e) {
                System.out.println("Error parsing JSON Array to List<PhotoDto>: " + e);
            }

            return photoList;
        } else {
            throw new IllegalArgumentException("Unexpected error occurred.");
        }
    }

}
