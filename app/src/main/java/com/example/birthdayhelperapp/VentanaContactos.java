package com.example.birthdayhelperapp;

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

import com.example.birthdayhelperapp.entity.Contacto;
import com.example.birthdayhelperapp.repository.MisCumplesRepository;


public class VentanaContactos extends AppCompatActivity {
    private ImageView imagePhoto;
    private EditText textName;
    private CheckBox checkSms;
    private Spinner spinnerPhone;
    private EditText textDate;
    private EditText textMensaje;
    private Contacto detallesContacto;
    private Button btnEditar;
    private Button btnGuardar;
    private MisCumplesRepository misCumplesRepository = new MisCumplesRepository(this );

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
        btnGuardar = findViewById(R.id.btnGuardar);
    }
    private void initValues(){
        String keyMap = (String) getIntent().getExtras().getSerializable("itemContacto");
        detallesContacto = MainActivity.listaContactos.get(keyMap) ;
        imagePhoto.setImageBitmap(detallesContacto.getFoto());
        textName.setText(detallesContacto.getNombre());
        textDate.setText(detallesContacto.getFechaNacimiento());
        textMensaje.setText(detallesContacto.getMensaje());
        textMensaje.setEnabled(false);
        if(("S").equals(detallesContacto.getTipoNotif())){
            checkSms.setChecked(true);
            textMensaje.setEnabled(true);
        }
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

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                detallesContacto.setNombre(textName.getText().toString());
                detallesContacto.setFechaNacimiento(textDate.getText().toString());
                detallesContacto.setMensaje(textMensaje.getText().toString());
                detallesContacto.setTelefono(spinnerPhone.getSelectedItem().toString());

                detallesContacto.setTipoNotif("N");
                if(checkSms.isChecked()){
                    detallesContacto.setTipoNotif("S");
                }

                misCumplesRepository.insertOrUpdate(detallesContacto);

                Intent i = new Intent(VentanaContactos.this, ContactsActivity.class);
                startActivity(i);

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