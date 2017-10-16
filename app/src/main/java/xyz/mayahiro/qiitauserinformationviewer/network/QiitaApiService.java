package xyz.mayahiro.qiitauserinformationviewer.network;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xyz.mayahiro.qiitauserinformationviewer.model.User;

/**
 * Created by mayahiro on 10/16/17.
 */

public interface QiitaApiService {
    @GET("users")
    Single<List<User>> users(
            @Query("page") Long page,
            @Query("per_page") Long perPage
    );

    @GET("users/{userId}")
    Single<User> user(
            @Path("userId") String userId
    );
}
