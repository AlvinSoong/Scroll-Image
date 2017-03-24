package com.example.soong.scrollimage;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.soong.scrollimage.myview.ImageBarnnerViewGroup;
import com.example.soong.scrollimage.myview.ImageListener;

public class MainActivity extends AppCompatActivity implements ImageListener{
    private ImageBarnnerViewGroup viewGroup;
    private int[] images = new int[]{
        R.drawable.img1,R.drawable.img2,R.drawable.img3
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int phone_width = dm.widthPixels;
        viewGroup = (ImageBarnnerViewGroup) findViewById(R.id.image_barnner);
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ActionBar.LayoutParams(phone_width, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageResource(images[i]);
            viewGroup.addView(imageView);
        }
        viewGroup.setListener(this);
    }

    @Override
    public void ImageTouch(int index) {
        Toast.makeText(this, "Pic"+index, Toast.LENGTH_SHORT).show();
    }
}
