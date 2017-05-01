package jc.vehiclemvp.framework.base;

public interface LocalProperties {

    String SERVER_BASE_URL = "serverUrl";
    String HTTP_TIMEOUT_SECONDS = "httpTimeoutSeconds";

    void init();

    String getProperty(String key);

}
