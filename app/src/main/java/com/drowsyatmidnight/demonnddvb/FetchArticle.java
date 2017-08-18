package com.drowsyatmidnight.demonnddvb;

import android.content.Context;
import android.util.Xml;

import com.drowsyatmidnight.demonnddvb.model.Article;
import com.drowsyatmidnight.demonnddvb.model.Content;
import com.drowsyatmidnight.demonnddvb.xmlData.WriteXMLData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haint on 16/08/2017.
 */

public class FetchArticle {

    private static boolean isItem;

    public static List<Article> parseXMLAndStoreIt(InputStream inputStream, Context context, int position) throws XmlPullParserException,
            IOException {
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            List<Article> articleList = new ArrayList<>();
            List<String> titleList = new ArrayList<>();
            List<String> descriptionList = new ArrayList<>();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if(isItem){
                    switch (name){
                        case "title":
                            titleList.add(result);
                            break;
                        case "description":
                            descriptionList.add(result);
                            break;
                    }
                }
            }
            WriteXMLData.writeXML(titleList,descriptionList,context, position);
            for (int i = 0; i<titleList.size(); i++){
                String linkIMG, linkArticle;
                Document doc = Jsoup.parse(descriptionList.get(i));
                Element link = doc.select("img").first();
                Element link2 = doc.select("a").first();
                if (link != null && link2 != null){
                    linkIMG = link.attr("src");
                    linkArticle = link2.attr("href");
                    articleList.add(new Article(titleList.get(i), new Content(linkIMG,linkArticle)));
                }
            }
            return articleList;
        }
        finally{
            inputStream.close();
        }
    }
}
