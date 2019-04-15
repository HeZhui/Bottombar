package com.example.a15927.bottombardemo.sortactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.a15927.bottombardemo.R;

public class SortMakeup extends AppCompatActivity implements View.OnClickListener{

    private ImageView arrow_back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sort_makeup );

        arrow_back1 = (ImageView) findViewById( R.id.arrow_back1);
        arrow_back1.setOnClickListener( this );
    }

    @Override
    public void onClick(View view){
        finish();
    }
}
