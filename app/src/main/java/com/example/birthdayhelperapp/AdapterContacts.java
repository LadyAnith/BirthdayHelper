package com.example.birthdayhelperapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birthdayhelperapp.entity.Contacto;

import java.util.List;

public class AdapterContacts extends RecyclerView.Adapter<AdapterContacts.ViewHolderContactos> {
    List<Contacto> listaContactos;

    public AdapterContacts(List<Contacto> listaContactos) {
        this.listaContactos = listaContactos;
    }

    @NonNull
    @Override
    public ViewHolderContactos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null, false);
        return new ViewHolderContactos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderContactos holder, int position) {
        final Contacto itemContacto = listaContactos.get(position);
        holder.foto.setImageBitmap(listaContactos.get(position).getFoto());
        holder.nombre.setText(listaContactos.get(position).getNombre());
        holder.telefono.setText(listaContactos.get(position).getTelefono());
        holder.fechaNacimiento.setText(listaContactos.get(position).getFechaNacimiento());
        if(listaContactos.get(position).getTipoNotif().equals("N")){
            holder.notificacion.setText("Aviso:Solo Notificación");
        } else{
            holder.notificacion.setText("Aviso:Enviar SMS");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), VentanaContactos.class);
                intent.putExtra("itemContacto", itemContacto.getNombre());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.listaContactos.size();
    }

    public class ViewHolderContactos extends RecyclerView.ViewHolder {

        public final ImageView foto;
        public final TextView nombre;
        public final TextView telefono;
        public final TextView fechaNacimiento;
        public final TextView notificacion;


        public ViewHolderContactos(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgPhoto);
            nombre = itemView.findViewById(R.id.txtName);
            telefono = itemView.findViewById(R.id.txtPhone);
            fechaNacimiento = itemView.findViewById(R.id.txtBirthday);
            notificacion = itemView.findViewById(R.id.txtNotification);
        }
    }
}
