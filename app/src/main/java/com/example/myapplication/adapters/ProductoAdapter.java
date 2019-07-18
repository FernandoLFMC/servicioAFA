package com.example.myapplication.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DetalleProductoCompradorFragment;
import com.example.myapplication.R;
import com.example.myapplication.items.ItemProducto;
import com.example.myapplication.utils.Data;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private Context context;
    private ArrayList<ItemProducto> listData;



    public ProductoAdapter(Context context, ArrayList<ItemProducto> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto,parent,false);

        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, final int position) {
        holder.setData(listData.get(position),position);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                DetalleProductoCompradorFragment fragment = new DetalleProductoCompradorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("idProducto",listData.get(position).getId());

                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.esenarioComprador,fragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return listData == null ? 0 : listData.size();
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFoto;
        TextView textDescripcion, textPrecio, textStock;
        ConstraintLayout parentLayout;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFoto = itemView.findViewById(R.id.imageFoto);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            textPrecio = itemView.findViewById(R.id.textPrecio);
            textStock = itemView.findViewById(R.id.textStock);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }

        public void setData(ItemProducto itemProducto, int position) {
            textDescripcion.setText(itemProducto.getDescripcion());
            textPrecio.setText(textPrecio.getText().toString() + itemProducto.getPrecio());
            textStock.setText(textStock.getText().toString() + itemProducto.getStock());
            Glide.with(context).load(Data.IP + itemProducto.getFoto()).into(imageFoto);
        }
    }



}
