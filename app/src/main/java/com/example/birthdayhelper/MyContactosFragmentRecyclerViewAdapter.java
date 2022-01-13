package com.example.birthdayhelper;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birthdayhelper.entity.Contacto;
import com.example.birthdayhelper.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.birthdayhelper.databinding.FragmentItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyContactosFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MyContactosFragmentRecyclerViewAdapter.ViewHolder>{

    private Context context;

    public MyContactosFragmentRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Contacto itemContacto = MainActivity.listaContactos.get(position);
        holder.foto.setImageBitmap(MainActivity.listaContactos.get(position).getFoto());
        holder.nombre.setText(MainActivity.listaContactos.get(position).getNombre());
        holder.telefono.setText(MainActivity.listaContactos.get(position).getTelefono());
        holder.fechaNacimiento.setText(MainActivity.listaContactos.get(position).getFechaNacimiento());
        if(MainActivity.listaContactos.get(position).getTipoNotif().equals("N")){
            holder.notificacion.setText("Aviso:Solo Notificación");
        } else{
            holder.notificacion.setText("Aviso:Enviar SMS");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), VentanaContactos.class);
                intent.putExtra("itemContacto", (Parcelable) itemContacto);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.listaContactos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public Contacto item;

        public final ImageView foto;
        public final TextView nombre;
        public final TextView telefono;
        public final TextView fechaNacimiento;
        public final TextView notificacion;


        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());

            foto = binding.imgPhoto;
            nombre = binding.txtName;
            telefono = binding.txtPhone;
            fechaNacimiento = binding.txtBirthday;
            notificacion = binding.txtNotification;
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "foto=" + foto +
                    ", nombre=" + nombre +
                    ", telefono=" + telefono +
                    ", fechaNacimiento=" + fechaNacimiento +
                    ", notificacion=" + notificacion +
                    '}';
        }
    }
}