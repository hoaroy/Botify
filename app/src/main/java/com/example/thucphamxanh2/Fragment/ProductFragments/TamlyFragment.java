package com.example.thucphamxanh2.Fragment.ProductFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thucphamxanh2.Adapter.ProductAdapter;
import com.example.thucphamxanh2.Model.Product;
import com.example.thucphamxanh2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TamlyFragment extends Fragment {

    private List<Product> listTamly;
    private RecyclerView rvTamly;
    
    private LinearLayoutManager linearLayoutManager;
    private ProductAdapter adapter;
    private View view;
    private ProductFragment fragment= new ProductFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tamly, container, false);
        initUI();
        return view;
    }

    public void initUI(){
        listTamly = getVanhocProduct();
        rvTamly = view.findViewById(R.id.rvTamly);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTamly.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter(listTamly,fragment, getActivity());
        rvTamly.setAdapter(adapter);
        rvTamly.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    public  List<Product> getVanhocProduct(){
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Vui lòng đợi ...");
        progressDialog.setCanceledOnTouchOutside(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Product");
        List<Product> list1 = new ArrayList<>();
        //TODO sửa dialog khi load dữ liệu từ firebase lên fragment
        progressDialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                list1.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Product product = snap.getValue(Product.class);
                    if (product!=null) {
                        if (product.getCodeCategory() == 3) {
                            list1.add(product);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list1;
    }
}