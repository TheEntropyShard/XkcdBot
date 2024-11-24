package me.theentropyshard.xkcdbot.xkcd;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class XkcdApi {
    private final Retrofit retrofit;
    private final XkcdHttpApi xkcdApi;

    public XkcdApi() {
        this.retrofit = new Retrofit.Builder()
            .baseUrl("https://xkcd.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        this.xkcdApi = this.retrofit.create(XkcdHttpApi.class);
    }

    public Call<Comic> getComic(int id) {
        return this.xkcdApi.getComic(id);
    }
}
