package com.example.inventorycontrolsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.example.inventorycontrolsystem.models.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button mBtIn;
    private ArrayList<Product> items;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ConnectDB.getconnection() == null) {
            Dru.failed(getBaseContext());
        } else {
            Dru.completed(getBaseContext());
        }
        //tool bar
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar) ;
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //bint widetd
        mBtIn = (Button) findViewById(R.id.bt_in);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));//2 row

        mBtIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menu_main){
            startActivity(new Intent(getBaseContext(),ProductActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProduct();
    }

    private void fetchProduct() {
        String sql = "SELECT p.*,t.type_name FROM product p INNER JOIN type t ON p.type_id=t.type_id";
        Dru.connection(ConnectDB.getconnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            items = new ArrayList<Product>();
                            while (resultSet.next()) {
                                Product product = new Product(
                                        resultSet.getString(1),
                                        resultSet.getString(2),
                                        resultSet.getDouble(3),
                                        resultSet.getInt(4),
                                        resultSet.getString(5),
                                        resultSet.getString(6),
                                        resultSet.getString(7)
                                );
                                items.add(product);

                                Log.d(TAG, "onComplete: " + items);

                            }
                            mRecyclerView.setAdapter(new ProductAdapter());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            Product product = items.get(position);
            holder.tvName.setText(product.getProductName());
            holder.tvPrice.setText(product.getPrice() + "");
            holder.tvQty.setText(product.getQty() + "");
            Dru.loadImageCircle(holder.ivImage, ConnectDB.BASE_IMAGE + product.getImage());
            Log.d(TAG, "onBindViewHolder: " + ConnectDB.BASE_IMAGE + product.getImage());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private final TextView tvName;
        private final TextView tvPrice;
        private final TextView tvQty;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_product);
            tvQty = (TextView) itemView.findViewById(R.id.tv_qty);
        }
    }
}
