package com.machava.demo.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.machava.demo.dtos.PhotoDto;
import com.machava.demo.dtos.UserDto;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

@Service
public class FbApi {
    String invalidToken = "EAATHPQNJw4sBAEmwEds3NNiOA8vkBLH3dx0ldeS9nUeu879b3Gw9xsSHUIBXuZCNstmVGPQZA6ORoZCWjIZCRbJ06XuZC7vCAZBWdHPxDTsOxLn5afaoS1c1yfDYU4QOOruDax3ZBrQeNDZBJ84ITvD6SHE5ZCybSuY4aOyaZCs0uP81isK3Iwe8PJLd1yZC3CbDZAoZD";
    String validToken = "EAATHPQNJw4sBAG11QKKcny1zm4lejXtBKZCkLIIKSICsXeStsAaUHHuvf7Gwu8Mi6zJaGEui0qwzoJh6L5PQGInvCWdncV39vK94ATszDdiBrjZAUALT1nSuw4TzQUcR64qMbuQbZBfEmZAAUrw0VNvUHBHb2ZBKKtsjSMG7VGJUiKP6CZCwLLZAP7zE4UNZBfUZD";

    // long lived access token:
    // /oauth/access_token?grant_type=fb_exchange_token&client_id=1344964768940939&client_secret=4821c6cf25b5abb3f0211025ee698e22&fb_exchange_token=EAATHPQNJw4sBALcUpccDfmZBFC3ZCFKaUZCH1rBg2FYDdMahiBZAiw8AjsZAguJ7RV0Om9ZAuf1008oC65ds6coxjIsZCxPkYEolQl2EDvsMvELcn9zfFgg2u2BNVakvoGwVQ4OMVoXKw2jawN1R77YCOCee1NPrlZBLbHinGgZB5IQ0wZBGMVFCm1uuOzre6kuB0ZD

    private static String apiUrl = "https://graph.facebook.com/v3.3/";

    // PhotoDto me/photos?fields=id,picture.width(800)
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
            return null;
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

    @Test
    public void testToken() {
        assertFalse(verifyToken(invalidToken));
        assertTrue(verifyToken(validToken));
    }

    static UserDto getUserInfo(String fbToken) throws IOException {

        HttpResponse<JsonNode> resposne = null;

        try {
            resposne = Unirest.get(apiUrl + "me")
                    .queryString("fields", "id,name,gender,picture.width(800)")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        assert resposne != null;
        JSONObject responseObject = resposne.getBody().getObject();

        if (responseObject.has("error")) {
            System.out.println("FB error says: " + responseObject.getJSONObject("error").getString("message"));
            return null;
        } else {
            byte[] imageInByte = null;

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                BufferedImage originalImage = ImageIO.read(new URL(responseObject.getJSONObject("picture").getJSONObject("data").getString("url")));
                ImageIO.write(originalImage, "jpg", baos);
                baos.flush();
                imageInByte = baos.toByteArray();
            } catch (IOException e) {
                System.out.println("Could not download profile image: " + e);
            }

            UserDto userDto = UserDto.builder()
                    .fbId(responseObject.getString("id"))
                    .name(responseObject.getString("name"))
                    .gender(responseObject.getString("gender"))
                    .picture(imageInByte)
                    .build();

            return userDto;
        }
    }

    @Test
    public void testUserInfo() throws IOException { // todo add unsuccessful test
        assertNotNull(getUserInfo(validToken).getFbId());
        assertNotNull(getUserInfo(validToken).getName());
        assertNotNull(getUserInfo(validToken).getGender());
        assertNotNull(getUserInfo(validToken).getPicture());
    }

    static List<PhotoDto> getUserPhotos(String fbToken) throws IOException { // TODO change to list

        HttpResponse<JsonNode> resposne = null;

        try {
            resposne = Unirest.get(apiUrl + "me/photos")
                    .queryString("fields", "id,name,picture,link")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }

        assert resposne != null;
        JSONArray responseArray = resposne.getBody().getObject().getJSONArray("data");

        ObjectMapper mapper = new ObjectMapper();
        List<PhotoDto> photoDtoList = mapper.readValue(String.valueOf(responseArray), new TypeReference<List<PhotoDto>>() {});

        return photoDtoList;
    }

    @Test
    public void testGetUserPhotos() throws IOException {
        assertNotNull(getUserPhotos(validToken));
    }

}
