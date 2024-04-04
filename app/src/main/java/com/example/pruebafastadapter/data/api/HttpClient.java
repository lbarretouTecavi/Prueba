package com.example.pruebafastadapter.data.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pruebafastadapter.data.models.DeviceToken;
import com.example.pruebafastadapter.data.models.Notification;
import com.example.pruebafastadapter.data.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpClient {
    private final OkHttpClient client;

    public HttpClient() {
        client = new OkHttpClient();
    }

    public String getAllTasks(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Código de respuesta inesperado: " + response);
            }
            return response.body().string();
        }
    }

    public String syncAllTask(String url, List<Task> tasks) {
        List<Task> modifiedTasks = new ArrayList<>(tasks);
        for (Task task : modifiedTasks) {
            task.setCreated(task.getCreated().replace(" ", "T"));
            task.setUpdated(task.getUpdated().replace(" ", "T"));
        }
        String jsonTask = null;
        Gson gson = new Gson();
        jsonTask = gson.toJson(modifiedTasks);
        RequestBody body = RequestBody.create(jsonTask, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.e("Error en la solicitud: ", response.code() + " " + response.message());
                return "";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String registerDeviceToken(String url, DeviceToken token) {
        String json = null;
        Gson gson = new Gson();
        json = gson.toJson(token);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.e("DEVICE TOKEN - Error en la solicitud: ", response.code() + " " + response.message());
                return "";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String senNotificationAllDevices(String url, Notification notification) {
        String json = null;
        Gson gson = new Gson();
        json = gson.toJson(notification);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Log.e("BODY", body.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.e("DEVICE TOKEN - Error en la solicitud: ", response.code() + " " + response.message());
                return "";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}