package com.esh_tech.aviram.barbershop.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.data.Product;

import java.util.ArrayList;
import java.util.List;


public class StockActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    EditText mProductName;
    EditText mProductQuantity;
    EditText mProductPrice;

    View mView;
    Product mProduct;
    AlertDialog.Builder mBuilder;

    ImageButton createProduct;

    ImageButton mPlus;
    ImageButton mMinus;


    //    Database
    BarbershopDBHandler dbHandler;

    ArrayList<Product> allProducts = new ArrayList<>();
    ListView lvProducts;
    MyProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        init();

    }

    private void init() {

        //        Database
        dbHandler = new BarbershopDBHandler(this);
        mProduct = new Product();
//        populateProducts();
        allProducts = dbHandler.getAllProducts();

        //        Connect list view
        lvProducts =(ListView)findViewById(R.id.fillProductLv);
        createProduct = (ImageButton)findViewById(R.id.ibAddProduct);
        createProduct.setOnClickListener(this);
        //        Connect adapter with custom view
        productsAdapter = new MyProductsAdapter(this,R.layout.custom_product_list_row,allProducts);
        lvProducts.setAdapter(productsAdapter);

////        TODO Fix long click and buttons
//        lvProducts.setOnItemLongClickListener(
//                new AdapterView.OnItemLongClickListener(){
//
//                    @Override
//                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(StockActivity.this, "Long Click", Toast.LENGTH_SHORT).show();
//
////                        Toast.makeText(StockActivity.this, "1 On Long Click", Toast.LENGTH_LONG).show();
////                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(StockActivity.this);
////                        View mView = getLayoutInflater().inflate(R.layout.dialog_addproduct,null);
////
////                        mProductName = (EditText)mView.findViewById(R.id.etProductName);
////                        mProductQuantity = (EditText)mView.findViewById(R.id.etProductQuantity);
////                        mProductPrice = (EditText)mView.findViewById(R.id.etProductPrice);
////
////
////                        Toast.makeText(StockActivity.this, allProducts.get(position).getName()+"", Toast.LENGTH_SHORT).show();
////                        mProduct = dbHandler.getProductByName(allProducts.get(position).getName());
////
////                        if(mProduct != null) {
////                            Toast.makeText(StockActivity.this, "My pos :"+position, Toast.LENGTH_SHORT).show();
////
////                            mProductName.setText(mProduct.getName());
////                            mProductQuantity.setText(String.valueOf(mProduct.getQuantity()));
////                            mProductPrice.setText(String.valueOf(mProduct.getPrice()));
////                        }else{
////                            Toast.makeText(StockActivity.this, "Filed to import", Toast.LENGTH_SHORT).show();
////                        }
////
////
////
////                        mBuilder.setNeutralButton(R.string.saveBt, new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////
////                                String name = mProductName.getText().toString();
////                                String price = mProductPrice.getText().toString();
////                                String quantity = mProductQuantity.getText().toString();
////
////                                if(name.isEmpty()|| price.isEmpty()|| quantity.isEmpty()){
////                                    Toast.makeText(StockActivity.this, R.string.fields_are_not_full, Toast.LENGTH_LONG).show();
////                                }else{
////
////                                    mProduct.setName(name);
////                                    mProduct.setQuantity(Integer.parseInt(quantity));
////                                    mProduct.setPrice(Double.parseDouble(price));
////
////                                    if(dbHandler.upDateProduct(mProduct)){
////                                        Toast.makeText(StockActivity.this, name + R.string.saved, Toast.LENGTH_LONG).show();
////                                        setProductView();
////                                    }else Toast.makeText(StockActivity.this,  R.string.failedToSave +" "+name, Toast.LENGTH_LONG).show();
////
////                                }
////                            }
////                        });
////
////
////                        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////
////                                Toast.makeText(StockActivity.this, R.string.cancel, Toast.LENGTH_LONG).show();
////                            }
////                        });
////
////
////                        mBuilder.setView(mView);
////                        AlertDialog dialog = mBuilder.create();
////                        dialog.show();
//
//                        return true;
//                    }
//                }
//        );


    }
    

