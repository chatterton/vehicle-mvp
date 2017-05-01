package jc.vehiclemvp.network;

import jc.vehiclemvp.network.data.GetVehicles;

import io.reactivex.Observable;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VehicleServices {

    @GET("/api/v1/profile/vehicles.json")
    Observable<GetVehicles> getVehicles();

    @POST("/api/v1/profile/addVehicle")
    Observable<ResponseBody> addVehicle(@Query("model") String modelName);

}
