package com.cse.pec.foodapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link order_detail_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link order_detail_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class order_detail_fragment extends Fragment {
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;
    static  ArrayList<DataObject.order_detail> order_details;
    static DataObject data ;
    private OnFragmentInteractionListener mListener;

    public order_detail_fragment() {

    }


    public static order_detail_fragment newInstance(ArrayList<DataObject.order_detail> od) {
        order_detail_fragment fragment = new order_detail_fragment();
        order_details = od;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);










    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = ((MyApp)getActivity().getApplication()).data;

        RecyclerView recyclerView =  (RecyclerView) inflater.inflate(R.layout.fragment_order_detail_fragment, container, false).findViewById(R.id.order_detail_fragment_recycler);

         final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.scrollToPosition(0);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL));
        OrderAdapter dishAdapter = new OrderAdapter();
        recyclerView.setAdapter(dishAdapter);




        return inflater.inflate(R.layout.fragment_order_detail_fragment, container, false);
    }
    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {



        public  class MyViewHolder extends RecyclerView.ViewHolder  {
            TextView dish,units,cost;
            public MyViewHolder(View itemView) {
                super(itemView);
                dish = (TextView)itemView.findViewById(R.id.order_detail_dish);
                units = (TextView)itemView.findViewById(R.id.order_detail_units);
                cost = (TextView)itemView.findViewById(R.id.order_detail_cost);
            }
        }
        @Override
        public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_detail_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OrderAdapter.MyViewHolder holder, final int position) {
                holder.dish.setText(data.Dishes.get(order_details.get(position).dishID).name);
                 holder.units.setText(order_details.get(position).quantity+" Units");
                    holder.cost.setText("₹"+data.Dishes.get(order_details.get(position).dishID).price*order_details.get(position).quantity);
        }





        @Override
        public int getItemCount() {
            return order_details.size();
        }


    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }



}
