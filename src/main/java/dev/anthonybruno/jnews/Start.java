package dev.anthonybruno.jnews;

import com.github.redouane59.twitter.signature.TwitterCredentials;
import dev.anthonybruno.jnews.jep.DefaultHtmlJepClient;
import dev.anthonybruno.jnews.jep.HtmlJepService;
import dev.anthonybruno.jnews.twitter.FakeTwitterClient;
import dev.anthonybruno.jnews.twitter.TwitterClient;
import dev.anthonybruno.jnews.twitter.TwitteredTwitterClient;
import dev.anthonybruno.jnews.util.AggregatePropertyAccessor;
import dev.anthonybruno.jnews.util.EnvVarPropertyAccessor;
import dev.anthonybruno.jnews.util.PropertiesPropertyAccessor;
import dev.anthonybruno.jnews.util.PropertyAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Start {
    private static final Logger log = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        var jepService = new HtmlJepService(new DefaultHtmlJepClient(HttpClient.newHttpClient()));
        var propertyAccessor = createPropertyAccessor();
        var twitterClient = createTwitterClient(propertyAccessor);
        var dataDir = propertyAccessor.getOrElse("jnews.data.dir", "data");
        log.info("Data directory set as '{}'", dataDir);
        var jepChanges = new JepChanges(jepService, twitterClient, Path.of(dataDir));
        var scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(jepChanges::process, 0, 24, TimeUnit.HOURS);
    }

    private static TwitterClient createTwitterClient(PropertyAccessor propertyAccessor) {
        var impl = propertyAccessor.getOrElse("jnews.twitter.impl", "fake");
        return switch (impl) {
            case "fake" -> new FakeTwitterClient();
            case "twittered" -> TwitteredTwitterClient.create(TwitterCredentials.builder()
                    .accessToken(propertyAccessor.getNonNull("jnews.twitter.access.token"))
                    .accessTokenSecret(propertyAccessor.getNonNull("jnews.twitter.access.token.secret"))
                    .apiKey(propertyAccessor.getNonNull("jnews.twitter.api.key"))
                    .apiSecretKey(propertyAccessor.getNonNull("jnews.twitter.api.secret.key"))
                    .build());
            default -> throw new IllegalStateException("unknown twitter impl: " + impl);
        };
    }

    private static PropertyAccessor createPropertyAccessor() {
        var accessors = new ArrayList<PropertyAccessor>();
        accessors.add(new EnvVarPropertyAccessor());
        var propertiesStream = JepChanges.class.getResourceAsStream("/app.properties");
        if (propertiesStream != null) {
            accessors.add(PropertiesPropertyAccessor.from(propertiesStream));
        }
        return new AggregatePropertyAccessor(accessors);
    }
}
