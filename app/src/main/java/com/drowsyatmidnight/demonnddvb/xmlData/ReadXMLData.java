package com.drowsyatmidnight.demonnddvb.xmlData;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.drowsyatmidnight.demonnddvb.model.Article;
import com.drowsyatmidnight.demonnddvb.model.Content;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haint on 18/08/2017.
 */

public class ReadXMLData {

    private static final String homeXML = "homeXML.xml";
    private static final String newsXML = "newsXML.xml";
    private static final String sportXML = "sportXML.xml";
    private static final String technologyXML = "technologyXML.xml";
    private static final String economyXML = "economyXML.xml";
    private static final String carXML = "carXML.xml";

    public static List<Article> readXML(Context context, int position) throws XmlPullParserException,
            IOException {
        try {
            String nameXML = "";
            switch (position){
                case 0:
                    nameXML = homeXML;
                    break;
                case 1:
                    nameXML = newsXML;
                    break;
                case 2:
                    nameXML = sportXML;
                    break;
                case 3:
                    nameXML = technologyXML;
                    break;
                case 4:
                    nameXML = economyXML;
                    break;
                case 5:
                    nameXML = carXML;
                    break;
            }
            InputStream inpst = context.openFileInput(nameXML);
            InputStreamReader inptsr = new InputStreamReader(inpst);
            BufferedReader reader = new BufferedReader(inptsr);
            String str;
            StringBuffer strbuf = new StringBuffer();
            while ((str = reader.readLine()) != null) {
                strbuf.append(str);
            }
            inpst.close();
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(new StringReader(strbuf.toString()));
            xmlPullParser.nextTag();
            List<Article> articleList = new ArrayList<>();
            List<String> titleList = new ArrayList<>();
            List<String> descriptionList = new ArrayList<>();
            boolean isItem = false;

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
            Log.d("xxxTitle", articleList.toString());
            return articleList;
        } catch (Exception FileNotFoundException){
            Log.d("xxxSize", "File Not Found");
        }
        return null;
    }
}
