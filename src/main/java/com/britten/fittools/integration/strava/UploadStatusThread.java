package com.britten.fittools.integration.strava;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class UploadStatusThread { //implements Runnable{

//    private MultipartFile multipartFile;
//    private String token;
//    private UploadResponse uploadResponse = null;
//    private ObjectMapper objectMapper;
//
//    public UploadThread(MultipartFile multipartFile, String token){
//        this.multipartFile = multipartFile;
//        this.token = token;
//        objectMapper = new ObjectMapper();
//    }
//
//    @Override
//    public void run() throws Exception{
//        while(true) {
//            File tempFile = File.createTempFile("upload", ".fit" );
//            multipartFile.transferTo(tempFile);
//
//            try (CloseableHttpClient client = HttpClients.createDefault()) {
//                HttpPost post = new HttpPost("https://www.strava.com/api/v3/uploads" );
//                post.setHeader("Authorization", token);
//
//                post.setEntity(MultipartEntityBuilder.create()
//                        .addBinaryBody("file", tempFile, ContentType.DEFAULT_BINARY, tempFile.getName())
//                        .addTextBody("data_type", "fit" )
//                        .addTextBody("name", "Workout" )
//                        .addTextBody("description", "Uploaded via FITTools by lb®" )
//                        .build()
//                );
//
//                var response = client.execute(post);
////            String response = client.execute(post, new BasicHttpClientResponseHandler()); -> returns String
//
////            System.out.println(response);
//            uploadResponse = objectMapper.readValue(response.getEntity().getContent(), UploadResponse.class);
//            if(uploadResponse.getStatus().contains("is still being processed.")){
//                System.out.println("...processing");
//            }
//            else{
//                System.out.println(uploadResponse.getStatus());
//            }
////            System.out.println(uploadResponse);
//
//            } finally {
//                tempFile.delete();
//            }
//        }
//    }
}
