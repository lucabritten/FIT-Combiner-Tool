package com.britten.fittools.integration.strava;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class TokenResponse {

    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("refresh_token")
    private final String refreshToken;
    @JsonProperty("athlete")
    private final Athlete athlete;
    @JsonProperty("expires_at")
    private final long expiresAtEpoch;

    public Instant getExpiresAt(){
        return Instant.ofEpochSecond(expiresAtEpoch);
    }

    @Getter
    @NoArgsConstructor
    public static class Athlete{
        private String firstname;
        private String lastname;

        public String getFullName(){
            return firstname + " " + lastname;
        }
    }

}
