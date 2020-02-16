package com.example.inventorycontrolsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.adedom.library.ExecuteUpdate;
import com.example.inventorycontrolsystem.models.Product;
import com.example.inventorycontrolsystem.models.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsertProductInActivity extends AppCompatActivity {

    private EditText mEtProductNo;
    private EditText mEtPrice;
    private EditText mEtQty;
    private Button mBtCancle;
    private Button mBtOk;
    private ArrayList<Product> items;
    private Spinner mESpinner;
    private Button mBtEdit;
    private String mTypeId;
    private String mProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product_in);

        mEtProductNo = (EditText) findViewById(R.id.et_product_no);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mEtQty = (EditText) findViewById(R.id.et_qty);
        mESpinner = (Spinner) findViewById(R.id.spinner);
        mBtCancle = (Button) findViewById(R.id.bt_cancel);
        mBtOk = (Button) findViewById(R.id.bt_ok);






        mBtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProduct();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinner();
    }

    private void setSpinner() {
        String sql = "SELECT product_id,product_name,image FROM product";
        Dru.connection(ConnectDB.getconnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            items = new ArrayList<Product>();
                            while (resultSet.next()) {
                                Product product=new Product();
                                product.setProductId(resultSet.getString("product_id"));
                                product.setProductName(resultSet.getString("product_name"));
                                product.setImage(resultSet.getString("image"));
                                items.add(product);
                            }
                            mESpinner.setAdapter(new ProductAdater(getBaseContext(), items));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

//        mESpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Type type = (Type) adapterView.getItemAtPosition(i);
//                Toast.makeText(getBaseContext(), type.getTypeName(), Toast.LENGTH_SHORT).show();
//                mTypeId = type.getTypeId();
//            }
//        });

        mESpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                Product product = (Product) parent.getItemAtPosition(i);
                mProductId = product.getProductId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void insertProduct() {

        String productInNo=mEtProductNo.getText().toString().trim();
        final String qty=mEtQty.getText().toString().trim();
        final String price=mEtPrice.getText().toString().trim();

        if (productInNo.isEmpty())return;
        else if (qty.isEmpty())return;
        else  if (price.isEmpty())return;;

        String sql = "INSERT INTO productin VALUES('"+productInNo+"','"+mProductId+"',CURRENT_DATE(),'"+qty+"','"+price+"')";
                Dru.connection(ConnectDB.getconnection())
                        .execute(sql)
                        .commit(new ExecuteUpdate() {
                            @Override
                            public void onComplete() {
//                                Toast.makeText(getBaseContext(), "Insert success", Toast.LENGTH_SHORT).show();
                                updateProduct(qty,price);

                            }
                        });
    }

    private void updateProduct(String qty, String price) {
        String sql = "UPDATE product SET qty=qty+"+qty+",price="+price+" WHERE product_id='"+mProductId+"'";
                Dru.connection(ConnectDB.getconnection())
                        .execute(sql)
                        .commit(new ExecuteUpdate() {
                            @Override
                            public void onComplete() {
                                finish();
                                Toast.makeText(getBaseContext(), "Insert success", Toast.LENGTH_SHORT).show();

                            }
                        });
    }

    private class ProductAdater extends ArrayAdapter<Product> {
        public ProductAdater(Context context, ArrayList<Product> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        private View initView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_product, parent, false);
            }
            ImageView imageView=(ImageView) convertView.findViewById(R.id.iv_image) ;
            TextView tvProductName=(TextView) convertView.findViewById(R.id.tv_product_name) ;

            Product product = getItem(position);
            Dru.loadImageCircle(imageView,ConnectDB.BASE_IMAGE+ product.getImage());
            tvProductName.setText(product.getProductName());

            return convertView;
        }

    }
}
