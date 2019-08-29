package com.machava.demo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.machava.demo.controllers.FbApi;
import com.machava.demo.managers.FbException;

@RunWith(SpringRunner.class)
//@DataJpaTest // DB tests - runs H2, logs sql cmds,...
public class TestFbApi {

    String invalidToken = "EAATHPQNJw4sBAEmwEds3NNiOA8vkBLH3dx0ldeS9nUeu879b3Gw9xsSHUIBXuZCNstmVGPQZA6ORoZCWjIZCRbJ06XuZC7vCAZBWdHPxDTsOxLn5afaoS1c1yfDYU4QOOruDax3ZBrQeNDZBJ84ITvD6SHE5ZCybSuY4aOyaZCs0uP81isK3Iwe8PJLd1yZC3CbDZAoZD";
    String validToken = "EAATHPQNJw4sBANpyCHXhZB5YmuBZARz0u2GZC0SX7y8faXShPkrdZA1XZB1afLZChBCdZCTQZC917ZAPNt65tytaaG15RrlYuQ9eXb1dYNcZB5kQ5ZCZBzyYwrGDYPg4GaE2TAY2ZCVeUVQAr4pyFFyYPLerCUnrpEGrM7Gop72QJIaXkdL2aXrk3GF6T1LIFaQjBZAh3pJGtjHkd0iAZDZD";
    Long validUser = 10212277543693880L;
    Long invalidUser = 10212222445566778L;

    @Test
    public void testToken() throws FbException {
        assertFalse(FbApi.verifyToken(invalidToken));
        assertTrue(FbApi.verifyToken(validToken));
    }

    @Test
    public void testUserDetails() throws Exception { // todo add unsuccessful test
        assertNotNull(FbApi.getUserDetails(validToken).getId());
        assertNotNull(FbApi.getUserDetails(validToken).getName());
        assertNotNull(FbApi.getUserDetails(validToken).getGender());
        assertNotNull(FbApi.getUserDetails(validToken).getPicture());
    }

    @Test
    public void testGetUserPhotos() throws Exception {
        assertNotNull(FbApi.getUserPhotos(validToken));
    }

}
