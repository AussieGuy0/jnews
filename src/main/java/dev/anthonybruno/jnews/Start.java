package dev.anthonybruno.jnews;

import com.github.redouane59.twitter.signature.TwitterCredentials;
import dev.anthonybruno.jnews.jep.DefaultHtmlJepClient;
import dev.anthonybruno.jnews.jep.HtmlJepService;
import dev.anthonybruno.jnews.twitter.TwitterClient;
import dev.anthonybruno.jnews.twitter.TwitteredTwitterClient;
import dev.anthonybruno.jnews.util.AggregatePropertyAccessor;
import dev.anthonybruno.jnews.util.FilePropertyAccessor;
import dev.anthonybruno.jnews.util.PropertyAccessor;
import dev.anthonybruno.jnews.util.SystemPropertyAccessor;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.util.ArrayList;

public class Start {
    public static void main(String[] args) {
        var jepService = new HtmlJepService(new DefaultHtmlJepClient(HttpClient.newHttpClient()));
        var propertyAccessor = propertyAccessor();
        var twitterImpl = propertyAccessor.getOrElse("jnews.twitter.impl", "fake");
        TwitterClient twitterClient;
        if (twitterImpl.equals("fake")) {
            twitterClient = tweet -> null;
        } else {
            twitterClient = TwitteredTwitterClient.create(TwitterCredentials.builder()
                    .accessToken(propertyAccessor.getNonNull("jnews.twitter.access.token"))
                    .accessTokenSecret(propertyAccessor.getNonNull("jnews.twitter.access.token.secret"))
                    .apiKey(propertyAccessor.getNonNull("jnews.twitter.api.key"))
                    .apiSecretKey(propertyAccessor.getNonNull("jnews.twitter.api.secret.key"))
                    .build());

        }
        var dataDir = propertyAccessor.getOrElse("jnews.data.dir", "data");
        var jepChanges = new JepChanges(jepService, twitterClient, Path.of(dataDir));
        jepChanges.process();
    }

    private static PropertyAccessor propertyAccessor() {
        var accessors = new ArrayList<PropertyAccessor>();
        var propertiesStream = JepChanges.class.getResourceAsStream("/app.properties");
        if (propertiesStream != null) {
            accessors.add(FilePropertyAccessor.from(propertiesStream));
        }
        accessors.add(new SystemPropertyAccessor());
        return new AggregatePropertyAccessor(accessors);
    }
}
