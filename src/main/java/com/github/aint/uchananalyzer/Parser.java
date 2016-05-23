/*
 * Copyright (c) <2016> <Oleksandr Tyshkovets>
 *
 * This file is part of uchan-analyzer.
 *
 *  uchan-analyzer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  uchan-analyzer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with uchan-analyzer.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.aint.uchananalyzer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Oleksandr Tyshkovets
 */
public class Parser {

    private static final int LAST_PAGE = 2700;
    private static final String UCHAN_LP_URL = "http://uchan.to/lp/";
    private static final String UCHAN_LP_PAGE_URL = "http://uchan.to/lp/index.php?board=all&page=";
    private static final String TITLE_ATTRIBUTE = "title";
    private static final String TITLE_ATTRIBUTE_VALUE = "Тред:";
    private static final String POST_ATTRIBUTE = "id";
    private static final String POST_ATTRIBUTE_VALUE = "post_text_";
    private static final String REPLY_CLASS = "reply";
    private static final String FILE_SIZE_CLASS = "filesize";
    private static final String P_TAG = "p";
    private static final Pattern PAT = Pattern.compile("\\d{4}\\.\\d{2}\\.\\d{2}\\sо\\s\\d{2}:\\d{2}");
    public static final String NO_DATE_FOUND = "No date found";
    public static final String COMMENT_POSTER_NAME_CLASS = "commentpostername";
    public static final String FONT_SIZE_ATTRIBUTE = "size";
    public static final String FONT_SIZE_ATTRIBUTE_VALUE = "4";
    public static final String REMOVE_POST_ID_REFERENCE_REGEX = ">>\\d{1,}";

    public static void main(String[] args) throws IOException {
        String lastPage = getLastPage(Jsoup.connect(UCHAN_LP_URL).get());
        Document page = Jsoup.connect(UCHAN_LP_PAGE_URL + lastPage).get();
//        Set<String> topics = new HashSet<>(getThreadTopics(page));
//        topics.forEach(System.out::println);
//        save2Json(topics);

        List<Post> posts = page.getElementsByClass(REPLY_CLASS).stream()
                .map(element -> new Post(
                        getPostAuthor(element),
                        getPostText(element),
                        getPostDate(element),
                        !element.getElementsByClass(FILE_SIZE_CLASS).isEmpty()
                ))
                .collect(Collectors.toList());
        posts.forEach(System.out::println);

    }

    private static String getPostAuthor(Element element) {
        return element.getElementsByClass(COMMENT_POSTER_NAME_CLASS).text();
    }

    private static LocalDateTime getPostDate(Element element) {
        String date = element.getElementsByTag("label").text();
        Matcher matcher = PAT.matcher(date);
        if (!matcher.find()) {
            throw new IllegalStateException(NO_DATE_FOUND);
        }
        return LocalDateTime.parse(matcher.group(), DateTimeFormatter.ofPattern("yyyy.MM.dd о HH:mm"));
    }

    private static String getPostText(Element element) {
        return element.getElementsByAttributeValueContaining(POST_ATTRIBUTE, POST_ATTRIBUTE_VALUE).stream()
                .map(e -> e.getElementsByTag(P_TAG).text())
                .map(s -> s.replaceAll(REMOVE_POST_ID_REFERENCE_REGEX, ""))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining());
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

    private static String getLastPage(Document page) {
        String lastPage = page.getElementsByAttributeValue(FONT_SIZE_ATTRIBUTE, FONT_SIZE_ATTRIBUTE_VALUE).text();
        return lastPage.substring(lastPage.lastIndexOf(" ") + 1);
    }

}
