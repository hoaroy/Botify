package com.example.Sachpee.Fragment.ProductFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Sachpee.Adapter.ProductAdapter;
import com.example.Sachpee.Model.Product;
import com.example.Sachpee.Model.ProductDetail;
import com.example.Sachpee.R;
import com.example.Sachpee.Service.ApiClient;
import com.example.Sachpee.Service.ApiService;
  

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KinhteFragment extends Fragment {

    private List<Product> listKinhte = new ArrayList<>();
    private RecyclerView rvKinhte;
    private LinearLayoutManager linearLayoutManager;
    private ProductAdapter adapter;
    private View view;
    private ProductFragment fragment = new ProductFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kinhte, container, false);
        unitUI();

        return view;
    }
    public void unitUI(){
        getKinhteProducts();
        rvKinhte = view.findViewById(R.id.rvKinhte);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvKinhte.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter(listKinhte,fragment,getContext());
        rvKinhte.setAdapter(adapter);
        rvKinhte.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onAddDetailClick(Product product) {
                addDetail(product);
            }
        });
    }
    // Thêm chi tiết sản phẩm
    // Thêm chi tiết sản phẩm
    private void addDetail(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm chi tiết cho sản phẩm: " + product.getNameProduct());

        // Thiết lập layout cho dialog
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_detail, null);
        builder.setView(dialogView);

        EditText etAuthor = dialogView.findViewById(R.id.etAuthor);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        Button btnAddDetail = dialogView.findViewById(R.id.btnAddDetail);

        // Thiết lập sự kiện click cho nút Lưu
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String author = etAuthor.getText().toString();
            String description = etDescription.getText().toString();

            // Kiểm tra thông tin người dùng nhập
            if (author.isEmpty() || description.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy thông tin sản phẩm từ routes/products.js
            int codeCategory = product.getCodeCategory(); // Lấy từ product
            String nameProduct = product.getNameProduct(); // Lấy từ product
            String imgProduct = product.getImgProduct(); // Lấy từ product

            // Tạo đối tượng ProductDetail
            ProductDetail productDetail = new ProductDetail(codeCategory, nameProduct, imgProduct, author, description);

            // Gọi API để lưu dữ liệu vào database
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            Call<Void> call = apiService.addProductDetail(productDetail);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Chi tiết sản phẩm đã được lưu!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi lưu chi tiết sản phẩm!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void getKinhteProducts() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Vui lòng đợi ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Product>> call = apiService.getAllProducts(); //  API trả về tất cả sản phẩm

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    listKinhte.clear(); // Xóa dữ liệu cũ

                    for (Product product : response.body()) {
                        if (product.getCodeCategory() == 2) { // Kiểm tra sản phẩm với mã category = 2
                            listKinhte.add(product);
                        }
                    }

                    adapter.notifyDataSetChanged(); // Cập nhật adapter với dữ liệu mới
                    Log.d("KinhteFragment", "Danh sách sách Kinh tế đã được lấy thành công: " + listKinhte.size());
                } else {
                    Log.e("KinhteFragment", "Lỗi khi lấy dữ liệu từ API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("KinhteFragment", "Lỗi kết nối khi gọi API: " + t.getMessage());
            }
        });
    }

}