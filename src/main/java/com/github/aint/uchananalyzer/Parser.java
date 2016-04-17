package com.github.aint.uchananalyzer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oleksandr Tyshkovets
 */
public class Parser {

    private static final int LAST_PAGE = 2700;
    private static final String URL = "http://uchan.to/lp/index.php?board=all&page=";
    private static final String TITLE_ATTRIBUTE = "title";
    private static final String TITLE_ATTRIBUTE_VALUE = "Тред:";
    private static final String POST_ATTRIBUTE = "id";
    private static final String POST_ATTRIBUTE_VALUE = "post_text_";

    public static void main(String[] args) throws IOException {
        Document page = Jsoup.connect(URL + LAST_PAGE).get();
        Set<String> topics = new HashSet<>(getThreadTopics(page));
        topics.forEach(System.out::println);
//        save2Json(topics);

        List<String> posts = getPosts(page);
        posts.forEach(System.out::println);

    }

    private static void save2Json(Collection<String> collection) {
        JsonArray array = new JsonArray();
        for (String str : collection) {
            JsonObject innerJson = new JsonObject();
            innerJson.addProperty("topic", str);
            array.add(innerJson);
        }
        JsonObject json = new JsonObject();
        json.add(String.valueOf(LAST_PAGE), array);

        try (Writer writer = new FileWriter(String.valueOf(LAST_PAGE) + ".json")) {
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getThreadTopics(Document page) {
        Elements topics = page.getElementsByAttributeValueContaining(TITLE_ATTRIBUTE, TITLE_ATTRIBUTE_VALUE);
        return topics.stream().map(t -> t.attr("title")).collect(Collectors.toList());
    }

    private static List<String> getPosts(Document page) {
        return page.getElementsByAttributeValueContaining(POST_ATTRIBUTE, POST_ATTRIBUTE_VALUE).stream()
                .map(p -> p.getElementsByTag("p"))
                .collect(Collectors.toList())
                .stream()
                .map(Elements::text)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

}
