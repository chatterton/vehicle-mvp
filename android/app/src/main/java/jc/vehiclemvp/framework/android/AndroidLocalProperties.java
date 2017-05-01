package jc.vehiclemvp.framework.android;

import jc.vehiclemvp.framework.base.LocalProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AndroidLocalProperties implements LocalProperties {

    private final BaseApplication application;

    private Properties properties;

    @Inject
    public AndroidLocalProperties(BaseApplication app) {
        this.application = app;
    }

    public void init() {
        try {
            InputStream inputStream = application.getResources().getAssets().open("local.properties");
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
