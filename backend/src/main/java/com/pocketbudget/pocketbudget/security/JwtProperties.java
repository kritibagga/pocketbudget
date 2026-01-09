package com.pocketbudget.pocketbudget.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="security.jwt")
public class JwtProperties {

    private String issuer="pocketbudget";
    private String secret;
    private long accessTokenMinutes;
    private long refreshDays;
    
    public String getIssuer() {
        return issuer;
    }
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public long getAccessTokenMinutes() {
        return accessTokenMinutes;
    }
    public void setAccessTokenMinutes(int accessMinutes) {
        this.accessTokenMinutes = accessMinutes;
    }
    public long getRefreshDays() {
        return refreshDays;
    }
    public void setRefreshDays(int refreshDays) {
        this.refreshDays = refreshDays;
    }



}
