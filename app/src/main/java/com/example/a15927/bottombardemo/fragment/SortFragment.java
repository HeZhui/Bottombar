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
    private RelativeLayout make_up;
    private RelativeLayout clothes;
    private RelativeLayout sports;
    private RelativeLayout book;
    private RelativeLayout camera;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        electric = (RelativeLayout)view.findViewById( R.id.rc_electric );
        electric.setOnClickListener( this );

        make_up = (RelativeLayout)view.findViewById( R.id.rc_make_up );
        make_up.setOnClickListener( this );

        clothes = (RelativeLayout)view.findViewById( R.id.rc_clothes );
        clothes.setOnClickListener( this );

        sports = (RelativeLayout)view.findViewById( R.id.rc_sports );
        sports.setOnClickListener( this );

        book = (RelativeLayout)view.findViewById( R.id.rc_book);
        book.setOnClickListener( this );

        camera = (RelativeLayout)view.findViewById( R.id.rc_camera );
        camera.setOnClickListener( this );

        return view;
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent( getActivity(), Sort.class );
        switch (view.getId()){
            case R.id.rc_electric:
//                Intent intent_electric = new Intent(getActivity(),SortElectric.class);
//                startActivity( intent_electric );
                intent.putExtra( "type","electric" );
                break;
            case R.id.rc_make_up:
//                Intent intent_makeup = new Intent(getActivity(),SortMakeup.class);
//                startActivity( intent_makeup );
                intent.putExtra( "type","makeup" );
                break;
            case R.id.rc_clothes:
//                Intent intent_clothes = new Intent(getActivity(),SortClothes.class);
//                startActivity( intent_clothes );
                intent.putExtra( "type","clothes" );
                break;
            case R.id.rc_sports:
//                Intent intent_sports = new Intent(getActivity(),SortSports.class);
//                startActivity( intent_sports );
                intent.putExtra( "type","sports" );
                break;
            case R.id.rc_book:
//                Intent intent_book = new Intent(getActivity(),SortBook.class);
//                startActivity( intent_book );
                intent.putExtra( "type","book" );
                break;
            case R.id.rc_camera:
//                Intent intent_camera = new Intent(getActivity(),SortCamera.class);
//                startActivity( intent_camera );
                intent.putExtra( "type","camera" );
                break;
            default:
                break;
        }
        startActivity( intent );
    }
}