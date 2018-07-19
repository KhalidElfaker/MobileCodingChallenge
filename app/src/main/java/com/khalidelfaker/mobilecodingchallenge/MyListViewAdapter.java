package com.khalidelfaker.mobilecodingchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.khalidelfaker.models.GithubRepos;
import com.khalidelfaker.models.Item;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Item> items;

    public MyListViewAdapter(Context context, GithubRepos repos) {
        this.context = context;
        items = repos.getItems();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell_list_View;
        TextView repo_name;
        TextView repo_desc;
        TextView owner_name;
        TextView rating;
        ImageView avatar;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cell_list_View = inflater.inflate(R.layout.repos_element_ligne, null);
        } else {
            cell_list_View = convertView;
        }

        repo_name = cell_list_View.findViewById(R.id.repo_name);
        repo_desc = cell_list_View.findViewById(R.id.repos_desc);
        owner_name = cell_list_View.findViewById(R.id.owner_name);
        rating = cell_list_View.findViewById(R.id.rating);
        avatar = cell_list_View.findViewById(R.id.avatar);

        repo_name.setText(items.get(position).getName());
        repo_desc.setText(items.get(position).getDescription());
        owner_name.setText(items.get(position).getOwner().getLogin());

        //formatting the number of stars to be shown as expected
        Long StargazersCount = items.get(position).getStargazersCount();
        double ratingToShow;
        String ratingS;
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat;
        if (StargazersCount > 999) {
            ratingToShow = StargazersCount / 1000.0;
            decimalFormat = new DecimalFormat("0.0K", decimalFormatSymbols);
        } else if (StargazersCount > 999999) {
            ratingToShow = StargazersCount / 1000000.0;
            decimalFormat = new DecimalFormat("0.0M", decimalFormatSymbols);
        } else {
            ratingToShow = StargazersCount;
            decimalFormat = new DecimalFormat("0", decimalFormatSymbols);
        }
        ratingS = decimalFormat.format(ratingToShow);
        rating.setText(ratingS);

        //using Picasso to download profile pictures and manage the caching process
        String avatarUrl = items.get(position).getOwner().getAvatarUrl();
        Picasso.get().load(avatarUrl).into(avatar);

        notifyDataSetChanged();
        return cell_list_View;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }
}
