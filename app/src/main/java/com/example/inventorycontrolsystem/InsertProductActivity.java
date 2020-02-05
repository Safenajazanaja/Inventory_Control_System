package com.example.inventorycontrolsystem;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteUpdate;

import java.io.IOException;
import java.util.UUID;

public class InsertProductActivity extends AppCompatActivity {

    private EditText mEtProductId;
    private EditText mEtProductName;
    private EditText mEtPrice;
    private EditText mEtQty;
    private ImageView mIvImage;
    private Button mBtCancle;
    private Button mBtOk;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        mEtProductId = (EditText) findViewById(R.id.et_product_id);
        mEtProductName = (EditText) findViewById(R.id.et_product_name);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mEtQty = (EditText) findViewById(R.id.et_qty);
        mIvImage = (ImageView) findViewById(R.id.iv_image);
        mBtCancle = (Button) findViewById(R.id.bt_cancel);
        mBtOk = (Button) findViewById(R.id.bt_ok);

        mBtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertProduct();
            }
        });

        mIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dru.selectImage(InsertProductActivity.this, 1234);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mIvImage.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertProduct() {
        String productId = mEtProductId.getText().toString().trim();
        String productName = mEtProductName.getText().toString().trim();
        String price = mEtPrice.getText().toString().trim();
        String qty = mEtQty.getText().toString().trim();

        if (productId.isEmpty()) {
            mEtProductId.setError("กรุณากรอกรหัสสินค้า");
            mEtProductId.setFocusable(true);
            return;
        } else if (productName.isEmpty()) return;
        else if (price.isEmpty()) return;
        else if (qty.isEmpty()) return;

        String name = "empty";
        if (mBitmap != null) {
            name = UUID.randomUUID().toString().replace("-", "") + ".png";
            Dru.uploadImage(ConnectDB.BASE_IMAGE, name, mBitmap);
        }

        String sql = "INSERT INTO product VALUES ('" + productId + "','" + productName +
                "'," + price + "," + qty + ",'" + name + "','T0001')";
        Dru.connection(ConnectDB.getconnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getBaseContext(),"Insert success",Toast.LENGTH_SHORT).show();
                    }
                });

//        Dru.connection(ConnectDB.getConnection())
//                .execute(sql)
//                .commit(new ExecuteUpdate() {
//                    @Override
//                    public void onComplete() {
//                        Toast.makeText(getBaseContext(), "Insert success", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                });

    }
}
