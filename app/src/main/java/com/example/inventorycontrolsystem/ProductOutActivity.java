package com.example.inventorycontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.example.inventorycontrolsystem.models.ProductIn;
import com.example.inventorycontrolsystem.models.ProductOut;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductOutActivity extends AppCompatActivity {

    private ArrayList<ProductOut> items;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_out);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProduct();
    }

    private void fetchProduct() {
        String sql = "SELECT p.product_name,p.image,t.type_name,pi.* FROM productin pi " +
                "INNER JOIN product p ON pi.ProductID=p.product_id " +
                "INNER JOIN type t on p.type_id=t.type_id";
        Dru.connection(ConnectDB.getconnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            items = new ArrayList<ProductOut>();
                            while (resultSet.next()) {
                                ProductOut product = new ProductOut(
                                        resultSet.getString("product_name"),
                                        resultSet.getString("image"),
                                        resultSet.getString("type_name"),
                                        resultSet.getString("ProductInNo"),
                                        resultSet.getString("ProductID"),
                                        resultSet.getString("DateIn"),
                                        resultSet.getInt("Quantity"),
                                        resultSet.getDouble("Price")
                                );
                                items.add(product);

                            }
                            mRecyclerView.setAdapter(new ProductOutActivity.ProductAdapter());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductOutActivity.ProductHolder> {
        @NonNull
        @Override
        public ProductOutActivity.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_in, parent, false);
            return new ProductOutActivity.ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductOutActivity.ProductHolder holder, int position) {
            ProductOut product = items.get(position);


            holder.tvProductName.setText(product.getProductName());
            holder.tvPrice.setText(String.format("%,.2f",product.getPrice()) + "");
            holder.tvProductNo.setText(product.getProductInNO());
            holder.tvQty.setText(String.format("%,.2f",(double)product.getQty()) + "หน่วย");
            holder.tvDateIn.setText(product.getDateIn());
            holder.tvTypeName.setText(product.getTypeName());
            Dru.loadImageCircle(holder.ivImage, ConnectDB.BASE_IMAGE + product.getImage());

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder {

        private final ImageView ivImage;
        private final TextView tvProductName;
        private final TextView tvPrice;
        private final TextView tvProductNo;
        private final TextView tvQty;
        private final TextView tvDateIn;
        private final TextView tvTypeName;
        private final Button btEdit;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvProductNo = (TextView) itemView.findViewById(R.id.tv_product_on);
            tvQty = (TextView) itemView.findViewById(R.id.tv_qty);
            tvDateIn = (TextView) itemView.findViewById(R.id.tv_date_in);
            tvTypeName = (TextView) itemView.findViewById(R.id.tv_type_name);
            btEdit = (Button) itemView.findViewById(R.id.bt_edit);


        }
    }


}





