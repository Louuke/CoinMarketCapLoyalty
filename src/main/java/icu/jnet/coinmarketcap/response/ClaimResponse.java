package icu.jnet.coinmarketcap.response;

import java.util.HashMap;

public class ClaimResponse {

    public HashMap<String, Object> data, status;

    public HashMap<String, Object> getData() {
        return data;
    }

    public HashMap<String, Object> getStatus() {
        return status;
    }
}
