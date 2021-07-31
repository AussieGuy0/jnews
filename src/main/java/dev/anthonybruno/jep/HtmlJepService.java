package dev.anthonybruno.jep;

import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.util.List;

public class HtmlJepService implements JepService {

    private static final URI JEP_URL = URI.create("https://openjdk.java.net/jeps/");

    private final HtmlJepClient htmlJepClient;

    public HtmlJepService(HtmlJepClient htmlJepClient) {
        this.htmlJepClient = htmlJepClient;
    }

    @Override
    public List<Jep> getJeps() {
        var jepHtml = htmlJepClient.getHtml(JEP_URL);
        var doc = Jsoup.parse(jepHtml);
        return doc.select("table.jeps tr")
                .stream()
                .map(this::tableRowToJep)
                .toList();
    }

    private Jep tableRowToJep(Element tableRow) {
        var cells = tableRow.select("> td");
        var linkCell = cells.get(7);

        var number = Integer.parseInt(cells.get(6).text());
        var name = linkCell.text();
        var url = constructAbsoluteUrl(linkCell.selectFirst("a").attr("href"));
        var status = cleanText(getValueFromTitleAttribute(cells.get(1), "Status: "));
        var title = getValueFromTitleAttribute(cells.get(0), "Type: ");
        var release = requireNonBlankElse(cells.get(2).text(), null);
        var component = requireNonBlankElse(cells.get(3).text() + cells.get(4).text() + cells.get(5).text(), null);
        return new Jep(
                number,
                name,
                url,
                status,
                title,
                release,
                component
        );
    }

    private String cleanText(String str) {
        // Replaces unicode spaces with normal spaces.
        return str.replaceAll("\\p{Z}+", " ");
    }

    private String getValueFromTitleAttribute(Element cell, String prefix) {
        var span = cell.selectFirst("span");
        var title = span.attr("title");
        var statusIndex = title.indexOf(prefix);
        if (statusIndex >= 0) {
            // Title has the form: 'Status: Bla'
            return title.substring(statusIndex + prefix.length());
        }
        return title;
    }

    private String requireNonBlankElse(String value, @Nullable String blankValue) {
        return value.isBlank() ? blankValue : value;
    }

    private URI constructAbsoluteUrl(String relativeLink) {
        return JEP_URL.resolve(relativeLink);
    }
}
