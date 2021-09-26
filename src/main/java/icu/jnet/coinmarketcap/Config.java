package icu.jnet.coinmarketcap;

import icu.jnet.coinmarketcap.helper.Utils;

import java.util.HashMap;

public class Config {

    private HashMap<String, String> credentials;
    private String antiCaptchaApikey, authorization;

    public String getEmail() {
        return credentials.get("email");
    }

    public String getPassword() {
        return credentials.get("password");
    }

    public String getAntiCaptchaApikey() {
        return antiCaptchaApikey;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
        Utils.saveConfig();
    }

    public String getAuthorization() {
        return authorization;
    }
}
