package com.drowsyatmidnight.demonnddvb.utils;

import com.drowsyatmidnight.demonnddvb.model.Article;

import java.util.List;

/**
 * Created by haint on 17/08/2017.
 */

public interface AsyncResponseArticle {
    void processFinish(List<Article> output);
}
