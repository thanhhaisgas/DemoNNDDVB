package com.drowsyatmidnight.demonnddvb.xmlData;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by haint on 17/08/2017.
 */

public class WriteXMLData {

    private static final String homeXML = "homeXML.xml";
    private static final String newsXML = "newsXML.xml";
    private static final String sportXML = "sportXML.xml";
    private static final String technologyXML = "technologyXML.xml";
    private static final String economyXML = "economyXML.xml";
    private static final String carXML = "carXML.xml";

    public static void writeXML(List<String> title, List<String> description, Context context, int position) {
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
            FileOutputStream fOut = context.openFileOutput(nameXML, context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "Articles");
            for (int i=0; i<title.size(); i++){
                xmlSerializer.startTag(null, "item");
                xmlSerializer.startTag(null, "title");
                xmlSerializer.text(title.get(i));
                xmlSerializer.endTag(null, "title");
                xmlSerializer.startTag(null, "description");
                xmlSerializer.text(description.get(i));
                xmlSerializer.endTag(null, "description");
                xmlSerializer.endTag(null, "item");
            }
            xmlSerializer.endTag(null, "Articles");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            StringBuffer dataXML = new StringBuffer();
            dataXML.append(dataWrite);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(dataXML.toString());
            osw.flush();
            osw.close();
            Log.d("xxxWrite", "Write thanh cong");
        } catch (IOException io) {
            io.printStackTrace();
            Log.d("xxxWrite", "Write khong thanh cong " + io.toString());
        }
    }
}
