package com.machava.demo.managers.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.machava.demo.entities.Photo;
import com.machava.demo.entities.Reaction;
import com.machava.demo.enums.EReactionType;
import com.machava.demo.managers.FbException;
import com.machava.demo.managers.repository.ReactionRepositoryManager;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FbApiManager {

    public static String apiUrl = "https://graph.facebook.com/v3.3/";

    private ReactionRepositoryManager reactionRepositoryManager;

    public List<Photo> mapPhotoResponseToList(JSONObject responseObject, String fbToken) {
        List<Photo> photoList = new ArrayList<>();
        JSONArray responseDataArray = responseObject.getJSONArray("data");

        ObjectMapper mapper = new ObjectMapper();
        try {
            photoList = mapper.readValue(String.valueOf(responseDataArray), new TypeReference<List<Photo>>() {});
        } catch (IOException e) {
            System.out.println("Error parsing JSON Array to List<PhotoDto>: " + e);
        }

        photoList.forEach(photo -> {
            List<Reaction> reactions = getPhotoReactions(fbToken, photo);
            photo.setReactions(reactions);
        });

        return photoList;
    }

    private List<Reaction> getPhotoReactions(String fbToken, Photo photo) {

        List<Reaction> reactionList = new ArrayList<>();

        List<EReactionType> eReactionTypesList = Arrays.asList(EReactionType.values());
        eReactionTypesList.forEach(reactionType -> {
            try {
                Long reactionSummary = getReactionSummary(fbToken, photo.getId(), reactionType);

                Reaction reactionEntity = reactionRepositoryManager.getReactionIfExists(reactionType, photo);
                reactionEntity.setSummary(reactionSummary);

                reactionList.add(reactionEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return reactionList;
    }

    private static Long getReactionSummary(String fbToken, Long photoId, EReactionType reactionType) throws Exception {

        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(apiUrl + photoId + "/reactions")
                    .queryString("type", reactionType)
                    .queryString("summary", "total_count")
                    .queryString("access_token", fbToken)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        JSONObject responseObject = response.getBody().getObject();

        if (responseObject.has("error")) {
            throw catchFbError(response);
        } else if (response.getStatus() == 200) {
            return responseObject.getJSONObject("summary").getLong("total_count");
        } else {
            throw new Exception("Unexpected error has occurred.");
        }
    }

    public static FbException catchFbError(HttpResponse<JsonNode> response) {
        return new FbException("Response code: " + response.getStatusText() +
                "FB error says: " + response.getBody().getObject().getJSONObject("error").getString("message"));
    }

}
