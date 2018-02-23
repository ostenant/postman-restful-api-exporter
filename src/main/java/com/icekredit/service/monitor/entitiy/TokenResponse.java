package com.icekredit.service.monitor.entitiy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TokenResponse implements Serializable {

    @JsonProperty("response_code")
    private String responseCode;

    @JsonProperty("token_id")
    private String tokenId;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "responseCode='" + responseCode + '\'' +
                ", tokenId='" + tokenId + '\'' +
                '}';
    }
}
