package com.drowsyatmidnight.demonnddvb.drawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drowsyatmidnight.demonnddvb.Category;
import com.drowsyatmidnight.demonnddvb.R;
import com.drowsyatmidnight.demonnddvb.adapter.ArticleAdapter;
import com.drowsyatmidnight.demonnddvb.utils.TaskInProgress;
import com.drowsyatmidnight.demonnddvb.xmlData.ReadXMLData;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haint on 17/08/2017.
 */

public class FmArticle extends Fragment{

    @BindView(R.id.lvArticle)
    RecyclerView lvArticle;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private static final String LINK = "LINK_RSS";
    private static final String POSITION = "POSITION";
    private List<String> text;
    private ArticleAdapter adapter;
    private View v;

    public static FmArticle newInstance(String link, int position) {
        FmArticle fragment = new FmArticle();
        Bundle args = new Bundle();
        args.putString(LINK, link);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        v = view;
        ButterKnife.bind(this, view);
        text = new ArrayList<>();
        text.add(getArguments().getString(LINK));
        text.add(String.valueOf(getArguments().getInt(POSITION)));
        loadArticles();
        swipeRefresh.setOnRefreshListener(() -> loadArticles());
    }

    private void loadArticles() {
        new TaskInProgress(output -> {
            adapter = new ArticleAdapter(v.getContext());
            if (Category.isNetworkAccess){
                adapter.setData(output);
            }else {
                try {
                    if (getArguments().getInt(POSITION)!=7)
                        if (ReadXMLData.readXML(v.getContext(), getArguments().getInt(POSITION))!=null) {
                            adapter.setData(ReadXMLData.readXML(v.getContext(), getArguments().getInt(POSITION)));
                        }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            lvArticle.setLayoutManager(layoutManager);
            lvArticle.setAdapter(adapter);
        }, v.getContext(), swipeRefresh.isRefreshing()).execute(text);
        if (adapter != null && swipeRefresh.isRefreshing()){
            swipeRefresh.setRefreshing(false);
        }
    }
}
