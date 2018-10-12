package ro.darkindy.autroll.bot;

import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by andrei.pietrusel on 10/11/2018.
 */
@Component
public class BotLibreMessageProvider implements MessageProvider {

    @Value("${autroll.botlibre.url}")
    private String botUrl;

    @Value("${autroll.botlibre.appid}")
    private String botAppId;

    @Value("${autroll.botlibre.botid}")
    private String botId;

    @Value("${autroll.botlibre.lang}")
    private String botLang;

    @Override
    public String computeMessage(String input) {
        try {
            String body = Unirest.get(botUrl)
                    .queryString("application", botAppId)
                    .queryString("instance", botId)
                    .queryString("language", botLang)
                    .queryString("message", input)
                    .asString().getBody();
            return StringUtils.substringBetween(body, "<message>", "</message>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
