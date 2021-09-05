package dev.anthonybruno.jnews.twitter;

import org.jetbrains.annotations.Nullable;

public interface TwitterClient {
    record CreateTweet(String body) {
    }

    record TweetId(String id) {
    }

    @Nullable
    TweetId postTweet(CreateTweet tweet);

}
