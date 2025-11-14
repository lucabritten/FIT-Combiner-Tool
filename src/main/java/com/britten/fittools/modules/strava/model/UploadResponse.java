package com.britten.fittools.modules.strava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadResponse {

    @JsonProperty("id_str")
    private String StringId;
    @JsonProperty("error")
    private String error;
    @JsonProperty("status")
    private String status;

    @Override
    public String toString() {
        return "UploadResponse{" +
                "StringId='" + StringId + '\'' +
                ", error='" + error + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
