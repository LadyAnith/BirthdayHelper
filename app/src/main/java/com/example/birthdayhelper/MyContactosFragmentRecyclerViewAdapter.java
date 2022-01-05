package com.example.birthdayhelper;

import androidx.recyclerview.widget.RecyclerView;

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
public class MyContactosFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MyContactosFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MyContactosFragmentRecyclerViewAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView foto;
        public final TextView nombre;
        public final TextView telefono;
        public final TextView fechaNacimiento;
        public final TextView notificacion;


        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            foto = binding.imgContact;
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