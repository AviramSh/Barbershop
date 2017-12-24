package com.esh_tech.aviram.barbershop.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.Utils.MailUtils;
import com.esh_tech.aviram.barbershop.data.Product;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class StockActivity extends AppCompatActivity implements View.OnClickListener{

    EditText mProductName;
    EditText mProductQuantity;
    EditText mProductPrice;

    String reportEmail;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 145;

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

        final SharedPreferences settings;

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        reportEmail = settings.getString(UserDBConstants.USER_EMAIL,"");

        //        Database
        dbHandler = new BarbershopDBHandler(this);
        mProduct = new Product();
//        populateProducts();
        allProducts = dbHandler.getAllProducts();

        //        Connect list view
        lvProducts = findViewById(R.id.fillProductLv);
        createProduct = findViewById(R.id.ibAddProduct);
        createProduct.setOnClickListener(this);
        //        Connect adapter with custom view
        productsAdapter = new MyProductsAdapter(this,R.layout.custom_product_list_row,allProducts);

        lvProducts.setAdapter(productsAdapter);

//        TODO Fix long click and buttons
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
//                        Toast.makeText(StockActivity.this,
//                                name + getResources().getString(R.string.saved)+"Price"+price, Toast.LENGTH_LONG).show();
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

            default:
                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();
                break;
        }
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
                        (TextView)convertView.findViewById(R.id.tv_product_quantity),
                        (TextView)convertView.findViewById(R.id.tv_product_price),
                                (ImageButton)convertView.findViewById(R.id.btn_product_list_plus),
                                (ImageButton)convertView.findViewById(R.id.btn_product_list_minus),
                                (ImageButton)convertView.findViewById(R.id.btn_product_list_trash));

                viewHolder.title.setText(getItem(position).getName());
                viewHolder.quantity.setText(String.valueOf(getItem(position).getQuantity()));
                if(getItem(position).getQuantity() <0)
                    viewHolder.quantity.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                viewHolder.price.setText(String.valueOf(getItem(position).getPrice()));

                convertView.setTag(viewHolder);

            }else{

                mainViewHolder = (ViewHolder)convertView.getTag();
//                Toast.makeText(StockActivity.this, "Name :"+getItem(position).getName(), Toast.LENGTH_SHORT).show();

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
            ImageButton trash;

            public ViewHolder(Product product, TextView title, TextView quantity, TextView price, ImageButton plus, ImageButton minus,ImageButton trash) {
                this.product = product;
                this.title = title;
                this.quantity = quantity;
                this.price = price;
                this.plus = plus;
                this.minus = minus;
                this.trash = trash;



                this.title.setOnLongClickListener(myOnItemLongClickListener);
                this.price.setOnLongClickListener(myOnItemLongClickListener);
                this.quantity.setOnLongClickListener(myOnItemLongClickListener);


                this.plus.setOnClickListener(mMyLocalClickListener);
                this.minus.setOnClickListener(mMyLocalClickListener);
                this.trash.setOnClickListener(mMyLocalClickListener);
            }

            private View.OnLongClickListener myOnItemLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    switch (view.getId()){
                        case R.id.tv_product_name:
                            changeValues(R.id.tv_product_name,product);
                            break;
                        case R.id.tv_product_quantity:
                            changeValues(R.id.tv_product_quantity,product);
                            break;
                        case R.id.tv_product_price:
                            changeValues(R.id.tv_product_price,product);
                            break;
                        default:
                            Toast.makeText(StockActivity.this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();

                            break;
                    }
                    return true;
                }
            };
//            private AdapterView.OnItemLongClickListener myOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Toast.makeText(StockActivity.this, "AAAAA", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            };

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

                        case R.id.btn_product_list_trash:
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            if(dbHandler.deleteProductById(product.get_id())) {
                                                Toast.makeText(StockActivity.this, R.string.product_deleted, Toast.LENGTH_SHORT).show();
                                                setProductView();
                                            }else{
                                                Toast.makeText(StockActivity.this, R.string.filed_to_delete, Toast.LENGTH_SHORT).show();
                                            }
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(StockActivity.this);
                            builder.setMessage(getResources().getString(
                                    R.string.are_you_sure_you_whant_to_delete)+"?").setPositiveButton(
                                            getResources().getString(R.string.yes), dialogClickListener)
                                    .setNegativeButton(
                                            getResources().getString(R.string.no), dialogClickListener).show();
                            break;
                        default:
                            Toast.makeText(StockActivity.this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();

                            break;
                    }

                }
            };
        }
    }

