package com.machava.demo.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.machava.demo.dtos.UserDto;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

@Service
public class FbApi {
    String invalidToken = "EAATHPQNJw4sBAEmwEds3NNiOA8vkBLH3dx0ldeS9nUeu879b3Gw9xsSHUIBXuZCNstmVGPQZA6ORoZCWjIZCRbJ06XuZC7vCAZBWdHPxDTsOxLn5afaoS1c1yfDYU4QOOruDax3ZBrQeNDZBJ84ITvD6SHE5ZCybSuY4aOyaZCs0uP81isK3Iwe8PJLd1yZC3CbDZAoZD";
    String validToken = "EAATHPQNJw4sBAI018NoKnpVAH0onaHFiqdw83FFVt9kzb7sOminLLrYgS41nYfJf09xIMVbQ4ofb3TiHR9jYM4FCOdDvKf6oYOGcJpdCZA5H91cGYPd48qZAZC1M6CEVciA1TAwypnsk1lhZB922yLHBVCUiFN0yu5Ods0TlQ8c6sqkNgB24DNPskXt5YEoZD";


    private static String apiUrl = "https://graph.facebook.com/v3.3/";

    // PhotoDto me/photos?fields=id,name,picture,link
    // UserDto me?fields=id,name
    // Debug FbToken debug_token?input_token={{FbToken}}&access_token=1344964768940939|_c_00YpU-mhtlroMpAdN2ftV_w8

    static Boolean verifyToken(String fbToken) {

        List<String> requiredPermissionsList = List.of("public_profile","user_photos");

        HttpResponse<JsonNode> resposne = null;

        try {
            resposne = Unirest.get(apiUrl + "debug_token")
                    .queryString("input_token", fbToken)
                    .queryString("access_token", "1344964768940939|_c_00YpU-mhtlroMpAdN2ftV_w8")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        assert resposne != null;
        JSONObject responseObject = resposne.getBody().getObject().getJSONObject("data");

        boolean isTokenValid = responseObject.getBoolean("is_valid");
        JSONArray scopesJsonArray = responseObject.getJSONArray("scopes");

        List<String> scopesList = new ArrayList<>();
        for (int i = 0; i < scopesJsonArray.length(); i++) {
            scopesList.add(scopesJsonArray.getString(i));
        }

        boolean permissions = scopesList.containsAll(requiredPermissionsList);

        if (responseObject.has("error")) {
            System.out.println(responseObject.getJSONObject("error").getString("message"));
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

    @Test
    public void testToken() {
        assertFalse(verifyToken(invalidToken));
        assertTrue(verifyToken(validToken));
    }

    static UserDto getUserInfo(String fbToken) throws IOException {

        HttpResponse<JsonNode> resposne = null;

        try {
            resposne = Unirest.get(apiUrl + "me")
                    .queryString("fields", "id,name")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        assert resposne != null;
        JSONObject responseObject = resposne.getBody().getObject();

        if (responseObject.has("error")) {
            System.out.println(responseObject.getJSONObject("error").getString("message"));
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            UserDto userDto = mapper.readValue(String.valueOf(responseObject), new TypeReference<UserDto>() {});
            return userDto;
        }
    }

    @Test
    public void testUserInfo() throws IOException {
        assertNotNull(getUserInfo(validToken).getId());
        assertNotNull(getUserInfo(validToken).getName());
    }

}
