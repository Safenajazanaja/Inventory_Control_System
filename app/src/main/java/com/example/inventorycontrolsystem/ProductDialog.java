package com.example.inventorycontrolsystem;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inventorycontrolsystem.models.Product;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDialog extends DialogFragment {


    private TextView mIvImage;
    private TextView mTvProductName;
    private TextView mTVprice;
    private TextView mTvQty;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        Product product=getArguments().getParcelable("product");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_product, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("สินค้า");

        mIvImage=(TextView)view.findViewById(R.id.tv_product_name);
        mTvProductName=(TextView)view.findViewById(R.id.tv_product_name);
        mTVprice=(TextView)view.findViewById(R.id.tv_price);
        mTvQty=(TextView)view.findViewById(R.id.tv_qty);

        mTvProductName.setText(product.getProductName());
        mTVprice.setText(product.getPrice()+"");
        mTvQty.setText(product.getQty()+"");
        return builder.setView(view).create();
    }
}
