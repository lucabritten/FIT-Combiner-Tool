package com.britten.fittools.modules.strava.util;

import com.britten.fittools.modules.strava.model.TokenResponse;
import com.britten.fittools.modules.strava.client.StravaService;

import java.io.IOException;
import java.time.Instant;

public class TokenUtil {

    private final StravaService stravaService;

    public TokenUtil(StravaService stravaService){
        this.stravaService = stravaService;
    }

    public TokenResponse ensureValidAccessToken(TokenResponse currentToken) throws IOException{
        if(Instant.now().isAfter(currentToken.getExpiresAt())) {
            return stravaService.refreshAccessToken(currentToken.getRefreshToken());
        }
        return currentToken;
    }
}
