package dev.anthonybruno.jnews.twitter;

import com.github.redouane59.twitter.signature.TwitterCredentials;
import org.jetbrains.annotations.Nullable;

public class TwitteredTwitterClient implements TwitterClient {
    private final com.github.redouane59.twitter.TwitterClient twitterClient;

    private TwitteredTwitterClient(com.github.redouane59.twitter.TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
    }

    public static TwitteredTwitterClient create(TwitterCredentials twitterCredentials) {
        return new TwitteredTwitterClient(new com.github.redouane59.twitter.TwitterClient(twitterCredentials));
    }

    @Nullable
    @Override
    public TweetId postTweet(CreateTweet toPost) {
        var tweet = twitterClient.postTweet(toPost.body());
        if (tweet.getId() == null) {
            return null;
        }
        return new TweetId(tweet.getId());
    }
}
