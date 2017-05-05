package com.germanco.opentable;

/**
 * Created by 250616 on 16/3/2017.
 */

public class gasCompany {
    String nombre;
    int idImagen;

    public gasCompany(String nombre, int idImagen){
        this.nombre=nombre;
        this.idImagen=idImagen;
    }

    public String getNombre(){
        return nombre;
    }

    public int getIdImagen(){
        return idImagen;
    }

    public int getId(){
        return nombre.hashCode();
    }

    public static gasCompany[] ITEMS={
            new gasCompany("OxxoGas",R.drawable.oxxogaslogo),
            new gasCompany("Esso",R.drawable.essogaslogo),
            new gasCompany("Mobil",R.drawable.mobilgaslogo),
            new gasCompany("Pemex",R.drawable.pemexgaslogo),
    };

    public static gasCompany getItem(int id){
        for(gasCompany item: ITEMS){
            if(item.getId()==id){
                return item;
            }
        }

        return null;
    }
}
