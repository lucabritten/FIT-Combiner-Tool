package com.britten.fittools.modules.strava.controller;

import com.britten.fittools.modules.strava.model.TokenResponse;
import com.britten.fittools.modules.strava.model.UploadResponse;
import com.britten.fittools.modules.strava.client.StravaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/strava")
public class StravaController {

    @Autowired
    private StravaService stravaService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String bearerToken) {
        try {
            var response = stravaService.uploadActivity(file, bearerToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthorizationUri(){
        return ResponseEntity.ok(stravaService.getAuthUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<TokenResponse> callback(@RequestParam String code){
        try{
            TokenResponse tokenResponse = stravaService.exchangeToken(code);
            return ResponseEntity.ok(tokenResponse);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{activityId}")
    public ResponseEntity<String> getCurrentUploadStatus(@PathVariable String activityId, @RequestHeader("Authorization") String bearerToken) throws Exception{
        System.out.println(activityId);
        System.out.println(bearerToken);

        UploadResponse uploadResponse = stravaService.getCurrentUploadStatus(activityId, bearerToken);
        return ResponseEntity.ok(uploadResponse.getStatus());
    }
}
