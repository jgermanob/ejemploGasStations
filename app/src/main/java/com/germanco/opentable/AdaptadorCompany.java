package com.germanco.opentable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by 250616 on 16/3/2017.
 */

public class AdaptadorCompany extends BaseAdapter {
    Context context;

    public AdaptadorCompany(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return gasCompany.ITEMS.length;
    }

    @Override
    public Object getItem(int position) {
        return gasCompany.ITEMS[position];
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_view_custom, viewGroup, false);
        }
        ImageView imagenCompany = (ImageView) view.findViewById(R.id.company_image);
        TextView textCompany = (TextView) view.findViewById(R.id.name_company);

        final gasCompany item= (gasCompany) getItem(i);
        Glide.with(imagenCompany.getContext()).load(item.getIdImagen()).into(imagenCompany);
        textCompany.setText(item.getNombre());



        return view;
    }
}