package com.example.Sachpee.Fragment.ProductFragments;

import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.Sachpee.R;

public class DetailFragment extends Fragment {

    private ImageView ivBookImage;
    private TextView tvBookName, tvAuthorName, tvCategory, tvDescription;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Initialize views
        ivBookImage = view.findViewById(R.id.ivBookImage);
        tvBookName = view.findViewById(R.id.tvBookName);
        tvAuthorName = view.findViewById(R.id.tvAuthorName);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvDescription = view.findViewById(R.id.tvDescription);

        // Get arguments passed from previous fragment (if any)
        Bundle bundle = getArguments();
        if (bundle != null) {
            String bookName = bundle.getString("nameProduct");
            String authorName = bundle.getString("author");
            String category = bundle.getString("codeCategory");
            String description = bundle.getString("description");
            String imgUrl = bundle.getString("imgProduct");

            // Set data to views
            tvBookName.setText(bookName);
            tvAuthorName.setText(authorName);
            tvCategory.setText(category);
            tvDescription.setText(description);

            // Load image (you can use Glide or Picasso for better performance)
            Glide.with(this).load(imgUrl).into(ivBookImage);
        }

        return view;
    }
}

