package icu.jnet.coinmarketcap.response;

import java.util.HashMap;

public class LoginResponse {

    private String method;
    private HashMap<String, Object> data, status, header;

    public String getMethod() {
        return method;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public HashMap<String, Object> getStatus() {
        return status;
    }

    public HashMap<String, Object> getHeader() {
        return header;
    }
}
