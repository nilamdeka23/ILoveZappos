package project.nilam.com.ilovezappos.model;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

public interface ZapposService {

    @GET("/Search")
    Observable<ResultSet> getSearchResult(@Query("term") String searchTerm, @Query("key") String apiKey);

    class Factory {
        public static ZapposService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.zappos.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(ZapposService.class);
        }
    }

}
