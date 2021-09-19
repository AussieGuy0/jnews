package dev.anthonybruno.jnews.twitter;

public class FakeTwitterClient implements TwitterClient {

    @Override
    public TweetId postTweet(CreateTweet tweet) {
        return new TweetId("FAKE");
    }
}
