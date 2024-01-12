package com.example.ufcproject;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.ufcproject.databinding.ActivityMainBinding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    public static final String CHANNEL_ID = "Result notification";
    public static final int NOTIFICATION_ID = 001;
    public static View view;
    public static String fighterChoice;
    public static String fighterWinner;
    public static int bettingScore ;
    public static DatabaseReference databaseReference;
    public String eventUrl;
    public String apiEventKey = "c68ee32ea54d50cb051b584803ee166f75e99d87";
    public RecyclerView myRecyclerView;
    public RecyclerView myRecyclerViewEventNumber;
    public List<Event> eventList;
    public List<String> eventNumberList;
    public MyEventAdapter eventAdapter;
    public MyEventNumberAdapter eventNumberAdapter;
    public Bundle bundle;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Declaration de la vue au fragment home
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //Declaration des recyclerView
        myRecyclerView = view.findViewById(R.id.myRecyclerView);
        myRecyclerViewEventNumber = view.findViewById(R.id.myRecyclerViewEventNumber);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        //Declaration des listes
        eventList = new ArrayList<>();
        eventNumberList = new ArrayList<>();

        //Déclaration du bundle
        bundle = this.getArguments();
        //liaison des adapter
        eventAdapter = new MyEventAdapter(eventList, getContext());
        eventNumberAdapter = new MyEventNumberAdapter(eventNumberList, getContext());

        //recupere le bon event selon le string resorti du bundle
        if (bundle != null) {
            eventUrl = bundle.getString("event");
            requestForEvent();
        } else { requestForQuote();}
        return view;
    }

    public static void setFighterChoice(String choiceFighter, String winnerFighter) {
        //methode appelle dans l'adapter pour verifier le gagnant, changer les couleurs des boutons ainsi que les rendre incliquable
        fighterChoice = choiceFighter;
        fighterWinner = winnerFighter;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    //creation de la notification
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "personal notifications";
                    String description = "personal notifications description";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;

                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                                                                                    name,
                                                                                    importance);
                    notificationChannel.setDescription(description);

                    NotificationManager notificationManager = (NotificationManager) view.getContext().getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                Intent landingIntent = new Intent(view.getContext(), MainActivity.class);
                landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent landingPendingIntent = PendingIntent.getActivity(view.getContext(),
                                                                                0,
                                                                                landingIntent,
                                                                                PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.baseline_message_24)
                        .setContentTitle("Ton paris est finis")
                        .setContentText("Le gagnant est : " + fighterWinner + " et tu à parié sur " + fighterChoice)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(landingPendingIntent)
                        .setAutoCancel(true);
                //assignation des points selon la victoire ou la defaite du pari
                if ( fighterWinner.equalsIgnoreCase(fighterChoice)) {bettingScore=bettingScore+10;}
                else {bettingScore= bettingScore-10;}
                //appel des methode lecture et ecriture en DB
                getScoreFromDB();
                setScoreInDB();

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(view.getContext());
                if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

                MyEventAdapter.setWinner();

            }
        }, 3000);

    }
    public static void getScoreFromDB() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("email").equalTo(currentUser.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);
                        HashMap<String, Object> userScore = (HashMap<String, Object>) data;
                        bettingScore = Integer.parseInt(userScore.get("bettingScore").toString());
                    }
                }
            }
                @Override
                public void onCancelled (@NonNull DatabaseError error){
                }
            });
    }
    public static void setScoreInDB(){

        databaseReference =FirebaseDatabase.getInstance().getReference("users")
                                                                            .child(FirebaseAuth
                                                                         .getInstance()
                                                                         .getCurrentUser()
                                                                         .getUid());
        databaseReference.child("bettingScore").setValue(bettingScore);
    }
    public void requestForEvent(){
        String url = "https://fightingtomatoes.com/API/"+apiEventKey+"/Any/"+eventUrl+"/Any";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        //appel de la requete
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String myResponse = response.body().string();

                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ObjectMapper objectMapper = new ObjectMapper();

                                try {
                                    eventList = Arrays.asList(objectMapper.readValue(myResponse, Event[].class));

                                } catch (Exception i) {
                                    i.printStackTrace();
                                }
                                myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                                myRecyclerView.setAdapter(new MyEventAdapter(eventList, getContext()));
                            }
                        });
                    }
                }
            }});
    }
    public void requestForQuote() {
        String url = "https://fightingtomatoes.com/API/"+apiEventKey+"/Any/Any/Any";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        //appel de la requete
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {e.printStackTrace();}
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ObjectMapper objectMapper = new ObjectMapper();
                                try {
                                    eventList = Arrays.asList(objectMapper.readValue(myResponse, Event[].class));
                                    for (Event eventNumber : eventList) {
                                        if (!eventNumberList.contains(eventNumber.getEvent()) && eventNumber.getEvent().length() < 4) {
                                            eventNumberList.add(eventNumber.getEvent());
                                        }
                                    }
                                } catch (Exception i) {
                                    i.printStackTrace();
                                }
                                myRecyclerViewEventNumber.setLayoutManager(new LinearLayoutManager(getContext(),
                                        RecyclerView.VERTICAL,
                                        false));
                                myRecyclerViewEventNumber.setAdapter(new MyEventNumberAdapter(eventNumberList, getContext()));
                            }
                        });
                    }
                }
            }});
    }
}