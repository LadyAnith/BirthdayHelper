package com.example.birthdayhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.birthdayhelper.entity.Contacto;

public class VentanaContactos extends AppCompatActivity {
    private ImageView imagePhoto;
    private EditText textName;
    private CheckBox checkSms;
    private Spinner spinnerPhone;
    private EditText textDate;
    private EditText textMensaje;
    private Contacto detallesContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_contactos);
        initViews();
        initValues();

    }
    private  void initViews(){
        imagePhoto = findViewById(R.id.imgFoto);
        textName= findViewById(R.id.txtNombre);
        checkSms= findViewById(R.id.checkSms);
        spinnerPhone= findViewById(R.id.spinnerTlf);
        textDate= findViewById(R.id.txtFecha);
        textMensaje= findViewById(R.id.txtMensaje);
    }
    private void initValues(){
        detallesContacto = (Contacto) getIntent().getExtras().getSerializable("itemContacto");
        imagePhoto.setImageBitmap(detallesContacto.getFoto());
        textName.setText(detallesContacto.getNombre());
        textDate.setText(detallesContacto.getFechaNacimiento());
        textMensaje.setText(detallesContacto.getMensaje());
    }
}