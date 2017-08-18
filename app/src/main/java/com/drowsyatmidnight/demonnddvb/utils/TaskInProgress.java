package com.drowsyatmidnight.demonnddvb.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.drowsyatmidnight.demonnddvb.FetchArticle;
import com.drowsyatmidnight.demonnddvb.model.Article;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by haint on 16/08/2017.
 */

public class TaskInProgress extends AsyncTask<List<String>, Void, List<Article>> {

    private AsyncResponseArticle asyncResponseArticle = null;
    private Context context;
    private SweetAlertDialog sweetAlertDialog;

    public TaskInProgress(AsyncResponseArticle asyncResponseArticle, Context context) {
        this.asyncResponseArticle = asyncResponseArticle;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Đang tải...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCancelText("Hủy");
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.show();
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
        asyncResponseArticle.processFinish(articles);
        sweetAlertDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected List<Article> doInBackground(List<String>... params) {
        try {
            URL url = new URL(params[0].get(0));
            InputStream inputStream = url.openConnection().getInputStream();
            return FetchArticle.parseXMLAndStoreIt(inputStream, context, Integer.parseInt(params[0].get(1)));
        } catch (IOException e) {
            Log.e("IOException", "Error", e);
        } catch (XmlPullParserException e) {
            Log.e("XmlPullParserException", "Error", e);
        }
        return null;
    }
}
