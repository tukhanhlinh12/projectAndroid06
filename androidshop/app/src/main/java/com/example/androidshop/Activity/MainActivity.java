package com.example.androidshop.Activity;

import com.example.androidshop.Adapter.CategoryAdapter;
import com.example.androidshop.Adapter.PopularAdapter;
import com.example.androidshop.Adapter.SliderAdapter;
import com.example.androidshop.Domain.CategoryDomain;
import com.example.androidshop.Domain.ItemsDomain;
import com.example.androidshop.Domain.SliderItems;
import com.example.androidshop.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
        initPopular();
        bottomNavigation();
    }

    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(MainActivity.this,ProfileActivity.class)));
            }
        });
    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items=new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                    if (!items.isEmpty()){
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarOffical.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(CategoryDomain.class));
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewOffical.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewOffical.setAdapter(new CategoryAdapter(items));

                    }
                    binding.progressBarOffical.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                        for(DataSnapshot issue:snapshot.getChildren()){
                            items.add(issue.getValue(SliderItems.class));
                        }
                        banners(items);
                        binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void banners(ArrayList<SliderItems> items) {
        binding.viewpagerSlider.setAdapter(new SliderAdapter(items,binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }
}