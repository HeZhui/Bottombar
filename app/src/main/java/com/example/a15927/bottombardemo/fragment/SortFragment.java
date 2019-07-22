package com.example.a15927.bottombardemo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.a15927.bottombardemo.R;
import com.example.a15927.bottombardemo.sortactivity.Sort;

public class SortFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout electric;
    private RelativeLayout dayLife;
    private RelativeLayout clothes;
    private RelativeLayout sports;
    private RelativeLayout book;
    private RelativeLayout qiTa;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        electric = (RelativeLayout)view.findViewById( R.id.rc_electric );
        electric.setOnClickListener( this );

        dayLife = (RelativeLayout)view.findViewById( R.id.rc_dayLife );
        dayLife.setOnClickListener( this );

        clothes = (RelativeLayout)view.findViewById( R.id.rc_clothes );
        clothes.setOnClickListener( this );

        sports = (RelativeLayout)view.findViewById( R.id.rc_sports );
        sports.setOnClickListener( this );

        book = (RelativeLayout)view.findViewById( R.id.rc_book);
        book.setOnClickListener( this );

        qiTa = (RelativeLayout)view.findViewById( R.id.rc_qiTa );
        qiTa.setOnClickListener( this );

        return view;
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent( getActivity(), Sort.class );
        switch (view.getId()){
            case R.id.rc_electric:
                intent.putExtra( "type","electric" );
                break;
            case R.id.rc_dayLife:
                intent.putExtra( "type","dayLife" );
                break;
            case R.id.rc_clothes:
                intent.putExtra( "type","clothes" );
                break;
            case R.id.rc_sports:
                intent.putExtra( "type","sports" );
                break;
            case R.id.rc_book:
                intent.putExtra( "type","book" );
                break;
            case R.id.rc_qiTa:
                intent.putExtra( "type","camera" );
                break;
            default:
                break;
        }
        startActivity( intent );
    }
}