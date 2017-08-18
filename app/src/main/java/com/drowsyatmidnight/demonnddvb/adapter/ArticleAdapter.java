package com.drowsyatmidnight.demonnddvb.adapter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.drowsyatmidnight.demonnddvb.R;
import com.drowsyatmidnight.demonnddvb.model.Article;
import com.drowsyatmidnight.demonnddvb.model.HolderItemArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haint on 17/08/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Article> articles;
    private final Context context;

    public ArticleAdapter(Context context) {
        this.articles = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderItemArticle(LayoutInflater.from(context).
                inflate(R.layout.item_article, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);
        bindViewArticleHasImage((HolderItemArticle) holder, article, position);
    }

    private void doQuickShare(Article article) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "email@email.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, article.getDescription().getHttp());
        context.startActivity(intent);
    }

    private void bindViewArticleHasImage(HolderItemArticle holder, Article article, int Pos) {
        holder.text_card_name_1.setText(article.getTitle());
        Glide.with(context)
                .load(article.getDescription().getImgURL())
                .into(holder.image_card_cover);
        holder.text_card_name_1.setOnClickListener(v -> goDetail(article));
        holder.image_card_cover.setOnClickListener(v -> goDetail(article));
        holder.image_action_share1.setOnClickListener(v -> doQuickShare(article));
        holder.image_action_like1.setOnClickListener(v -> {
            if (holder.image_action_like1.getDrawable().getConstantState().equals
                    (context.getResources().getDrawable(R.drawable.ic_action_action_favorite).getConstantState())) {
                holder.image_action_like1.setImageResource(R.drawable.ic_action_favorite_enable);
            } else {
                holder.image_action_like1.setImageResource(R.drawable.ic_action_action_favorite);
            }
        });
    }

    private void goDetail(Article article) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_share_chrome);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "email@email.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, article.getDescription().getHttp());
        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setActionButton(bitmap, "Share link", pendingIntent, true);
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(article.getDescription().getHttp()));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<Article> data) {
        this.articles.clear();
        if (data!=null)
            this.articles.addAll(data);
        notifyDataSetChanged();
    }
}
