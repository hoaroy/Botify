package com.example.thucphamxanh2.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.thucphamxanh2.Fragment.ProductFragments.FoodFragment;
import com.example.thucphamxanh2.Fragment.ProductFragments.KinhteFragment;
import com.example.thucphamxanh2.Fragment.ProductFragments.TamlyFragment;
import com.example.thucphamxanh2.Fragment.ProductFragments.VanhocFragment;

public class ProductAdapter_tabLayout extends FragmentStateAdapter {
    public ProductAdapter_tabLayout(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new VanhocFragment();
            case 1: return new KinhteFragment();
            case 2: return new TamlyFragment();
            default: return new FoodFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
