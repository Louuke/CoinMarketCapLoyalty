package icu.jnet.coinmarketcap.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import icu.jnet.coinmarketcap.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class Utils {

    private static String lastOutput = "";
    private static Config config;

    public static Config loadConfig() {
        if(config == null) {
            try {
                List<String> conf = Files.readAllLines(Paths.get("config.json"));
                StringBuilder builder = new StringBuilder();
                for(String s : conf) {
                    builder.append(s);
                }
                config = new Gson().fromJson(builder.toString(), Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    public static void saveConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(Paths.get("config.json"), gson.toJson(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print(String output) {
        if(!output.equals(lastOutput)) {
            lastOutput = output;

            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)
                    .withLocale(Locale.getDefault())
                    .withZone(ZoneId.systemDefault());

            System.out.println(formatter.format(Instant.now()) + ": "+ output);
        }
    }

    public static void waitMill(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
