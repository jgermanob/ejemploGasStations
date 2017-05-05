package com.germanco.opentable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptadorGasStation extends BaseAdapter {
    Activity activity;
    List<gasStation>gasStationList;
    LayoutInflater inflater;
    public AdaptadorGasStation(Activity activity, List<gasStation>gasStationList){
        this.activity=activity;
        this.gasStationList=gasStationList;
    }
    @Override
    public int getCount() {
        try {
            return gasStationList.size();
        }catch (NullPointerException count){
            return 0;
        }

    }

    @Override
    public Object getItem(int position) {
        return gasStationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null){
            inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView==null){
            convertView=inflater.inflate(R.layout.gaslist, null);
        }

        //ASIGNAR LOS VIEWS PARA MOSTRAR
        TextView gasName=(TextView)convertView.findViewById(R.id.gasName);
        ImageView gasImage=(ImageView)convertView.findViewById(R.id.gasImage);
        TextView gasAdress=(TextView)convertView.findViewById(R.id.gasAdress);

        gasStation gs= gasStationList.get(position);
        gasName.setText(gs.getNombre());
        gasAdress.setText(gs.getDireccion());
        Glide.with(activity).load(gs.getImage()).into(gasImage);
        return convertView;
       }
}
