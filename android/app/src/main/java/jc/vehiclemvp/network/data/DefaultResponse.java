package jc.vehiclemvp.network.data;

public class DefaultResponse extends BaseResponse {

    BaseResponse.Response response;

    @Override
    protected BaseResponse.Response getResponse() {
        return response;
    }

}
