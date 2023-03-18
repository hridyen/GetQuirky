package com.example.getquirky.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.getquirky.R;
import com.example.getquirky.adapter.CategoryAdapter;
import com.example.getquirky.adapter.ProductAdapter;
import com.example.getquirky.databinding.ActivityMainBinding;
import com.example.getquirky.model.Category;
import com.example.getquirky.model.Product;
import com.example.getquirky.utilities.Constants;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
CategoryAdapter categoryAdapter;
ArrayList<Category>categories;

ProductAdapter productAdapter;
ArrayList<Product>products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);

                    intent.putExtra("querry", text.toString());
                    startActivity(intent);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

initCategories();
initProducts();
initSlider();
    
    }

    private void initSlider() {
        getRecentOffers();
    }

    void initCategories(){    categories = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(this, categories);
       getCategories();
        GridLayoutManager layoutManager = new GridLayoutManager( this , 4);
        binding.categorieslist.setLayoutManager(layoutManager);
        binding.categorieslist.setAdapter(categoryAdapter);


    }

    void getCategories(){

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("err",response );
                    JSONObject mainObj = new JSONObject(response);
                    if (mainObj.getString("status").equals("success")){
                        JSONArray categoriesArray = mainObj.getJSONArray("categories");
                        for (int i = 0 ; i<categoriesArray.length();i++) {
                            JSONObject object = categoriesArray.getJSONObject(i);
                            Category Category = new Category(
                                    object.getString("name"),
                                   Constants.CATEGORIES_IMAGE_URL + object.getString("icon"),
                                    object.getString("color"),
                                    object.getString("brief"),
                                    object.getInt("id")
                            );
                            categories.add(Category);
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }else {
                        //do nothing
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        } ,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);

    }

    void getRecentProducts(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.GET_PRODUCTS_URL + "?count = 8";


        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")){
                    JSONArray productsArray = object.getJSONArray("products");
                    for (int i = 0; i<productsArray.length(); i++){
                        JSONObject childobject = productsArray.getJSONObject(i);
                        Product product = new Product(
                                childobject.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childobject.getString("image"),
                                childobject.getString("status"),
                                childobject.getDouble("price"),
                                childobject.getDouble("price_discount"),
                                childobject.getInt("stock"),
                                childobject.getInt("id")
                        );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


        }, error -> {


        });
        queue.add(request);
    }

    void getRecentOffers(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, Constants.GET_OFFERS_URL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("status").equals("success")){
                    JSONArray offerArray = object.getJSONArray("news_infos");
                    for (int i = 0 ; i<offerArray.length(); i++){
                        JSONObject childobject = offerArray.getJSONObject(i);
                        binding.carousel.addData(
                                new CarouselItem(
                                       Constants.NEWS_IMAGE_URL + childobject.getString("image"),
                                        childobject.getString("title")
                                )
                        );
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error ->  {});
        queue.add(request);
    }

    void  initProducts(){
        products= new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);

     getRecentProducts();

        
        GridLayoutManager  layoutManager = new GridLayoutManager(this , 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
        
        
        
    }
}