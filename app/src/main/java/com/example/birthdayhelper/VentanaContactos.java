package com.example.birthdayhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import com.example.birthdayhelper.entity.Contacto;

public class VentanaContactos extends AppCompatActivity {
    private ImageView imagePhoto;
    private EditText textName;
    private CheckBox checkSms;
    private Spinner spinnerPhone;
    private EditText textDate;
    private EditText textMensaje;
    private Contacto detallesContacto;
    private Button btnEditar;

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
        btnEditar = findViewById(R.id.btnEditar);
    }
    private void initValues(){
        detallesContacto = (Contacto) getIntent().getExtras().getSerializable("itemContacto");
        imagePhoto.setImageBitmap(detallesContacto.getFoto());
        textName.setText(detallesContacto.getNombre());
        textDate.setText(detallesContacto.getFechaNacimiento());
        textMensaje.setText(detallesContacto.getMensaje());
        textMensaje.setEnabled(false);
        checkSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSms.isChecked()){
                    textMensaje.setEnabled(true);
                } else{
                    textMensaje.setEnabled(false);
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, detallesContacto.getTelefonos());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(adapter);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactById(detallesContacto.getId());
            }
        });
    }

    public void openContactById(int idContacto){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(idContacto));
        intent.setData(uri);
        startActivity(intent);

    }

}