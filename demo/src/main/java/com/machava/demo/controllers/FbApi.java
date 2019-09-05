package com.machava.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.machava.demo.entities.Photo;
import com.machava.demo.entities.User;
import com.machava.demo.managers.FbException;
import com.machava.demo.managers.api.FbApiManager;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FbApi {

    @Autowired
    FbApiManager fbApiManager;

    public String verifyToken(String fbToken) {

        List<String> requiredPermissionsList = List.of("public_profile","user_photos","user_gender","email");

        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(fbApiManager.apiUrl + "debug_token")
                    .queryString("input_token", fbToken)
                    .queryString("access_token", "1344964768940939|_c_00YpU-mhtlroMpAdN2ftV_w8")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        JSONObject responseObject = response.getBody().getObject().getJSONObject("data");

        boolean isTokenValid = responseObject.getBoolean("is_valid");
        JSONArray scopesJsonArray = responseObject.getJSONArray("scopes");

        List<String> scopesList = new ArrayList<>();
        for (int i = 0; i < scopesJsonArray.length(); i++) {
            scopesList.add(scopesJsonArray.getString(i));
        }

        boolean permissions = scopesList.containsAll(requiredPermissionsList);

        if (response.getBody().getObject().has("error")) {
            return "Response code: " + response.getStatusText() +
                    "FB error says: " + response.getBody().getObject().getJSONObject("error").getString("message");
        } else if (!isTokenValid) {
            return "Token is invalid";
        } else if (responseObject.has("error")) {
            return "FB error says: " + responseObject.getJSONObject("error").getString("message");
        } else if (!permissions) {
            return "Token is missing permission:" + requiredPermissionsList.removeAll(scopesList);
        } else {
            return null;
        }
    }

    public User getUserDetails(String fbToken) throws Exception {

        User user;
        String tokenError = verifyToken(fbToken);

        if (tokenError != null) {
            throw new FbException(tokenError);
        }

        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(fbApiManager.apiUrl + "me")
                    .queryString("fields", "id,name,gender,picture.width(800)")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        JSONObject responseObject = response.getBody().getObject();

        if (responseObject.has("error")) {
            throw fbApiManager.catchFbError(response);
        } else if (response.getStatus() == 200) {

            user = User.builder()
                    .id(responseObject.getLong("id"))
                    .name(responseObject.getString("name"))
                    .gender(responseObject.getString("gender"))
                    .picture(responseObject.getJSONObject("picture").getJSONObject("data").getString("url"))
                    .build();
        } else {
            throw new Exception("Unexpected Error has occurred.");
        }

        return user;
    }

    public List<Photo> getUserPhotos(String fbToken) throws Exception {

        HttpResponse<JsonNode> response;

        try {
            response = Unirest.get(fbApiManager.apiUrl + "me/photos")
                    .queryString("fields", "id,name,link,picture.width(800)")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }

        JSONObject responseObject = response.getBody().getObject();

        if (responseObject.has("error")) {
            throw fbApiManager.catchFbError(response);
        } else if (response.getStatus() == 200) {
            return fbApiManager.mapPhotoResponseToList(responseObject, fbToken);
        } else {
            throw new Exception("Unexpected error occurred.");
        }
    }

}
