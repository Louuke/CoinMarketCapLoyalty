package icu.jnet.coinmarketcap.response;

import java.util.HashMap;
import java.util.List;

public class CheckInLogsResponse {

    private List<HashMap<String, Object>> data;
    private HashMap<String, Object> status;

    public HashMap<String, Object> getStatus() {
        return status;
    }

    public List<HashMap<String, Object>> getData() {
        return data;
    }
}
