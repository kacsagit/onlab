package com.example.kata.onlab.network;

import android.util.Log;

import com.example.kata.onlab.event.DeleteFriendEvent;
import com.example.kata.onlab.event.ErrorResponseEvent;
import com.example.kata.onlab.event.GetDataDetailsEvent;
import com.example.kata.onlab.event.GetDataEvent;
import com.example.kata.onlab.event.GetFriendsEvent;
import com.example.kata.onlab.event.GetMeEvent;
import com.example.kata.onlab.event.GetUserEvent;
import com.example.kata.onlab.event.GetUsersEvent;
import com.example.kata.onlab.event.LoginDataEvent;
import com.example.kata.onlab.event.PostDataEvent;
import com.example.kata.onlab.event.PostFriendEvent;
import com.example.kata.onlab.event.SignUpDataEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kata on 2017. 02. 11..
 */

public class NetworkManager {

    public static final String ENDPOINT_ADDRESS = "https://still-dawn-67153.herokuapp.com/";

    private static final String TAG = "NetworkManager";
    private static NetworkManager instance;


    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();

        }
        return instance;
    }

    private Retrofit retrofit;
    private NetApi netApi;
    private String token;
    private String usertablename;


    private NetworkManager() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder().baseUrl(ENDPOINT_ADDRESS).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        netApi = retrofit.create(NetApi.class);
        updateData();

    }

    public void setTokenEmail(String token, String email) {
        this.token = token;
        this.usertablename = email;
    }

    public void postFriend(final FriendDetail friend) {
        if (token != null) {
            netApi.postFriend(token, new Friend(friend.id)).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {

                        PostFriendEvent postFriendEvent = new PostFriendEvent();
                        postFriendEvent.setData(friend);
                        EventBus.getDefault().post(postFriendEvent);
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    }

    public void deleteFriend(final FriendDetail friend) {
        if (token != null) {
            netApi.deleteFriend(token, friend.id).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        DeleteFriendEvent deleteFriendEvent = new DeleteFriendEvent();
                        deleteFriendEvent.setData(friend);
                        EventBus.getDefault().post(deleteFriendEvent);
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    }


    public void getfriends() {
        if (token != null) {
            netApi.getFriends(token).enqueue(new Callback<List<Friends>>() {
                @Override
                public void onResponse(Call<List<Friends>> call, Response<List<Friends>> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.body().toString());
                        GetFriendsEvent getFriendsEvent = new GetFriendsEvent();
                        List<Friends> resp = response.body();
                        getFriendsEvent.setData(resp);
                        EventBus.getDefault().post(getFriendsEvent);
                    }
                }

                @Override
                public void onFailure(Call<List<Friends>> call, Throwable t) {

                }
            });
        }
    }

    public void getme() {
        if (token != null) {
            netApi.getMe(token).enqueue(new Callback<Friends>() {
                @Override
                public void onResponse(Call<Friends> call, Response<Friends> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.body().toString());
                        GetMeEvent getmeEvent = new GetMeEvent();
                        Friends resp = response.body();
                        getmeEvent.setData(resp);
                        EventBus.getDefault().post(getmeEvent);
                    }
                }

                @Override
                public void onFailure(Call<Friends> call, Throwable t) {

                }
            });
        }
    }

    public void getuser(int id) {
        if (token != null) {
            netApi.getuser(token, id).enqueue(new Callback<FriendDetail>() {
                @Override
                public void onResponse(Call<FriendDetail> call, Response<FriendDetail> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.body().toString());
                        GetUserEvent getUserEvent = new GetUserEvent();
                        FriendDetail resp = response.body();
                        getUserEvent.setData(response.body());
                        EventBus.getDefault().post(getUserEvent);
                    }

                }

                @Override
                public void onFailure(Call<FriendDetail> call, Throwable t) {

                }
            });
        }
    }

    public void getusers() {
        if (token != null) {
            netApi.getusers(token).enqueue(new Callback<List<Friends>>() {
                @Override
                public void onResponse(Call<List<Friends>> call, Response<List<Friends>> response) {
                    if (response.isSuccessful()) {

                        Log.d(TAG, response.body().toString());
                        GetUsersEvent getUserEvent = new GetUsersEvent();
                        List<Friends> resp = response.body();
                        getUserEvent.setData(resp);
                        EventBus.getDefault().post(getUserEvent);
                    }

                }

                @Override
                public void onFailure(Call<List<Friends>> call, Throwable t) {

                }
            });
        }
    }

    public void updateData() {
        if (token != null) {
            netApi.getDataSpec(token).enqueue(new Callback<List<Data>>() {
                @Override
                public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.body().toString());
                        GetDataEvent getDataEvent = new GetDataEvent();
                        List<Data> resp = response.body();
                        getDataEvent.setData(resp);
                        EventBus.getDefault().post(getDataEvent);
                    } else {
                        ErrorResponseEvent errorResponseEvent = new ErrorResponseEvent();
                        EventBus.getDefault().post(errorResponseEvent);
                        // Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Data>> call, Throwable t) {

                }
            });
        }
    }

    public void updateDataMy() {
        if (token != null) {
            netApi.getDataSpecMy(token).enqueue(new Callback<List<MyData>>() {
                @Override
                public void onResponse(Call<List<MyData>> call, Response<List<MyData>> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.body().toString());
                        GetDataEvent getDataEvent = new GetDataEvent();
                        List<MyData> resp = response.body();
                        getDataEvent.setData(resp);
                        EventBus.getDefault().post(getDataEvent);
                    } else {
                        ErrorResponseEvent errorResponseEvent = new ErrorResponseEvent();
                        EventBus.getDefault().post(errorResponseEvent);
                        // Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<MyData>> call, Throwable t) {

                }
            });
        }
    }


    public void postTokenFB(final String tokenFB) {
        FB fb = new FB();
        fb.access_token = tokenFB;
        netApi.postTokenFB(fb).enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (response.isSuccessful()) {
                    onTokenPostSuccesSocial(response.body());
                } else {
                    Log.d(TAG, response.message());
                    ErrorResponseEvent errorResponseEvent = new ErrorResponseEvent();
                    EventBus.getDefault().post(errorResponseEvent);
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {

            }
        });
    }

    public void onTokenPostSuccesSocial(LoginData data) {
        usertablename = data.email;
        Log.d(TAG, data.email);
        LoginDataEvent loginDataEvent = new LoginDataEvent();
        loginDataEvent.setData(data);
        token = data.token;
        EventBus.getDefault().post(loginDataEvent);
    }

    public void postTokenGoogle(final String tokenG) {
        Google g = new Google();
        g.id_token = tokenG;
        netApi.postTokenGoogle(g).enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (response.isSuccessful()) {
                    onTokenPostSuccesSocial(response.body());
                } else {
                    Log.d(TAG, response.message());
                    ErrorResponseEvent errorResponseEvent = new ErrorResponseEvent();
                    EventBus.getDefault().post(errorResponseEvent);
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {

            }
        });
    }


    public void logIn(final String username, String password) {
        final Login login = new Login(username, password);
        netApi.logIn(login).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    usertablename = username;
                    Log.d(TAG, response.body());
                    token = response.body();
                    LoginDataEvent loginDataEvent = new LoginDataEvent();
                    LoginData loginData = new LoginData();
                    loginData.token = response.body();
                    loginData.email = username;
                    loginDataEvent.setData(loginData);
                    EventBus.getDefault().post(loginDataEvent);
                } else {
                    ErrorResponseEvent errorResponseEvent = new ErrorResponseEvent();
                    EventBus.getDefault().post(errorResponseEvent);
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    public void signupData(final Login d) {
        netApi.signupData(d).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().toString());
                    SignUpDataEvent signupDataEvent = new SignUpDataEvent();
                    signupDataEvent.setData(d);
                    EventBus.getDefault().post(signupDataEvent);

                } else {
                    ErrorResponseEvent errorResponseEvent = new ErrorResponseEvent();
                    EventBus.getDefault().post(errorResponseEvent);
                    //     Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }


    public void postData(final Data d) {
        if (token != null) {
            netApi.postData(token, d).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.body().toString());
                        d.id = response.body();
                        PostDataEvent postDataEvent = new PostDataEvent();
                        postDataEvent.setData(d);
                        EventBus.getDefault().post(postDataEvent);

                    } else {
                        ErrorResponseEvent errorResponseEvent = new ErrorResponseEvent();
                        EventBus.getDefault().post(errorResponseEvent);
                        //     Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    }


    public void postImage(String filePath) {
        if (token != null) {
            File file = new File(filePath);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "avatar");
            netApi.postImage(token, body, name).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    }

    public void signDevice(String devicetoken) {
        if (token != null) {
            Deviceid d = new Deviceid(devicetoken);
            netApi.signDevice(token, d).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

    public void pushNotif(DataDetails item) {
        if (token != null) {
            int id = item.getOwnerid();
            PushId d = new PushId(id);
            netApi.pushNotif(token, d).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

    public void getDataDetails(int dataid) {
        if (token != null) {
            netApi.getDataDetail(token, dataid).enqueue(new Callback<DataDetails>() {
                @Override
                public void onResponse(Call<DataDetails> call, Response<DataDetails> response) {
                    if (response.isSuccessful()) {
                        GetDataDetailsEvent getDataDetailsEvent = new GetDataDetailsEvent();
                        getDataDetailsEvent.setData(response.body());
                        EventBus.getDefault().post(getDataDetailsEvent);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<DataDetails> call, Throwable t) {

                }
            });

        }
    }


    public interface ResponseListener<T> {
        void onResponse(T t);

        void onError(Exception e);
    }

}
