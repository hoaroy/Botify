package com.example.Sachpee.Fragment.BottomNav;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Sachpee.Adapter.ImageSliderAdapter;
import com.example.Sachpee.Adapter.ProductAdapter;
import com.example.Sachpee.Fragment.ProductFragments.KinhteFragment;
import com.example.Sachpee.Fragment.ProductFragments.TamlyFragment;
import com.example.Sachpee.Fragment.ProductFragments.ProductFragment;
import com.example.Sachpee.Fragment.ProductFragments.VanhocFragment;
import com.example.Sachpee.Model.ImageSlider;
import com.example.Sachpee.Model.Product;
import com.example.Sachpee.Model.ProductTop;
import com.example.Sachpee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;


public class HomePageFragment extends Fragment {

    private List<ProductTop> productToplistVanhoc = new ArrayList<>();
    private List<ProductTop> productToplistTamly= new ArrayList<>();
    private List<ProductTop> productToplistKinhte= new ArrayList<>();
    private List<ProductTop> productTopListFood= new ArrayList<>();
    private List<Product> listVanhoc = new ArrayList<>();
    private List<Product> listTamly= new ArrayList<>();
    private List<Product> listKinhte= new ArrayList<>();
    private List<Product> listFood= new ArrayList<>();
    private List<Product> listProduct = new ArrayList<>();
    ProductAdapter adapter;

    CardView card_vanhoc_home,card_kinhte_home,card_tamly_home,card_giaoduc_home;

    CardView card_trending_home,card_Top_Fruit,card_Top_Meat,card_Top_Food;

    RecyclerView rv_trending_home,rv_FruitTop_Home,rv_MeatTop_Home,rv_FoodTop_Home;

    ImageView arrow1,arrow2,arrow3,arrow4;
    private ProductFragment fragment = new ProductFragment();

    ///imgview
    private List<ImageSlider> list = new ArrayList<>();
    private ViewPager viewPager;
    private ImageSliderAdapter imageSliderAdapter;
    private CircleIndicator circleIndicator;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        //viewslider
        viewPager = view.findViewById(R.id.main_slider_image);
        circleIndicator = view.findViewById(R.id.circle_indicator);

        list.add(new ImageSlider(R.drawable.banner_0803));
        list.add(new ImageSlider(R.drawable.banner_book));
        list.add(new ImageSlider(R.drawable.banner_kim_dong));
        list.add(new ImageSlider(R.drawable.banner_fahasa));

        imageSliderAdapter = new ImageSliderAdapter(getContext(), list);
        viewPager.setAdapter(imageSliderAdapter);
        circleIndicator.setViewPager(viewPager);

