package jc.vehiclemvp.network.data;

public abstract class BaseResponse {

    transient Response response;

    protected class Response {
        public String status;
    }

    protected abstract Response getResponse();

    public boolean isError() {
        return "ERROR".equalsIgnoreCase(getResponse().status);
    }

    public boolean hasResponse() {
        return null != getResponse();
    }

    public class Error {
        public String code;
    }

}
