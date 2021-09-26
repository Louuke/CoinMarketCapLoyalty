package icu.jnet.coinmarketcap;

import icu.jnet.coinmarketcap.api.CoinMarketCap;
import icu.jnet.coinmarketcap.helper.Utils;

import java.time.Instant;

public class Main {

    private static final CoinMarketCap coinMarketCap = new CoinMarketCap();

    public static void main(String[] args) {
        long nextClaim = 0;

        while(true) {
            long now = Instant.now().getEpochSecond();
            if(nextClaim < now) {
                nextClaim = now + 3600;
                if(coinMarketCap.login()) {
                    if(coinMarketCap.isClaimReady()) {
                        Utils.print("Diamond collect is ready!");
                        if(coinMarketCap.claim()) {
                            Utils.print("Claim was successful!");
                        }
                    }
                    nextClaim = coinMarketCap.nextClaim();
                }
                Utils.print("Next claim in " + (int) ((nextClaim - now) / 60.0) + " minutes");
                Utils.print("Total diamonds: " + getDiamonds());
            }
            Utils.waitMill(3000);
        }
    }

    private static int getDiamonds() {
        return (int)((double) coinMarketCap.userPointSummary().getData().get("point"));
    }
}
