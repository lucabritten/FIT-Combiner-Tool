package com.britten.fittools.integration.strava;

import com.britten.fittools.service.StravaService;

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