//    private void populateProducts() {
//
//        allProducts.add(new Product(1, "מסרק", 14, 5));
//        allProducts.add(new Product(1, "מסרק", 14, 5));
//        allProducts.add(new Product(1, "מסרק", 14, 5));
//        allProducts.add(new Product(1, "מסרק", 14, 5));
////        allProducts.add(new Appointment.Product(1, "מסרק", 14, 5));
////        allProducts.add(new Appointment.Product(1, "מסרק", 14, 5));
//
//    }

    public void addProduct() {

        mBuilder = new AlertDialog.Builder(this);
        mView =getLayoutInflater().inflate(R.layout.dialog_addproduct,null);

        mProductName = (EditText)mView.findViewById(R.id.etProductName);
        mProductQuantity = (EditText)mView.findViewById(R.id.etProductQuantity);
        mProductPrice = (EditText)mView.findViewById(R.id.etProductPrice);

      /*  ImageButton plus = (ImageButton)mView.findViewById(R.id.btPlus);
        ImageButton minus = (ImageButton)mView.findViewById(R.id.btMinus);

        plus.setOnClickListener(this);
        minus.setOnClickListener(this);*/



        /*Button mSave = (Button)mView.findViewById(R.id.bSave);
        Button mCancel = (Button)mView.findViewById(R.id.bCancel);*/



        mBuilder.setNeutralButton(R.string.saveBt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = mProductName.getText().toString();
                String price = mProductPrice.getText().toString();
                String quantity = mProductQuantity.getText().toString();

                if(name.isEmpty()|| price.isEmpty()|| quantity.isEmpty()){
                    Toast.makeText(StockActivity.this, R.string.fields_are_not_full, Toast.LENGTH_LONG).show();
                }else{
                    if(dbHandler.addProduct(new Product(name , Integer.parseInt(quantity) ,Double.parseDouble(price)))){
                        Toast.makeText(StockActivity.this, name + R.string.saved, Toast.LENGTH_LONG).show();
                        setProductView();
                    }else Toast.makeText(StockActivity.this,  R.string.failedToSave +" "+name, Toast.LENGTH_LONG).show();

                }


            }
        });


        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(StockActivity.this, R.string.cancel, Toast.LENGTH_LONG).show();
                    }
                });


        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void setProductView() {

        allProducts = dbHandler.getAllProducts();
        productsAdapter = new MyProductsAdapter(StockActivity.this,R.layout.custom_product_list_row,allProducts);
        lvProducts.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();
        

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ibAddProduct:
                addProduct();
                break;

            case R.id.ibSave:
                this.finish();
                break;

            case R.id.btPlus:

                break;
            case R.id.btMinus:

                break;
            default:
                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {

        Toast.makeText(this, "Yesss", Toast.LENGTH_SHORT).show();
        return true;
    }
//
///*
//    @Override
//    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(StockActivity.this, "@@@@@@", Toast.LENGTH_SHORT).show();
//
//        switch (view.getId()){
//            case R.id.btPlus:
//                Toast.makeText(this, "p", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.btMinus:
//                Toast.makeText(this, "m", Toast.LENGTH_SHORT).show();
//                break;
//
//            default:
//                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(StockActivity.this, "@@@@@@", Toast.LENGTH_SHORT).show();
//        switch (view.getId()){
//            case R.id.btPlus:
//                Toast.makeText(this, "p", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.btMinus:
//                Toast.makeText(this, "m", Toast.LENGTH_SHORT).show();
//                break;
//
//            default:
//                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }*/


    //Creating custom Adapter for the list view GUI
    class MyProductsAdapter extends ArrayAdapter<Product> {
        private int layout;
        public MyProductsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder mainViewHolder = null;

            if (convertView == null){
                Log.e("Test get view","inside if with position"+position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_product_list_row,parent,false);

                ViewHolder viewHolder = new ViewHolder(getItem(position),
                        (TextView)convertView.findViewById(R.id.tv_product_name),
                        (TextView)convertView.findViewById(R.id.tv_product_price),
                        (TextView)convertView.findViewById(R.id.tv_product_quantity),
                                (ImageButton)convertView.findViewById(R.id.btn_product_list_plus),
                                (ImageButton)convertView.findViewById(R.id.btn_product_list_minus));
                viewHolder.title.setText(getItem(position).getName());
                viewHolder.quantity.setText(String.valueOf(getItem(position).getQuantity()));
                viewHolder.price.setText(String.valueOf(getItem(position).getPrice()));

//                viewHolder.product = getItem(position);
//                viewHolder.title = (TextView)convertView.findViewById(R.id.tv_product_name);
//                viewHolder.price = (TextView)convertView.findViewById(R.id.tv_product_price);
//                viewHolder.quantity = (TextView)convertView.findViewById(R.id.tv_product_quantity);
//                viewHolder.plus = (ImageButton)convertView.findViewById(R.id.btn_product_list_plus);
//                viewHolder.minus = (ImageButton)convertView.findViewById(R.id.btn_product_list_minus);

                convertView.setTag(viewHolder);

            }else{

                mainViewHolder = (ViewHolder)convertView.getTag();
                Toast.makeText(StockActivity.this, "Name :"+getItem(position).getName(), Toast.LENGTH_SHORT).show();
                mainViewHolder.title.setText(getItem(position).getName());
                mainViewHolder.quantity.setText(String.valueOf(getItem(position).getQuantity()));
                mainViewHolder.price.setText(String.valueOf(getItem(position).getPrice()));
            }
//            convertView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    Toast.makeText(StockActivity.this, mProduct.getName()+"AAAAA", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            });

//
//            TextView name = (TextView) convertView.findViewById(R.id.tvProductName);
//            TextView price = (TextView) convertView.findViewById(R.id.tvProductPrice);
//            TextView quantity = (TextView)convertView.findViewById(R.id.tvProductQuantity);
//            ImageButton plus = (ImageButton)convertView.findViewById(R.id.btPlus);
//            ImageButton minus = (ImageButton)convertView.findViewById(R.id.btMinus);
//
//            //Data
//            name.setText(mProduct.getName());
//            price.setText(String.valueOf(mProduct.getPrice()));
//            quantity.setText(String.valueOf(mProduct.getQuantity()));
            
//            name.setTag(position);
//            minus.setTag(position);
//            plus.setTag(position);

//
//            plus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int i = mProduct.getQuantity()+1;
//                    mProduct.setQuantity(i);
//                    dbHandler.upDateProduct(mProduct);
//
//                    allProducts =dbHandler.getAllProducts();
//                    lvProducts.setAdapter(productsAdapter);
//                    lvProducts.deferNotifyDataSetChanged();
//                }
//            });
//
//            minus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int i = mProduct.getQuantity()-1;
//                    mProduct.setQuantity(i);
//                    dbHandler.upDateProduct(mProduct);
//
//                    allProducts =dbHandler.getAllProducts();
//                    lvProducts.setAdapter(productsAdapter);
//                    lvProducts.deferNotifyDataSetChanged();
//                }
//            });
//            name.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//
//                    return true;
//                }
//            });
//
//            plus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position  = (Integer) view.getTag();
//
//                    Product p = allProducts.get(position);
//                    int q = p.getQuantity();
//                    p.setQuantity(q-1);
//
//                    allProducts.set(position,p);
//                    dbHandler.upDateProduct(p);
//
//                    setProductView();
//                }
//            });
//
//            minus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position  = (Integer) view.getTag();
//                    /*Product product = getItem(position);
//                    Toast.makeText(StockActivity.this, ""+position, Toast.LENGTH_SHORT).show();
//
//                    int q = getItem(position).getQuantity();
//                    getItem(position).setQuantity(q+1);*/
//
//                    Product p = allProducts.get(position);
//                    int q = p.getQuantity();
//                    p.setQuantity(q+1);
//
//                    allProducts.set(position,p);
//                    dbHandler.upDateProduct(p);
//
//
//                    setProductView();
//
//                }
//            });


            return convertView;
        }


        private class ViewHolder{
            Product product;
            TextView title;
            TextView quantity;
            TextView price;
            ImageButton plus;
            ImageButton minus;

            public ViewHolder(Product product, TextView title, TextView quantity, TextView price, ImageButton plus, ImageButton minus) {
                this.product = product;
                this.title = title;
                this.quantity = quantity;
                this.price = price;
                this.plus = plus;
                this.minus = minus;
                this.plus.setOnClickListener(mMyLocalClickListener);
                this.minus.setOnClickListener(mMyLocalClickListener);
            }

            private View.OnClickListener mMyLocalClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (view.getId()){
                        case R.id.btn_product_list_plus:

                            product.setQuantity(product.getQuantity()+1);
                            dbHandler.upDateProduct(product);
                            setProductView();
                            break;
                        case R.id.btn_product_list_minus:

                            product.setQuantity(product.getQuantity()-1);
                            dbHandler.upDateProduct(product);
                            setProductView();
                            break;
                    }

                }
            };
        }
    }
}







