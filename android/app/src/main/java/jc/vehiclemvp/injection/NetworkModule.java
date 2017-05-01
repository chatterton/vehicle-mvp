package jc.vehiclemvp.injection;

import jc.vehiclemvp.framework.base.LocalProperties;
import jc.vehiclemvp.network.VehicleServices;
import jc.vehiclemvp.framework.injection.OkHttpClientFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    VehicleServices provideVehicleServices(OkHttpClientFactory clientHelper, LocalProperties props) {
        String url = props.getProperty(LocalProperties.SERVER_BASE_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .client(clientHelper.client())
                .build();
        return retrofit.create(VehicleServices.class);
    }

}
