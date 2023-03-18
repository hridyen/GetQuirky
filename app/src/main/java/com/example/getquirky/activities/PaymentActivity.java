package com.example.getquirky.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.getquirky.databinding.ActivityPaymentBinding;
import com.example.getquirky.utilities.Constants;

public class PaymentActivity extends AppCompatActivity {
ActivityPaymentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String orderCode = getIntent().getStringExtra("orderCode");
        //webview definning to get url
        binding.webview.setMixedContentAllowed(true);
        binding.webview.loadUrl(Constants.PAYMENT_URL + orderCode);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}