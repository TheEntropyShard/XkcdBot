package me.theentropyshard.xkcdbot.xkcd;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface XkcdHttpApi {
    @GET("{id}/info.0.json")
    Call<Comic> getComic(@Path("id") int id);
}
