package com.example.ufcproject;

import static com.example.ufcproject.HomeFragment.NOTIFICATION_ID;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.ufcproject.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());


         binding.bottomNavigationView.setOnItemSelectedListener(item -> {
             switch (item.getItemId()){
                 case R.id.home:
                     replaceFragment(new HomeFragment());
                     break;
                 case R.id.profile:
                     replaceFragment(new ProfileFragment());
                     break;
                 case R.id.messages:
                     Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                     startActivity(intent);
                     break;
                 case R.id.search:
                     replaceFragment(new SearchFragment());
                     break;
             }
             return true;
         });

        auth= FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}