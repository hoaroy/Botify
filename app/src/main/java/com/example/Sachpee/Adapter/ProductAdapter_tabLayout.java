package com.example.Sachpee.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.Sachpee.Fragment.ProductFragments.BookFragment;
import com.example.Sachpee.Fragment.ProductFragments.GiaokhoaFragment;
import com.example.Sachpee.Fragment.ProductFragments.HoikyFragment;
import com.example.Sachpee.Fragment.ProductFragments.KinhteFragment;
import com.example.Sachpee.Fragment.ProductFragments.NgoainguFragment;
import com.example.Sachpee.Fragment.ProductFragments.TamlyFragment;
import com.example.Sachpee.Fragment.ProductFragments.ThieunhiFragment;
import com.example.Sachpee.Fragment.ProductFragments.VanhocFragment;

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
            case 3: return new BookFragment();
            case 4: return new ThieunhiFragment();
            case 5: return new HoikyFragment();
            case 6: return new GiaokhoaFragment();
            default: return new NgoainguFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 8;
    }
}
