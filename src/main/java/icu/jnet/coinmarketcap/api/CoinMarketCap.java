package icu.jnet.coinmarketcap.api;

import com.anticaptcha.api.response.TaskResult;
import icu.jnet.coinmarketcap.response.CheckInLogsResponse;
import icu.jnet.coinmarketcap.response.ClaimResponse;

import java.time.Instant;

public class CoinMarketCap extends RequestFactory {

    public boolean isClaimReady() {
        String url = "https://api.coinmarketcap.com/asset/v3/loyalty/latest-check-in-logs";
        CheckInLogsResponse logsResponse = gson.fromJson(query(url, "{}"), CheckInLogsResponse.class);
        if(((String) logsResponse.getStatus().get("error_message")).equalsIgnoreCase("Success")) {
           return ((double) logsResponse.getData().get(logsResponse.getData().size() - 1).get("state")) == 0;
        }
        return false;
    }

    public boolean claim() {
        TaskResult taskResult = recaptchaV2.createTask();
        if(taskResult.getErrorId() == 0) {
            String url = "https://api.coinmarketcap.com/asset/v3/loyalty/check-in/";
            String token = taskResult.getSolution().getGRecaptchaResponse();
            String body = String.format("{\"platform\":\"web\",\"recaptcha\":\"%s\"}", token);
            ClaimResponse claimResponse = gson.fromJson(query(url, body), ClaimResponse.class);
            return ((String) claimResponse.getStatus().get("error_message")).equalsIgnoreCase("Success");
        }
        return false;
    }

    public long nextClaim() {
        String url = "https://api.coinmarketcap.com/asset/v3/loyalty/latest-check-in-logs";
        CheckInLogsResponse logsResponse = gson.fromJson(query(url, "{}"), CheckInLogsResponse.class);
        return !isClaimReady() && ((String) logsResponse.getStatus().get("error_message")).equalsIgnoreCase("Success")
                ? (long) (((double) logsResponse.getData().get(logsResponse.getData().size() - 1).get("time")) / 1000 + 86400)
                : Instant.now().getEpochSecond() + 3600;
    }
}
