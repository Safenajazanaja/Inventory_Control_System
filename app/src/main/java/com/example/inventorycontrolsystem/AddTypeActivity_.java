package com.example.inventorycontrolsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.adedom.library.ExecuteUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AddTypeActivity_ extends AppCompatActivity {


    private Button mBtCancle;
    private Button mBtOk;
    private EditText mETypeId;
    private EditText mEtTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type_);

        mETypeId = (EditText) findViewById(R.id.et_type_id);
        mEtTypeName = (EditText) findViewById(R.id.et_type_name);
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
                insertType();
            }
        });

        mETypeId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String sql = "SELECT * FROM type WHERE type_id='"+charSequence+"'";
                        Dru.connection(ConnectDB.getconnection())
                                .execute(sql)
                                .commit(new ExecuteQuery() {
                                    @Override
                                    public void onComplete(ResultSet resultSet) {
                                        try {
                                            if (resultSet.next()) {
                                                mBtOk.setEnabled(false);

                                            }else {
                                                mBtOk.setEnabled(true);
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void insertType() {
        String typeId = mETypeId.getText().toString().trim();
        String typeName = mEtTypeName.getText().toString().trim();

        if (typeId.isEmpty()) return;
        else if (typeName.isEmpty()) return;

        String sql = "INSERT INTO type VALUES('" + typeId + "','" + typeName + "')";
        Dru.connection(ConnectDB.getconnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        finish();

                    }
                });
    }

}
