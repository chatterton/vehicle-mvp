package jc.vehiclemvp.framework.injection;

import jc.vehiclemvp.framework.base.LocalProperties;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

public class OkHttpClientFactory {

    private final LocalProperties localProperties;

    @Inject
    public OkHttpClientFactory(LocalProperties p) {
        localProperties = p;
    }

    public OkHttpClient client() {
        int timeout = Integer.parseInt(localProperties.getProperty(LocalProperties.HTTP_TIMEOUT_SECONDS));
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
        return client;
    }
}