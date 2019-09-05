package com.machava.demo;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.machava.demo.controllers.FbApi;
import com.machava.demo.managers.FbException;

@RunWith(SpringRunner.class)
//@DataJpaTest // DB tests - runs H2, logs sql cmds,...
public class TestFbApi {

    @Autowired
    FbApi fbApi;

    String invalidToken = "EAATHPQNJw4sBAEmwEds3NNiOA8vkBLH3dx0ldeS9nUeu879b3Gw9xsSHUIBXuZCNstmVGPQZA6ORoZCWjIZCRbJ06XuZC7vCAZBWdHPxDTsOxLn5afaoS1c1yfDYU4QOOruDax3ZBrQeNDZBJ84ITvD6SHE5ZCybSuY4aOyaZCs0uP81isK3Iwe8PJLd1yZC3CbDZAoZD";
    String validToken = "EAATHPQNJw4sBANZA7VAGywuc6WzAYYhyl9Y7DBtfbwZBX8SBWHlMy0ZCrm814zeS1PZC8E2fmSMsD1Ke6uvrdG6nfDNCMq6LaMtJd90Uuihv0ZAf0j7gb4zqyImZBB0R6PgC53Ewao9sgmFrbox153cfcuNx2u4ZCy0BwUGvtgoy7o4vAtqh8Jh0UHT8L0SGrcZD";
    Long validUser = 10212277543693880L;
    Long invalidUser = 10212222445566778L;

    @Test
    public void testToken() throws FbException {
        assertNotNull(fbApi.verifyToken(invalidToken));
        //assertNull(fbApi.verifyToken(validToken));
    }

    @Test
    public void testUserDetails() throws Exception { // todo add unsuccessful test
        assertNotNull(fbApi.getUserDetails(validToken).getId());
        assertNotNull(fbApi.getUserDetails(validToken).getName());
        assertNotNull(fbApi.getUserDetails(validToken).getGender());
        assertNotNull(fbApi.getUserDetails(validToken).getPicture());
    }

    @Test
    public void testGetUserPhotos() throws Exception {
        assertNotNull(fbApi.getUserPhotos(validToken));
    }

}