//    TODO TEST Y ADAPTER NOT SHOWING DOUBLE VALUE IN EDIT TEXT
    private void changeValues(final int id,final Product product) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setView(input);




//        result = number1/number2
//        String stringdouble= Double.toString(result);
//        textview1.setText(stringdouble));
//
//        or you can use the NumberFormat:
//
//        Double result = number1/number2;
//        NumberFormat nm = NumberFormat.getNumberInstance();
//        textview1.setText(nm.format(result));
//
//        To force for 3 units precision:
//
//        private static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");
//        textview1.setText(REAL_FORMATTER.format(result));



            switch (id){
            case R.id.tv_product_name:
                input.setText(product.getName());
                builder.setTitle(getResources().getString(R.string.productName));
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.tv_product_quantity:
                input.setText(String.valueOf(product.getQuantity()));
                builder.setTitle(getResources().getString(R.string.quantity));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case R.id.tv_product_price:
                input.setText(String.valueOf(product.getPrice()));
                builder.setTitle(getResources().getString(R.string.price));
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                break;
            default:
                Toast.makeText(StockActivity.this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();

                break;
        }

//        input.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (id){
                    case R.id.tv_product_name:
                        product.setName(input.getText().toString());
                        dbHandler.upDateProduct(product);
                        setProductView();
                        break;
                    case R.id.tv_product_quantity:
                        product.setQuantity(Integer.parseInt(input.getText().toString()));
                        dbHandler.upDateProduct(product);
                        setProductView();
                        break;
                    case R.id.tv_product_price:
                        product.setPrice(Double.parseDouble(input.getText().toString()));
                        Toast.makeText(StockActivity.this, "Product Price is : "+product.getPrice(), Toast.LENGTH_SHORT).show();
                        if(dbHandler.upDateProduct(product)){
                            setProductView();
                        }else{
                            Toast.makeText(StockActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        Toast.makeText(StockActivity.this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    public void createReport(View view) {
        if(!reportEmail.equals("")) {
            if (allProducts.size() > 0) {
                new StockActivity.GenerateReportTask(allProducts).execute();
            } else {
                Toast.makeText(StockActivity.this, R.string.product_list_empty, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(StockActivity.this, R.string.report_filed, Toast.LENGTH_LONG).show();
        }

    }
    @SuppressLint("StaticFieldLeak")
    class GenerateReportTask extends AsyncTask<Void, Void, Boolean> {

        ArrayList<Product> productsList;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(StockActivity.this);
            pd.show();
        }

        public GenerateReportTask(ArrayList<Product> productList) {
            this.productsList = productList;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            if (result)
                Toast.makeText(StockActivity.this, R.string.report_generated, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(StockActivity.this, R.string.report_failure, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (ContextCompat.checkSelfPermission(StockActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(StockActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_REQUEST);
            } else {

                File folder = new File(Environment.getExternalStorageDirectory() + "/Barbershop");

                boolean var = false;
                if (!folder.exists())
                    var = folder.mkdir();


                final String filename = folder.toString() + "/" + "Report1.csv";

                try {

                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.write('\ufeff');
                    writer.println(" ID , Name , Quantity , Price");

                    double calc = 0;

                    for (Product product : productsList) {

                        writer.println(
                                product.get_id() + "," +
                                        product.getName() + "," +
                                        product.getQuantity() + "," +
                                        product.getPrice());
                    }
//                    writer.println(
//                            "Total" + "," +
//                                    "" + "," +
//                                    "" + "," +
//                                    "" + "," +
//                                    "" + "," +
//                                    calc);

                    writer.close();

                    MailUtils.sendMail(StockActivity.this, reportEmail, filename);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            return true;

        }
    }
}







