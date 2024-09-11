package com.example.thucphamxanh2.Fragment.BottomNav;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thucphamxanh2.Adapter.Partner_BookAdapter;
import com.example.thucphamxanh2.Model.Partner;
import com.example.thucphamxanh2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PartnerBookFragment extends Fragment implements Partner_BookAdapter.ItemClickListener{
    RecyclerView recyclerView_Partner_Book;
    LinearLayoutManager linearLayoutManager;
    List<Partner> list;
    Partner_BookAdapter partner_bookAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_partner_food, container, false);


        recyclerView_Partner_Book = view.findViewById(R.id.recyclerView_Partner_Book);

        list = getAllPartner();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_Partner_Book.setLayoutManager(linearLayoutManager);

        partner_bookAdapter = new Partner_BookAdapter(list,this);
        recyclerView_Partner_Book.setAdapter(partner_bookAdapter);



        return view;
    }

    public List<Partner> getAllPartner(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Partner");
        List<Partner> list1 = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                for (DataSnapshot snap : snapshot.getChildren()){
                    Partner partner = snap.getValue(Partner.class);
                    list1.add(partner);
                }
                partner_bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list1;
    }


    @Override
    public void onItemClick(Partner partner) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Partner", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("partner",partner.getUserPartner());
        editor.apply();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_Home, new Book_Of_PartnerFragment(),null).addToBackStack(null).commit();
    }
}