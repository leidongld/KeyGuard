package com.example.leidong.keyguard.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.db.AcctType;
import com.example.leidong.keyguard.db.TypeHelper;
import com.example.leidong.keyguard.ui.activities.AddTypeDialogActivity;
import com.example.leidong.keyguard.utils.ResUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by leidong on 2017/10/15
 */

public class TypeSpinnerAdapter extends ArrayAdapter implements View.OnClickListener{
    private ArrayList<AcctType> types;

    public TypeSpinnerAdapter(Context context, int resource) {
        super(context, resource);
        types = TypeHelper.getInstance(context).getAllTypes();
    }

    public TypeSpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        types = TypeHelper.getInstance(context).getAllTypes();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == types.size()) {
            AppCompatButton button = new AppCompatButton(getContext());
            button.setText("Add New Account Type");
            button.setOnClickListener(this);
            return button;
        }
        AcctType type = this.types.get(position);
        TypeViewHolder viewHolder;

        //Drop the add button here too.
        if (convertView == null || convertView instanceof AppCompatButton) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_category, parent, false);
            viewHolder = new TypeViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TypeViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new TypeViewHolder();
            }
        }
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
        viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
        viewHolder.textView.setText(type.getName());

        int size = ResUtil.getInstance(null).pointToDp(50);
        Picasso.with(getContext()).load(ResUtil.getInstance(null).getBmpUri(type.getIcon()))
                .config(Bitmap.Config.RGB_565)
                .resize(size, size)
                .onlyScaleDown()
                .into(viewHolder.imageView);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount(){
        return types.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == types.size()) {
            return null;
        }
        return types.get(position);
    }

    public int getPosition(AcctType item) {
        return types.indexOf(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), AddTypeDialogActivity.class);
        getContext().startActivity(intent);

    }

    class TypeViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
