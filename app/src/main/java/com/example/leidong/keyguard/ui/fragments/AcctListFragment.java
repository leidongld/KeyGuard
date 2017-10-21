package com.example.leidong.keyguard.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.adapters.AcctListAdapter;
import com.example.leidong.keyguard.listeners.OnCardsScrollListener;

/**
 * Created by leidong on 2017/10/15
 */

public class AcctListFragment extends Fragment{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CATEGORY = "category_param";

    private Long category;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private CardView placeHolder;
    private AcctListAdapter adapter;

    public AcctListFragment() {
    }

    /**
     *使用此工厂方法创建带有特定参数的新实例
     * @param
     * @return
     */
    // TODO: Rename and change types and number of parameters
    public static AcctListFragment newInstance(Long category) {
        AcctListFragment fragment = new AcctListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getLong(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_acct_list, container, false);
        recyclerView = (RecyclerView) ret.findViewById(R.id.acct_list_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AcctListAdapter(getContext(), recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.loadAccountsInCategory(category);
        recyclerView.addOnScrollListener(new OnCardsScrollListener());

        placeHolder = (CardView) ret.findViewById(R.id.placeholder);
        ViewCompat.setElevation(placeHolder, 10.0f);
        return ret;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.loadAccountsInCategory(category);
        //当前无条目时，RecyclerView不显示
        if (recyclerView.getAdapter().getItemCount() == 0) {
            placeHolder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        //当前有条目时，RecyclerView显示
        else {
            placeHolder.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        adapter.loadAccountsInCategory(category);
        if (recyclerView.getAdapter().getItemCount() == 0) {
            placeHolder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            placeHolder.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 此接口必须由包含此片段的Activity实现，
     * 以允许将此片段中的交互传达到该Activity中包含的Activity和潜在的其他片段。
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
