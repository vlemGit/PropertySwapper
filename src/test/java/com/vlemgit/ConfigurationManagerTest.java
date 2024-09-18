package com.vlemgit;

import com.vlemgit.model.ConfigurationModel;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationManagerTest {

    private ConfigurationModel model;

    @Before
    public void setUp() throws IOException {
        model = new ConfigurationModel("src\\main\\resources\\com\\vlemgit\\settings\\TestSettings.conf");
    }

    @Test
    public void testAddUrl() {
        String url = "http://example.com";
        model.setUrl(url);
        assertTrue(model.getUrlList().contains(url));
    }

    @Test
    public void testValidUrl() {
        String validUrl = "http://example.com";
        String invalidUrl = "example.com";
       /* assertTrue(model.isValidUrl(validUrl));
        assertFalse(model.isValidUrl(invalidUrl));*/
    }
}

