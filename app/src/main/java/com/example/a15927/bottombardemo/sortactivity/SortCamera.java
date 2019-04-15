package com.example.a15927.bottombardemo.sortactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.a15927.bottombardemo.R;

import static com.example.a15927.bottombardemo.R.id.arrow_back5;

public class SortCamera extends AppCompatActivity implements View.OnClickListener{

    private ImageView arrow_back4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sort_camera );

        arrow_back4 = (ImageView) findViewById( R.id.arrow_back4);
        arrow_back4.setOnClickListener( this );
    }

    @Override
    public void onClick(View view){
        finish();
    }
}
