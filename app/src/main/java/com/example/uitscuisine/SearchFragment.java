package com.example.uitscuisine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.uitscuisine.Category_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    ArrayList<Category_item> items = new ArrayList<>();
    categoryRVAdapter adapter;
    DatabaseReference mref;
    RecyclerView recyclerView;
    ImageButton returnBack,reload;
    SearchView searchView;
    ArrayList<Post> list;
    MainActivity mainActivity;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mref = FirebaseDatabase.getInstance().getReference().child("Post");
        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv);
        searchView =(SearchView) rootView.findViewById(R.id.searchViewItem);
        returnBack = (ImageButton)rootView.findViewById(R.id.returnBack);
        mainActivity = (MainActivity) getActivity();
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animatoo.animateSlideLeft(mainActivity);
                Fragment selectedFragment = new FragmentHome();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fr = fragmentManager.beginTransaction();
                fr.replace(R.id.fragment_search,selectedFragment);
                fr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fr.addToBackStack(null);
                fr.commit();
            }
        });
        return rootView;
    }
    private void search(String str) {
        ArrayList<Post> mylist = new ArrayList<>();
        for (Post object : list) {
            if (object.getRecipeName().toLowerCase().contains(str.toLowerCase())) {
                mylist.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(mylist);
        recyclerView.setAdapter(adapterClass);
    }

    public void onStart() {
        super.onStart();
        if (mref != null) {
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            list.add(ds.getValue(Post.class));
                        }
                        AdapterClass adapterClass = new AdapterClass(list);
                        recyclerView.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return false;
                }
            });
        }
    }
}
