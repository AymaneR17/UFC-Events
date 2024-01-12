package com.example.ufcproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    Button btnLogout;
    TextView txtUserEmail;

    TextView lblBettingScore;
    TextView txtName;
    DatabaseReference databaseReference;

    int bettingScore;
    String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogout = view.findViewById(R.id.logout);
        txtUserEmail = view.findViewById(R.id.lblEmail);
        lblBettingScore = view.findViewById(R.id.lblBettingScore);
        txtName = view.findViewById(R.id.txtName);

        auth= FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        txtUserEmail.setText(user.getEmail());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("email").equalTo(currentUser.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()) {
                        //recuperer le betting score provenant de la DB
                        Object data = dataMap.get(key);
                        HashMap<String, Object> userScore = (HashMap<String, Object>) data;
                        bettingScore = Integer.parseInt(userScore.get("bettingScore").toString());
                        name = userScore.get("name").toString();
                    }
                }
                lblBettingScore.setText(bettingScore + " point(s)");
                txtName.setText(name);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
            }
        });

        //deconnexion
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                getActivity().getFragmentManager().popBackStack();
            }
        });
        return view;
    }
}