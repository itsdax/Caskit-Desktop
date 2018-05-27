package com.caskit.desktop_app.caskit_api;

import com.caskit.desktop_app.caskit_api.data.Content;
import com.caskit.desktop_app.caskit_api.data.Token;
import javafx.util.Pair;
import okhttp3.*;
import org.json.JSONObject;
import com.caskit.desktop_app.utils.Jsonable;

import java.io.File;

import static com.google.common.net.HttpHeaders.*;


public class CaskitApi {

    private static CaskitApi ourInstance = new CaskitApi();

    public static CaskitApi get() {
        return ourInstance;
    }

    private OkHttpClient okHttpClient;

    private CaskitApi() {
        okHttpClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader(USER_AGENT, getUserAgent()).build();
            return chain.proceed(newRequest);
        }).build();
    }

    public Token login(String username, String password) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, createMap(
                new Pair<>("username", username),
                new Pair<>("password", password)
        ));

        Request request = new Request.Builder()
                .url("https://auth.caskit.io/login")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        String response = null;
        try {
            response = parseResponse(okHttpClient.newCall(request).execute());
            return Jsonable.fromJSON(response, Token.class);
        } catch (Exception e) {
            System.out.println(response);
            throw new IllegalStateException(response);
        }

    }

    public Content upload(File file) {
        return upload(file, null);
    }

    public Content upload(File file, String token) {
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("content", file.getName(), RequestBody.create(mediaType, file))
                .addFormDataPart("title", file.getName()).build();

        Request.Builder requestBuilder = new Request.Builder()
                .url("https://serv.caskit.io/upload")
                .post(requestBody)
                .addHeader("content-type", "multipart/form-data");

        if (token != null) {
            requestBuilder.addHeader("token", token);
        }

        String response = null;
        try {
            response = parseResponse(okHttpClient.newCall(requestBuilder.build()).execute());
            System.out.println(response);
            return Jsonable.fromJSON(response, Content.class);
        } catch (Exception e) {
            System.out.println(response);
        }

        return null;
    }

    public String url(Content content) {
        return "https://caskit.io/content/" + content.getId();
    }

    public String directUrl(Content content) {
        return "https://d1.caskit.io/" + content.getFileName();
    }

    @SafeVarargs
    private final String createMap(Pair<String, String>... pairs) {
        JSONObject jsonObject = new JSONObject();
        for (Pair<String, String> pair : pairs) {
            jsonObject.put(pair.getKey(), pair.getValue());
        }
        return jsonObject.toString();
    }

    private String parseResponse(Response response) {
        try (ResponseBody responseBody = response.body()) {
            if (responseBody != null) {
                String body = responseBody.string();

                if (response.code() != 200) {
                    JSONObject jsonObject = new JSONObject(body);
                    return jsonObject.getString("message");
                }

                return body;
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

    private String getUserAgent() {
//        MavenXpp3Reader reader = new MavenXpp3Reader();
//        try {
//            Model model = reader.read(new FileReader("pom.xml"));
//            return "caskit-desktop/" + model.getVersion();
//        } catch (IOException | XmlPullParserException e) {
//            e.printStackTrace();
//        }
        return "caskit-desktop";
    }

}
