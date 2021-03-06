package com.sahni.rahul.ieee_niec.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.adapter.SearchUserAdapter;
import com.sahni.rahul.ieee_niec.custom.MyRecyclerDivider;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.interfaces.OnRecyclerViewItemClickListener;
import com.sahni.rahul.ieee_niec.interfaces.OnSearchUserFragmentInteractionListener;
import com.sahni.rahul.ieee_niec.interfaces.OnSharedElementClickListener;
import com.sahni.rahul.ieee_niec.interfaces.UpdateSearchFragmentDetails;
import com.sahni.rahul.ieee_niec.models.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByNameOrInterestFragment extends Fragment implements UpdateSearchFragmentDetails, OnRecyclerViewItemClickListener, OnSharedElementClickListener {

    public static final String TAG = "SearchByNameOrInterest";
    private String mSearchBy;

    private RecyclerView mSearchUserRecyclerView;
    private SearchUserAdapter mSearchUserAdapter;
    private ArrayList<User> mUserArrayList;
    private ProgressBar mProgressbar;
    private TextView mHintTextView;
    private CardView cardView;

    private CollectionReference mUserCollection;

    private OnSearchUserFragmentInteractionListener mListener;

    public SearchByNameOrInterestFragment() {
        // Required empty public constructor
    }

    public static SearchByNameOrInterestFragment newInstance(String searchBy) {

        Bundle args = new Bundle();
        args.putString(ContentUtils.SEARCH_BY, searchBy);
        SearchByNameOrInterestFragment fragment = new SearchByNameOrInterestFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_by_name_or_interest, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchBy = getArguments().getString(ContentUtils.SEARCH_BY);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserCollection = FirebaseFirestore.getInstance().collection("users");

        mHintTextView = view.findViewById(R.id.search_user_hint_text_view);
        mProgressbar = view.findViewById(R.id.search_user_progress_bar);
        cardView = view.findViewById(R.id.user_card_view);
        cardView.setVisibility(View.INVISIBLE);
        mProgressbar.setVisibility(View.INVISIBLE);
        if (mSearchBy.equals(ContentUtils.SEARCH_BY_NAME)) {
            mHintTextView.setText("Search users by name");
        } else {
            mHintTextView.setText("Search users by interest");
        }
        mUserArrayList = new ArrayList<>();
        mSearchUserRecyclerView = view.findViewById(R.id.search_user_recycler_view);
        mSearchUserAdapter = new SearchUserAdapter(getContext(), mUserArrayList, this);
        mSearchUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mSearchUserRecyclerView.addItemDecoration(new MyRecyclerDivider(getActivity(), DividerItemDecoration.VERTICAL));
        mSearchUserRecyclerView.setAdapter(mSearchUserAdapter);

    }

    @Override
    public void update(final String query) {
        mUserArrayList.clear();
        mSearchUserAdapter.notifyDataSetChanged();
        cardView.setVisibility(View.INVISIBLE);
        mProgressbar.setVisibility(View.VISIBLE);
        mHintTextView.setVisibility(View.INVISIBLE);

        if (mSearchBy.equals(ContentUtils.SEARCH_BY_INTEREST)) {
            mUserCollection.whereEqualTo("interestMap." + query, true)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            mHintTextView.setVisibility(View.VISIBLE);
                            if (documentSnapshots.isEmpty()) {
                                mHintTextView.setText("No user found");
                            } else {
                                mHintTextView.setVisibility(View.INVISIBLE);
                                cardView.setVisibility(View.VISIBLE);
                                for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                                    User user = document.toObject(User.class);
                                    mUserArrayList.add(user);
                                }
                                mSearchUserAdapter.notifyDataSetChanged();


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            cardView.setVisibility(View.INVISIBLE);
                            mProgressbar.setVisibility(View.INVISIBLE);
                            mHintTextView.setVisibility(View.VISIBLE);
                            mHintTextView.setText("Oops! , there was a problem from our side. Please try again later.");
                        }
                    });
        } else {
            mUserCollection.whereGreaterThanOrEqualTo("name", query)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            mHintTextView.setVisibility(View.VISIBLE);
                            if (documentSnapshots.isEmpty()) {
                                mHintTextView.setText("No user found");
                            } else {
                                mHintTextView.setVisibility(View.INVISIBLE);
                                for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                                    User user = document.toObject(User.class);
                                    if (user.getName().contains(query)) {
                                        mUserArrayList.add(user);
                                    }
                                }
                                if (mUserArrayList.isEmpty()) {
                                    cardView.setVisibility(View.INVISIBLE);
                                    mHintTextView.setVisibility(View.VISIBLE);
                                    mHintTextView.setText("No user found");
                                } else {
                                    cardView.setVisibility(View.VISIBLE);
                                    mSearchUserAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            cardView.setVisibility(View.INVISIBLE);
                            mProgressbar.setVisibility(View.INVISIBLE);
                            mHintTextView.setVisibility(View.VISIBLE);
                            mHintTextView.setText("Oops! , there was a problem from our side. Please try again later.");
                        }
                    });

        }
    }

    @Override
    public void onItemClicked(View view) {
        int position = mSearchUserRecyclerView.getChildAdapterPosition(view);
        User user = mUserArrayList.get(position);
        if(mListener != null){
            mListener.onSearchUserFragmentInteraction(user, null);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSearchUserFragmentInteractionListener){
            mListener = (OnSearchUserFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement" +
                    "OnSearchUserFragmentInteractionListener");
        }
    }

    @Override
    public void onSharedElementClicked(View view, View sharedView) {
        int position = mSearchUserRecyclerView.getChildAdapterPosition(view);
        User user = mUserArrayList.get(position);
        if(mListener != null){
            mListener.onSearchUserFragmentInteraction(user, sharedView);
        }
    }
}
