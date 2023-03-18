package com.example.getquirky.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.getquirky.R;
import com.example.getquirky.databinding.ItemCartBinding;
import com.example.getquirky.databinding.QuantitydialogeBinding;
import com.example.getquirky.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Objects;

public  class  CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    Context context;

    ArrayList<Product>products;
    CartListner cartListner;
    Cart cart;
    public interface CartListner{
        public void onQuantityChanged();
    }

    public CartAdapter(Context context , ArrayList<Product>products,CartListner cartListner){
        this.context = context;
        this.products = products;
        this.cartListner = cartListner;
        cart = TinyCartHelper.getCart();
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent , false));
    }



    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);
        holder.binding.name.setText(product.getName());
        holder.binding.price.setText("₹" + product.getPrice());

      holder.binding.quantity.setText(product.getQuantity() + "item(s)");

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        QuantitydialogeBinding quantitydialogeBinding = QuantitydialogeBinding.inflate(LayoutInflater.from(context));

        AlertDialog dialog = new AlertDialog.Builder(context).setView(quantitydialogeBinding.getRoot()).create();
dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));


quantitydialogeBinding.productName.setText(product.getName());
quantitydialogeBinding.productStock.setText("stock: "+ product.getStock());

quantitydialogeBinding.quantity.setText(String.valueOf(product.getQuantity()));
int stock =  product.getStock();

//plus btn
quantitydialogeBinding.plusbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        int quantity = product.getQuantity();
        quantity++;
        if (quantity>product.getStock()){
            Toast.makeText(context, "Max stock available : "+product.getStock(), Toast.LENGTH_SHORT).show();
       return;
        }else {
        product.setQuantity(quantity);
        quantitydialogeBinding.quantity.setText(String.valueOf(quantity));

            notifyDataSetChanged();
            cart.updateItem(product,product.getQuantity());
            cartListner.onQuantityChanged();

        }}
});
//minus btn

quantitydialogeBinding.minusbtn.setOnClickListener(new View.OnClickListener() {
    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onClick(View view) {
        int quantity = product.getQuantity();
        if (quantity > 1)
        quantity --;
        product.setQuantity(quantity);
        quantitydialogeBinding.quantity.setText(String.valueOf(quantity));

        notifyDataSetChanged();
        cart.updateItem(product,product.getQuantity());
        cartListner.onQuantityChanged();

    }
});
//save btn
quantitydialogeBinding.savebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        dialog.dismiss();
   //     notifyDataSetChanged();
     //   cart.updateItem(product,product.getQuantity());
       // cartListner.onQuantityChanged();


    }
});
        dialog.show();

    }
       });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public  class CartViewHolder extends RecyclerView.ViewHolder{
ItemCartBinding binding;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemCartBinding.bind(itemView);
        }
    }

}
