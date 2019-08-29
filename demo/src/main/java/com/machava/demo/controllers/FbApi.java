package com.machava.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.machava.demo.entities.Photo;
import com.machava.demo.entities.User;
import com.machava.demo.managers.FbApiManager;
import com.machava.demo.managers.FbException;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

@Service
public class FbApi {

    public static Boolean verifyToken(String fbToken) throws FbException {

        List<String> requiredPermissionsList = List.of("public_profile","user_photos");

        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(FbApiManager.apiUrl + "debug_token")
                    .queryString("input_token", fbToken)
                    .queryString("access_token", "1344964768940939|_c_00YpU-mhtlroMpAdN2ftV_w8")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
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

        if (response.getBody().getObject().has("error")) {
            throw FbApiManager.catchFbError(response);
        } else if (responseObject.has("error")) {
            throw new FbException("FB error says: " + responseObject.getJSONObject("error").getString("message"));
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

    public static User getUserDetails(String fbToken) throws Exception {

        User user;

        if (!FbApi.verifyToken(fbToken)) {
            throw new FbException("Token is invalid.");
        }

        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(FbApiManager.apiUrl + "me")
                    .queryString("fields", "id,name,gender,picture.width(800)")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        JSONObject responseObject = response.getBody().getObject();

        if (responseObject.has("error")) {
            throw FbApiManager.catchFbError(response);
        } else if (response.getStatus() == 200) {

            user = User.builder()
                    .id(responseObject.getLong("id"))
                    .name(responseObject.getString("name"))
                    .gender(responseObject.getString("gender"))
                    .picture(responseObject.getJSONObject("picture").getJSONObject("data").getString("url"))
                    .build();
        } else {
            throw new Exception("Unexpected Error occurred.");
        }

        return user;
    }

    public static List<Photo> getUserPhotos(String fbToken) throws Exception {

        HttpResponse<JsonNode> response;

        try {
            response = Unirest.get(FbApiManager.apiUrl + "me/photos")
                    .queryString("fields", "id,name,link,picture.width(800)")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject responseObject = response.getBody().getObject();

        if (responseObject.has("error")) {
            throw FbApiManager.catchFbError(response);
        } else if (response.getStatus() == 200) {
            return FbApiManager.mapPhotoResponseToList(responseObject, fbToken);
        } else {
            throw new Exception("Unexpected error occurred.");
        }
    }

}
