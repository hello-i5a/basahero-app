package com.example.basaheroapp.Utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Database {

    private static final String SUPABASE_URL = "https://zibrpedawwcozzwcepfg.supabase.co";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InppYnJwZWRhd3djb3p6d2NlcGZnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM4ODA4NzksImV4cCI6MjA0OTQ1Njg3OX0.lPCYMz6--EDOW_UmsfBfBu7IDcs3YKcgJrIQJ3kmMNs";

    public static void checkAccount(String username, String email, CheckCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Construct the query URL
        String url = SUPABASE_URL + "/rest/v1/users?or=(username.eq." + username + ",email.eq." + email + ")";

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Pass the exception to the callback
                callback.onFailure(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
                    int count = jsonArray.size();  // Count the number of matching users
                    callback.onResult(count);      // Pass the result to the callback
                } else {
                    // In case of failure, return -1
                    callback.onResult(-1);
                }
            }
        });
    }

    public static void checkAccountUsername(String username, CheckCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Construct the query URL
        String url = SUPABASE_URL + "/rest/v1/users?or=(username.eq." + username + ")";

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Pass the exception to the callback
                callback.onFailure(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
                    int count = jsonArray.size();  // Count the number of matching users
                    callback.onResult(count);      // Pass the result to the callback
                } else {
                    // In case of failure, return -1
                    callback.onResult(-1);
                }
            }
        });
    }

    public static void checkAccountEmail(String email, CheckCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Construct the query URL
        String url = SUPABASE_URL + "/rest/v1/users?or=(email.eq." + email + ")";

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Pass the exception to the callback
                callback.onFailure(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
                    int count = jsonArray.size();  // Count the number of matching users
                    callback.onResult(count);      // Pass the result to the callback
                } else {
                    // In case of failure, return -1
                    callback.onResult(-1);
                }
            }
        });
    }

    //-1 = request failure | -2 no account
    public static void getInfo(String accKey, String desiredInfo, GetCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Construct the query URL
        String url = SUPABASE_URL + "/rest/v1/users?or=(username.eq." + accKey + ",email.eq." + accKey + ")";

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Pass the exception to the callback
                callback.onFailure(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();

                    // Process the response and extract desired information
                    if (jsonArray.size() > 0) {
                        JsonObject userObject = jsonArray.get(0).getAsJsonObject(); // Assuming we're interested in the first match
                        String info = userObject.get(desiredInfo).getAsString();  // Extract the desired info
                        callback.onResult(info);  // Pass the extracted information back to the callback
                    } else {
                        // If no users match, return -2
                        callback.onResult(String.valueOf(-2));
                    }
                } else {
                    // In case of failure, return -1
                    callback.onResult(String.valueOf(-1));
                }
            }
        });
    }

    // 0 = true | 1 = false | 2 = No account found
    public static void login(String username, String password, CheckCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Construct the query URL
        String url = SUPABASE_URL + "/rest/v1/users?select=password&or=(username.eq." + username + ",email.eq." + username + ")";

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Pass the exception to the callback
                callback.onFailure(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
                    if (jsonArray.size() == 0) {
                        callback.onResult(2);
                    } else {
                        JsonObject userObject = jsonArray.get(0).getAsJsonObject();
                        String acc_pass = userObject.get("password").getAsString();

                        if (password.equals(acc_pass)) {
                            callback.onResult(0);
                        } else {
                            callback.onResult(1);
                        }
                    }
                } else {
                    callback.onResult(-1);
                }
            }
        });
    }

    public static void register(String name, String username, String email, String password) {
        // Create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create JSON payload for insertion
        String json = "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"username\": \"" + username + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\"\n" +
                "}";

        // Create the request body
        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json; charset=utf-8")
        );

        // Create the request
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/users")
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Data inserted successfully: " + response.body().string());
            } else {
                System.err.println("Failed to insert data: " + response.code() + " - " + response.message());
                System.err.println("Response: " + response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addBook(String title, String author, String genre, String pub_date, String img_name, String description) {
        // Create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create JSON payload for insertion
        String json = "{\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"author\": \"" + author + "\",\n" +
                "  \"genre\": \"" + genre + "\",\n" +
                "  \"pub_date\": \"" + pub_date + "\",\n" +
                "  \"img_name\": \"" + img_name + "\",\n" +
                "  \"description\": \"" + description + "\"\n" +
                "}";

        // Create the request body
        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json; charset=utf-8")
        );

        // Create the request
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/books")
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Data inserted successfully: " + response.body().string());
            } else {
                System.err.println("Failed to insert data: " + response.code() + " - " + response.message());
                System.err.println("Response: " + response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface CheckCallback {
        void onResult(int response);
        void onFailure(IOException e);
    }

    public interface GetCallback {
        void onResult (String info);
        void onFailure(IOException e);
    }
}
