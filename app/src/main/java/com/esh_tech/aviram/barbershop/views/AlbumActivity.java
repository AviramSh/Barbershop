package com.esh_tech.aviram.barbershop.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.data.Picture;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

//    view
    ImageAdapter myImageAdapter;
    GridView gridview;



    ArrayList<Picture> pictures;
    ImageView tempCustomerPic;
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        init();

    }

    private void init() {
        dbHandler = new BarbershopDBHandler(this);

        myImageAdapter= new ImageAdapter(this);
        gridview = findViewById(R.id.gridview);
        gridview.setAdapter( myImageAdapter);

//        gridview.deferNotifyDataSetChanged();
        pictures = new ArrayList<>();
        pictures = dbHandler.getAllPictures();

        for (Picture index :
                pictures) {
            myImageAdapter.add(index.getBitmapImageData());
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AlbumActivity.this, "Item position : "+i, Toast.LENGTH_SHORT).show();
                final int picPosition = i;



                final AlertDialog.Builder alertadd = new AlertDialog.Builder(AlbumActivity.this);
                LayoutInflater factory = LayoutInflater.from(AlbumActivity.this);
                final View mView =factory.inflate(R.layout.custom_image_layout,null);

//        ArrayList<Picture> allCustomerPics = dbHandler.getAllPicturesByUserID(customerProfile.get_id());


                tempCustomerPic = (ImageView) mView .findViewById(R.id.dialogImageView);
                try {
                    tempCustomerPic.setImageBitmap(pictures.get(i).getBitmapImageData());
                } catch (Exception e) {
                    Toast.makeText(AlbumActivity.this, R.string.picture_not_exist, Toast.LENGTH_SHORT).show();
                }




                alertadd.setView(mView );
                alertadd.setPositiveButton(R.string.deleteCustomer, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(dbHandler.deletePictureById(pictures.get(picPosition).getId())){
                            Toast.makeText(AlbumActivity.this, "Delete Pic "+picPosition, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AlbumActivity.this, "Delete Pic "+picPosition +" Failed", Toast.LENGTH_SHORT).show();
                        }

                        init();
                    }
                });
                alertadd.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertadd.show();




            }
        });
    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private Bitmap btimaprecieve;
        private ArrayList<Bitmap> btAlbum;

        public ImageAdapter(Context c) {
            mContext = c;
            this.btAlbum =new ArrayList<>();
        }

        public int getCount() {
            return btAlbum.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, initialize some
                // attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(285, 285));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

//            imageView.setImageResource(mThumbIds[position]);
            imageView.setImageBitmap(btAlbum.get(position));

            return imageView;
        }

        private Integer[] mThumbIds = { R.drawable.usermale48,
                R.drawable.usermale48, R.drawable.usermale48,
                R.drawable.usermale48, R.drawable.usermale48, };

        public Bitmap add(Bitmap bitmap_recieve) {
            btAlbum.add(bitmap_recieve);
            return btimaprecieve;
        }
    }
}
