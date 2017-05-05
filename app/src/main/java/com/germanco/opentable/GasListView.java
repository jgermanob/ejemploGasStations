package com.germanco.opentable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GasListView extends AppCompatActivity {
    int claveRecibida;
    List<gasStation> gasList;
    ListView listView;
    AdaptadorGasStation adaptador;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_list_view);
        listView=(ListView)findViewById(R.id.gasList_view);
        gasList=MainActivity.gasStationList;
        adaptador= new AdaptadorGasStation(this,gasList);
        listView.setAdapter(adaptador);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                gasStation gs=gasList.get(position);
                Intent intent=new Intent(GasListView.this, GasMapView.class);
                intent.putExtra("latitud",gs.getLatitud());
                intent.putExtra("longitud",gs.getLongitud());
                intent.putExtra("nombre",gs.getNombre());
                startActivity(intent);
            }
        });
    }


}
