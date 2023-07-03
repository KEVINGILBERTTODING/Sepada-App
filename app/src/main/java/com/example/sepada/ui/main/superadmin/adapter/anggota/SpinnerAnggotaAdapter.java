package com.example.sepada.ui.main.superadmin.adapter.anggota;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.sepada.data.model.AnggotaModel;

import java.util.List;

public class SpinnerAnggotaAdapter extends ArrayAdapter<AnggotaModel> {

   public SpinnerAnggotaAdapter(@NonNull Context context, List<AnggotaModel> categoriesModel){
            super(context, android.R.layout.simple_spinner_item, categoriesModel);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getNamaLengkap());
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setText(getItem(position).getNamaLengkap());
            return view;
        }



    public String getCategoriesId(int position) {
        return getItem(position).getAnggotaId();
    }





    }
