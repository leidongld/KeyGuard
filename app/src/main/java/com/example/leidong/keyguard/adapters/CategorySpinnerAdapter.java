package com.example.leidong.keyguard.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.db.Category;
import com.example.leidong.keyguard.db.CategoryHelper;
import com.example.leidong.keyguard.ui.activities.AddCategoryDialogActivity;
import com.example.leidong.keyguard.utils.AppConstants;
import com.example.leidong.keyguard.utils.ResUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by leidong on 2017/10/15
 */

public class CategorySpinnerAdapter extends ArrayAdapter{
    private ArrayList<Category> categories;

    public CategorySpinnerAdapter(Context context, int resource) {
        super(context, resource);
        this.categories = CategoryHelper.getInstance(context).getAllCategory();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == categories.size()) {
            AppCompatButton ret = new AppCompatButton(getContext());
            ret.setText("Add New Category");
            ret.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AddCategoryDialogActivity.class);
                    ((AppCompatActivity) getContext()).startActivityForResult(intent, AppConstants.REQUEST_CODE_ADD_CATE);
                }
            });
            return ret;
        }
        CategoryViewHolder viewHolder;
        if (convertView == null || convertView instanceof AppCompatButton) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_category, parent, false);
            viewHolder = new CategoryViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CategoryViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new CategoryViewHolder();
            }
        }
        viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
        viewHolder.textView.setText(categories.get(position).getName());

        int size = ResUtil.getInstance(null).pointToDp(50);
        Picasso.with(getContext())
                .load(ResUtil.getInstance(null).getBmpUri(categories.get(position).getIcon()))
                .resize(size, size)
                .onlyScaleDown()
                .config(Bitmap.Config.RGB_565)
                .into(viewHolder.imageView);
//        try {
//            viewHolder.imageView.setImageBitmap(ResUtil.getInstance(null).getBmp(categories.get(position).getIcon()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount(){
        return categories.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == categories.size()){
            return null;
        }
        return categories.get(position);
    }

    public int getPosition(Category item) {
        return categories.indexOf(item);
    }
    class CategoryViewHolder {
        public TextView textView;
        public ImageView imageView;
    }
}
