package com.github.aint.uchananalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author Oleksandr Tyshkovets
 */
public class Parser {

    public static void main(String[] args) throws IOException {
        Document page = Jsoup.connect("http://uchan.to/lp").get();
    }

}
