package com.example.inventorycontrolsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.adedom.library.ExecuteUpdate;
import com.example.inventorycontrolsystem.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private ArrayList<Product> items;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar) ;
        toolbar.setTitle("Product backend");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),InsertProductActivity.class));
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menagerment_product, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            Product product = items.get(position);
            holder.tvProductName.setText(product.getProductName());
            holder.tvPrice.setText(String.format("%,.2f", product.getPrice()) + " บาท");
            holder.tvProductId.setText(product.getProductId());
            holder.tvQty.setText(String.format("%,.0f", (double) product.getQty()) + " หน่วย");
            holder.tvTypeId.setText(product.getTypeId());
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
        private final TextView tvProductId;
        private final TextView tvQty;
        private final TextView tvTypeId;
        private final TextView tvTypeName;
        private final Button btEdit;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            tvProductName = (TextView) itemView.findViewById(R.id.tv_product_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvProductId = (TextView) itemView.findViewById(R.id.tv_product_id);
            tvQty = (TextView) itemView.findViewById(R.id.tv_qty);
            tvTypeId = (TextView) itemView.findViewById(R.id.tv_type_id);
            tvTypeName = (TextView) itemView.findViewById(R.id.tv_type_name);
            btEdit = (Button) itemView.findViewById(R.id.bt_edit);

            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product=items.get(getAdapterPosition());
                    Intent intent=new Intent(getBaseContext(),EditProductActivity.class);
                    intent.putExtra("product",product);
//                    intent.putExtra("product_id",product.getProductId());
//                    intent.putExtra("product_name",product.getProductName());
//                    intent.putExtra("price",product.getPrice());
//                    intent.putExtra("qty",product.getQty());
//                    intent.putExtra("image",product.getImage());
//                    intent.putExtra("type_id",product.getTypeId());
//                    intent.putExtra("type_name",product.getTypeName());
                    startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Product product= items.get(getAdapterPosition());
                    dialogMessages(product);
                    return false;
                }
            });

        }

        private void dialogMessages(final Product product) {
            AlertDialog.Builder builder=new AlertDialog.Builder(ProductActivity.this);
            builder.setTitle("Delete product");
            builder.setMessage("Do you want delete product"+product.getProductName()+" !!");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    deleteProduct(product.getProductId());
                }
            });
            builder.show();

        }

        private void deleteProduct(String productId) {
            Toast.makeText(getBaseContext(),"Delete success",Toast.LENGTH_SHORT).show();


            String sql = "DELETE FROM `product` WHERE product_id='"+productId+"'";
                    Dru.connection(ConnectDB.getconnection())
                            .execute(sql)
                            .commit(new ExecuteUpdate() {

                                public void onComplete() {
                                    fetchProduct();
                                }
                            });
        }
    }
}
