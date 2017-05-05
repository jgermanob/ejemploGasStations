package com.germanco.opentable;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    FirebaseAuth firebaseAuth;
    GoogleApiClient googleApiClient;
    GridView gridView;
    AdaptadorCompany adaptadorCompany;
    int clave;
    public static List<gasStation> gasStationList= new ArrayList<>();


    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> optionalPendingResult= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        gridView=(GridView)findViewById(R.id.gridview);
      /*  gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        }); */

        adaptadorCompany= new AdaptadorCompany(this);
        gridView.setAdapter(adaptadorCompany);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                if(position==0){readJSON("OxxoGas");}
                if(position==1){readJSON("Esso");}
                if(position==2){readJSON("Mobil");}
                if(position==3){readJSON("Pemex");}

               // System.out.println("CLAVE ENVIADA: "+clave);

                Intent intent= new Intent(MainActivity.this, GasListView.class);
                //intent.putExtra("clave",clave);
                startActivity(intent);
            }
        });


        //CODIGO PARA USAR GOOGLE SIGN IN
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        googleApiClient= new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_descubre) {
            // Handle the camera action
        } else if (id == R.id.nav_busca) {

        } else if (id == R.id.nav_ayuda) {

        } else if (id == R.id.nav_elimina) {

        }else if (id == R.id.nav_cierra) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if(status.isSuccess()){
                        Intent intent=new Intent(MainActivity.this, InicioSesion.class);
                        startActivity(intent);
                    }else{
                        return;
                    }
                }
            });
            firebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent intent= new Intent(MainActivity.this,InicioSesion.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public String loadJSON(){
        String json;
        try{
            InputStream is= getAssets().open("gastations.json");
            int size=is.available();
            byte[] buffer= new byte[size];
            is.read(buffer);
            is.close();
            json=new String(buffer, "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void readJSON(String gasNameCompany){
        try{
            gasStationList.clear();
            JSONObject obj= new JSONObject(loadJSON());
            JSONArray jsonArray= obj.getJSONArray(gasNameCompany);
            for(int i=0; i<jsonArray.length(); i++){
                System.out.println("INICIA LECTURA");
                JSONObject gasolinera=jsonArray.getJSONObject(i);
                String nombreGas=gasolinera.getString("name");
                System.out.println("NOMBRE GAS: "+nombreGas);
                String latitudGas=gasolinera.getString("latitud");
                String longitudGas=gasolinera.getString("longitud");
                String direcGas=gasolinera.getString("direccion");
                String urlImagen="http://maps.google.com/maps/api/staticmap?center="+latitudGas+","+longitudGas+"&zoom=17&size=510x250";
                System.out.println("URL IMAGEN: "+urlImagen);
                gasStation gs= new gasStation();
                gs.setNombre(nombreGas);
                gs.setLatitud(latitudGas);
                gs.setLongitud(longitudGas);
                gs.setDireccion(direcGas);
                gs.setImage(urlImagen);

                gasStationList.add(gs);
                Log.d("Estación agregada",gs.nombre);



            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


}
