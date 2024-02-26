package com.moika22.canteen;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ApplicationProperties {

    private PropertiesConfiguration configuration;

    @Value("${spring.config.location}")
    private String filePath;

    @PostConstruct
    private void init() {
        try {
            System.out.println("Loading the properties file: " + filePath);
            configuration = new PropertiesConfiguration(filePath);
            FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
            configuration.setReloadingStrategy(fileChangedReloadingStrategy);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return (String) configuration.getProperty(key);
    }

    public void setProperty(String key, Object value) {
        configuration.setProperty(key, value);
    }

    public void save() {
        try {
            configuration.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

}
