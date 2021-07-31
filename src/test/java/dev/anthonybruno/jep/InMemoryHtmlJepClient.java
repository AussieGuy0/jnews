package dev.anthonybruno.jep;

import java.net.URI;

public class InMemoryHtmlJepClient implements HtmlJepClient {

    private final String html;

    public InMemoryHtmlJepClient(String html) {
        this.html = html;
    }

    @Override
    public String getHtml(URI uri) {
        return html;
    }
}
