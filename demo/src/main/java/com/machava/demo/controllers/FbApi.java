package com.machava.demo.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.stereotype.Service;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

@Service
public class FbApi {

    private static String apiUrl = "https://graph.facebook.com/v3.3/";

    // PhotoDto me/photos?fields=id,name,picture,link
    // UserDto me
    // Debug FbToken debug_token?input_token={{FbToken}}&access_token=1344964768940939|_c_00YpU-mhtlroMpAdN2ftV_w8

    static Boolean verifyToken(String fbToken) {
        if (fbToken == null || fbToken.equals("")) {
            throw new IllegalArgumentException("Token cannot be null!");
        }

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
        //scopesJsonArray.toList().forEach(item -> scopesList.add(item.toString()));

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
    public void testPermissions() {
        String invalidToken = "EAATHPQNJw4sBAEmwEds3NNiOA8vkBLH3dx0ldeS9nUeu879b3Gw9xsSHUIBXuZCNstmVGPQZA6ORoZCWjIZCRbJ06XuZC7vCAZBWdHPxDTsOxLn5afaoS1c1yfDYU4QOOruDax3ZBrQeNDZBJ84ITvD6SHE5ZCybSuY4aOyaZCs0uP81isK3Iwe8PJLd1yZC3CbDZAoZD";
        String validToken = "EAATHPQNJw4sBAAI4dOAiuYbzq70Oe0ZAljvHrmUCZC5bKokGesTiMgCvdjwaDpSxqafAKmFT5iP2sSgKXMu0gJ0R0mjMZALE2plewuqwV3wEiWT1gBrJlkZAGZAZBIZAJ7HrkCq8VddxEagPZAqMg5gpfu93gaFAwiauMzhmZC6uShCSDIV3RWZCtduVMxPzrGZCLAZD";

        assertFalse(this.verifyToken(invalidToken));
        assertTrue(this.verifyToken(validToken));
    }

}