        adapter = new ProductAdapter(listVanhoc,fragment,getContext());
        getTopProduct();
        getProduct();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);

        card_vanhoc_home = view.findViewById(R.id.card_vanhoc_home);
        card_kinhte_home = view.findViewById(R.id.card_kinhte_home);
        card_tamly_home = view.findViewById(R.id.card_tamly_home);
        card_giaoduc_home = view.findViewById(R.id.card_giaoduc_home);

        card_trending_home = view.findViewById(R.id.card_trending_home);
        card_Top_Fruit = view.findViewById(R.id.card_Top_Fruit);
        card_Top_Meat = view.findViewById(R.id.card_Top_Meat);
        card_Top_Food = view.findViewById(R.id.card_Top_Food);

        rv_trending_home = view.findViewById(R.id.rv_trending_home);
        rv_FruitTop_Home = view.findViewById(R.id.rv_FruitTop_Home);
        rv_MeatTop_Home = view.findViewById(R.id.rv_MeatTop_Home);
        rv_FoodTop_Home = view.findViewById(R.id.rv_FoodTop_Home);
        card_vanhoc_home.setOnClickListener(view1 -> {
            fragmentManager.beginTransaction().replace(R.id.frame_Home, new VanhocFragment(),null).addToBackStack(null).commit();

        });
        card_kinhte_home.setOnClickListener(view1 -> {
            fragmentManager.beginTransaction().replace(R.id.frame_Home, new KinhteFragment(),null).addToBackStack(null).commit();
        });
        card_tamly_home.setOnClickListener(view1 -> {

            fragmentManager.beginTransaction().replace(R.id.frame_Home, new TamlyFragment(),null).addToBackStack(null).commit();
        });
        card_giaoduc_home.setOnClickListener(view1 -> {

            fragmentManager.beginTransaction().replace(R.id.frame_Home, new PartnerBookFragment(),null).addToBackStack(null).commit();
        });


        card_trending_home.setOnClickListener(view1 -> {
            onClickItemCart(listVanhoc,rv_trending_home);
        });
        card_Top_Fruit.setOnClickListener(view1 -> {
            onClickItemCart(listKinhte,rv_FruitTop_Home);
        });
        card_Top_Meat.setOnClickListener(view1 -> {
            onClickItemCart(listTamly,rv_MeatTop_Home);
        });
        card_Top_Food.setOnClickListener(view1 -> {

            onClickItemCart(listFood,rv_FoodTop_Home);

        });


        return view;
    }

    public void getTopProduct(){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("ProductTop");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   productToplistVanhoc.clear();
                    productTopListFood.clear();
                    productToplistKinhte.clear();
                    productToplistTamly.clear();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        ProductTop top = snapshot1.getValue(ProductTop.class);
                        if (top.getIdCategory()==1){
                            productToplistVanhoc.add(top);
                        }else  if (top.getIdCategory()==2){
                            productToplistKinhte.add(top);
                        }else  if (top.getIdCategory()==3){
                            productToplistTamly.add(top);
                        }else {productTopListFood.add(top);
                        }
                    }
                    getProduct();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    public void getProduct(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTamly.clear();
                listKinhte.clear();
                listVanhoc.clear();
                listFood.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Product top = snapshot1.getValue(Product.class);
                        listProduct.add(top);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Collections.sort(productToplistVanhoc,Comparator.comparing(ProductTop::getAmountProduct).reversed());
                    Collections.sort(productToplistKinhte,Comparator.comparing(ProductTop::getAmountProduct).reversed());
                    Collections.sort(productTopListFood,Comparator.comparing(ProductTop::getAmountProduct).reversed());
                    Collections.sort(productToplistTamly,Comparator.comparing(ProductTop::getAmountProduct).reversed());
                }
                add(productToplistVanhoc,listProduct,listVanhoc);
                add(productToplistKinhte,listProduct,listKinhte);
                add(productTopListFood,listProduct,listFood);
                add(productToplistTamly,listProduct,listTamly);
                collections(listVanhoc);
                collections(listKinhte);
                collections(listFood);
                collections(listTamly);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
public void add(List<ProductTop> listTop, List<Product> listProduct ,List<Product> listProductTop ){
    for (int i = 0; i < listTop.size(); i++) {
        for (int j = 0; j < listProduct.size(); j++) {
            if (listTop.get(i).getIdProduct() == listProduct.get(j).getCodeProduct() ){
                listProductTop.add(listProduct.get(j));
            }
        }

    }
}
    public void collections(List<Product> listProductTop ){
//        for (int i = 0; i < listTop.size(); i++) {
//            for (int j = 0; j < listProduct.size(); j++) {
//                if (listTop.get(i).getIdProduct() == listProduct.get(j).getCodeProduct() ){
//                    listProductTop.add(listProduct.get(j));
//                }
//            }
//
//        }
        try {
            for (int i = 0; i < listProductTop.size(); i++) {
                for (int j = 1; j < listProductTop.size(); j++) {
                    if (listProductTop.get(i).getCodeProduct() == listProductTop.get(j).getCodeProduct() ){
                        listProductTop.remove(listProductTop.get(i));
                    }
                }
            }
        }catch (Exception e){

        }

    }

    public void onClickItemCart(List<Product> list,RecyclerView recyclerView){
        if (recyclerView.getVisibility() == View.GONE){
            arrow1.setImageResource(R.drawable.ic_arrow_drop_down);
            arrow2.setImageResource(R.drawable.ic_arrow_drop_down);
            arrow3.setImageResource(R.drawable.ic_arrow_drop_down);
            arrow4.setImageResource(R.drawable.ic_arrow_drop_down);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ProductAdapter(list,fragment,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else {
            recyclerView.setVisibility(View.GONE);
            arrow1.setImageResource(R.drawable.ic_arrow_drop_up);
            arrow2.setImageResource(R.drawable.ic_arrow_drop_up);
            arrow3.setImageResource(R.drawable.ic_arrow_drop_up);
            arrow4.setImageResource(R.drawable.ic_arrow_drop_up);
        }
    }







}