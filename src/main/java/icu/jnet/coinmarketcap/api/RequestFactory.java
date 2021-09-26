package icu.jnet.coinmarketcap.api;

import com.anticaptcha.api.captcha.RecaptchaV2Proxyless;
import com.anticaptcha.api.response.TaskResult;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import icu.jnet.coinmarketcap.Config;
import icu.jnet.coinmarketcap.helper.Utils;
import icu.jnet.coinmarketcap.response.LoginResponse;
import icu.jnet.coinmarketcap.response.PointSummaryResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RequestFactory {

    private final Config config;
    final RecaptchaV2Proxyless recaptchaV2;
    final Gson gson = new Gson();

    public RequestFactory() {
        config = Utils.loadConfig();

        recaptchaV2 = new RecaptchaV2Proxyless(config.getAntiCaptchaApikey());
        recaptchaV2.setWebsiteUrl("https://coinmarketcap.com/");
        recaptchaV2.setWebsiteKey("6LfSEDIbAAAAAEyHtxj2xtEA8It6gSi6s4_PNxwI");
        recaptchaV2.setInvisible(true);
    }

    public boolean login() {
        if(config.getAuthorization() == null || config.getAuthorization().isEmpty()) {
            // Get authorization
            TaskResult taskResult = recaptchaV2.createTask();
            if(taskResult.getErrorId() == 0) {
                String url = "https://api.coinmarketcap.com/auth/v4/user/login";
                String token = taskResult.getSolution().getGRecaptchaResponse();
                String body = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"platform\":\"web\",\"recaptcha\":\"%s\"}",
                        config.getEmail(), config.getPassword(), token);
                LoginResponse response = gson.fromJson(query(url, body), LoginResponse.class);
                if(((String) response.getStatus().get("error_message")).equalsIgnoreCase("Success")) {
                    config.setAuthorization((String) response.getData().get("token"));
                }
            }
        } else {
            // Verify valid authorization
            PointSummaryResponse pointSummary = userPointSummary();
            if(!(((String) pointSummary.getStatus().get("error_message")).equalsIgnoreCase("Success"))) {
                config.setAuthorization(null);
            }
        }
        return config.getAuthorization() != null;
    }

    public PointSummaryResponse userPointSummary() {
        String url = "https://api.coinmarketcap.com/asset/v3/loyalty/user-point-summary";
        return gson.fromJson(query(url, "{}"), PointSummaryResponse.class);
    }

    String query(String url, String body) {
        HttpRequestFactory factory = new NetHttpTransport().createRequestFactory();
        try {
            HttpRequest request = factory.buildPostRequest(new GenericUrl(url), ByteArrayContent.fromString("application/json", body));
            request.setHeaders(new HttpHeaders().set("authorization", config.getAuthorization()));
            return read(request.execute().getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String read(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024*64];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int numRead;
        while ((numRead = inputStream.read(buffer)) != -1) {
            baos.write(buffer,  0,  numRead);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }
}
