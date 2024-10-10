package com.example.Sachpee.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Sachpee.Activity.PaymentWebViewActivity;
import com.example.Sachpee.Model.Bill;
import com.example.Sachpee.Model.Cart;
import com.example.Sachpee.Model.PaymentRequest;
import com.example.Sachpee.Model.PaymentResponse;
import com.example.Sachpee.Model.User;
import com.example.Sachpee.R;
import com.example.Sachpee.Service.ApiClient;
import com.example.Sachpee.Service.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterBill extends RecyclerView.Adapter<AdapterBill.ViewHolder> {
    private List<Bill> list;
    private Context context;
    private ApiService apiService;

    public AdapterBill(List<Bill> list, Context context, ApiService apiService) {
        this.list = list;
        this.context = context;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferences preferences = context.getSharedPreferences("My_User", Context.MODE_PRIVATE);
        String role = preferences.getString("role", "");
        Bill bill = list.get(position);

        holder.tvidBill.setText("Mã HD :" + bill.getIdBill());
        holder.tvPhone.setText("Số điện thoại : " + bill.getIdClient());
        holder.tvTime.setText("Thời gian: " + bill.getTimeOut());
        holder.tvDay.setText(String.valueOf(bill.getDayOut()));
        holder.tvTotal.setText(String.valueOf(bill.getTotal()));

        holder.linearLayout_item_product.setOnClickListener(view -> {
            if (holder.rvItemOrder.getVisibility() == View.GONE) {
                holder.rvItemOrder.setVisibility(View.VISIBLE);
                holder.img_drop_up.setImageResource(R.drawable.ic_arrow_drop_down);
            } else {
                holder.rvItemOrder.setVisibility(View.GONE);
                holder.img_drop_up.setImageResource(R.drawable.ic_arrow_drop_up);
            }
        });

        holder.btn_updateStatusBill.setOnClickListener(view -> {
            updateBillStatus(String.valueOf(bill.getIdBill()));
        });

        holder.card_bill.setOnClickListener(view -> {
            handleButtonVisibility(holder, role, bill);
        });

        holder.btn_paymentBill.setOnClickListener(view -> {
            handlePayment(bill);
        });

        // Thiết lập LinearLayoutManager cho RecyclerView
        holder.rvItemOrder.setLayoutManager(new LinearLayoutManager(context));

        // Lấy dữ liệu giỏ hàng và cập nhật RecyclerView
        getAllCart(bill.getIdBill(), holder.rvItemOrder);
    }


    private void handleButtonVisibility(ViewHolder holder, String role, Bill bill) {
        if (!role.equals("user")) {
            holder.btn_updateStatusBill.setVisibility(holder.btn_updateStatusBill.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        } else {
            holder.btn_paymentBill.setVisibility(holder.btn_paymentBill.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }

        if (bill.getStatus().equals("Yes")) {
            holder.btn_updateStatusBill.setVisibility(View.GONE);
            holder.btn_paymentBill.setVisibility(View.GONE);
        }
    }

    private void getAllCart(int idBill, RecyclerView recyclerView) {
        Call<List<Cart>> call = apiService.getCartsByBillId(idBill);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cart> listCart = response.body();
                    Log.d("AdapterBill", "Cart data retrieved successfully: " + listCart);
                    AdapterItemBill adapterItemBill = new AdapterItemBill(listCart);
                    recyclerView.setAdapter(adapterItemBill);
                    adapterItemBill.notifyDataSetChanged();
                } else {
                    Log.e("AdapterBill", "Error fetching cart data: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("AdapterBill", "Failed to retrieve cart data: " + t.getMessage());
            }
        });
    }

    private void updateBillStatus(String idBill) {
        Call<Void> call = apiService.updateBillStatus(idBill, "Yes");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("AdapterBill", "Bill status updated successfully");
                } else {
                    Log.e("AdapterBill", "Error updating bill status: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AdapterBill", "Failed to update bill status: " + t.getMessage());
            }
        });
    }


    // AdapterBill.java (Phương thức handlePayment)
    private void handlePayment(Bill bill) {
        // Lấy thông tin cần thiết từ bill
        String appUser = bill.getIdClient();
        int amount = bill.getTotal();
        String description = "Payment for bill #" + bill.getIdBill();


        PaymentRequest paymentRequest = new PaymentRequest(appUser, amount, description);

        // Gửi yêu cầu thanh toán tới server
        apiService.createPayment(paymentRequest).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PaymentResponse paymentResponse = response.body();
                    if (paymentResponse.getReturnCode() == 1) {
                        Log.d("AdapterBill", "Payment successful: " + paymentResponse.getOrderUrl());
                        // Mở PaymentWebViewActivity và truyền URL thanh toán
                        Intent intent = new Intent(context, PaymentWebViewActivity.class);
                        intent.putExtra("ORDER_URL", paymentResponse.getOrderUrl());
                        context.startActivity(intent);

                    } else {
                        // Xử lý khi thanh toán thất bại theo phản hồi từ server
                        Log.e("AdapterBill", "Payment failed: " + paymentResponse.getReturnMessage());
                        Toast.makeText(context, "Thanh toán thất bại: " + paymentResponse.getReturnMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("AdapterBill", "Error creating payment: " + response.message() + " - " + errorBody);
                        Toast.makeText(context, "Lỗi thanh toán: " + response.message(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("AdapterBill", "Error reading error body: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.e("AdapterBill", "Failed to create payment: " + t.getMessage());
                Toast.makeText(context, "Kết nối mạng thất bại: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvidBill, tvNameClient, tvTotal, tvTime, tvDay, tvPhone;
        LinearLayout linearLayout_item_product;
        ImageView img_drop_up;
        Button btn_updateStatusBill, btn_paymentBill;
        RecyclerView rvItemOrder;
        CardView card_bill;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvidBill = itemView.findViewById(R.id.tv_idBill_item);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvTotal = itemView.findViewById(R.id.tv_totalOrder_item);
            linearLayout_item_product = itemView.findViewById(R.id.linear_layout_item_product);
            img_drop_up = itemView.findViewById(R.id.img_drop_up);
            btn_updateStatusBill = itemView.findViewById(R.id.btn_updateStatusBill_item);
            btn_paymentBill = itemView.findViewById(R.id.btn_paymentBill_item);
            rvItemOrder = itemView.findViewById(R.id.rv_order);
            card_bill = itemView.findViewById(R.id.card_bill);
            tvTime = itemView.findViewById(R.id.tv_time_item);
            tvDay = itemView.findViewById(R.id.tv_day_item);
            tvNameClient = itemView.findViewById(R.id.tv_name_client_item);
        }
    }
}


