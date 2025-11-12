package com.britten.fittools.service;


import com.britten.fittools.integration.strava.StravaConfig;
import com.britten.fittools.integration.strava.TokenResponse;
import com.britten.fittools.integration.strava.UploadResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

@Service
@RequiredArgsConstructor
public class StravaService {

    private final StravaConfig stravaConfig;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL = "https://www.strava.com/";

    public String getAuthUrl(){
        return BASE_URL + "oauth/authorize"
                + "?client_id=" + stravaConfig.getClientId()
                + "&response_type=code"
                + "&redirect_uri=" + stravaConfig.getRedirectUri()
                + "&approval_prompt=force"
                + "&scope=activity:write,profile:read_all";
    }

    public TokenResponse exchangeToken(String code){
        String url = BASE_URL + "oauth/token";

        String jsonBody = String.format(
        "{\"client_id\": \"%s\", \"client_secret\": \"%s\", \"code\": \"%s\", \"grant_type\": \"authorization_code\"}",
                stravaConfig.getClientId(),
                stravaConfig.getClientSecret(),
                code
        );

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json" );
            post.setEntity(new StringEntity(jsonBody));

            var response = client.execute(post);
            TokenResponse tokenResponse = objectMapper.readValue(response.getEntity().getContent(), TokenResponse.class);
            System.out.println(tokenResponse.getAccessToken() + " A: " + tokenResponse.getAthlete().getFirstname());
            ;
            return tokenResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadActivity(MultipartFile multipartFile,  String bearerToken) throws Exception{
        File tempFile = File.createTempFile("upload", ".fit");
        multipartFile.transferTo(tempFile);

        try(CloseableHttpClient client = HttpClients.createDefault()){
            String url = BASE_URL + "api/v3/uploads";
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", bearerToken);

            post.setEntity(MultipartEntityBuilder.create()
                    .addBinaryBody("file", tempFile, ContentType.DEFAULT_BINARY, tempFile.getName())
                    .addTextBody("data_type", "fit")
                    .addTextBody("name", "Workout")
                    .addTextBody("description", "Uploaded via FITTools by lb®")
                    .build()
            );

            var response = client.execute(post);
//            String response = client.execute(post, new BasicHttpClientResponseHandler()); -> returns String

//            System.out.println(response);
            UploadResponse uploadResponse = objectMapper.readValue(response.getEntity().getContent(), UploadResponse.class);
            System.out.println(uploadResponse);
            var status = response.getCode();

            if(status >= 200 && status < 300)
                return "Upload successful (" + status + ")";
            else
                return "Upload failed (" + status + ")";
        }
        finally {
            tempFile.delete();
        }
    }

    public TokenResponse refreshAccessToken(String refreshToken) throws IOException {
        String url =  BASE_URL + "oath/token";

        String body = String.format("""
                {
                    "client_id: "%s",
                    "client_secret": "%s",
                    "grant_type": "refresh_token",
                    "refresh_token": "%s"
                }
                """,
                stravaConfig.getClientId(),
                stravaConfig.getClientSecret(),
                refreshToken
        );

        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(body));

            var response = client.execute(post);
            if(response.getCode() >= 200 && response.getCode() < 300){
                return objectMapper.readValue(response.getEntity().getContent(), TokenResponse.class);
            }
            throw new IOException("Failed to refresh token: " + response.getCode());
        }
    }

    public UploadResponse getCurrentUploadStatus(String activityId, String bearerToken) throws Exception{
        String url = BASE_URL + "api/v3/uploads/" + activityId;

        try(CloseableHttpClient client = HttpClients.createDefault()){
            HttpGet get = new HttpGet(url);
            get.setHeader("Content-Type", "application/json");
            get.setHeader("Authorization", bearerToken);

            var response = client.execute(get);
            UploadResponse uploadResponse = objectMapper.readValue(response.getEntity().getContent(), UploadResponse.class);
            return uploadResponse;
        }
    }


}
