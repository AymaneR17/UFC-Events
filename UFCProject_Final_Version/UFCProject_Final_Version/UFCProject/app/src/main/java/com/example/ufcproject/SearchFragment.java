package com.example.ufcproject;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    RecyclerView myRecyclerViewFighterEvent;
    SearchView mySearchView;
    String apiEventKey = "c68ee32ea54d50cb051b584803ee166f75e99d87";
    MyFighterAdapter myFighterAdapter;
    List<Event> eventList;
    List<Event> fighterList;
    List<Event> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        myRecyclerViewFighterEvent = view.findViewById(R.id.recyclerViewFighterEvent);
        mySearchView = view.findViewById(R.id.searchView);
        myRecyclerViewFighterEvent.setHasFixedSize(true);

        requestForFighter();
        mySearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
            }
        });
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String myText) { // rempli la liste avec le combattant recherche a l'instante
                if (eventList == null) {
                    eventList = new ArrayList<>();
                }
                filterList(myText);
                return true;
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mySearchView.clearFocus();
    }

    private void filterList(String text){

        filteredList = new ArrayList<>();
        for (Event event: eventList){
            // condition verifiant si c'est en minuscule ou majuscule
            if (event.getFighterOne().toLowerCase().contains(text.toLowerCase()) ||
                    event.getFighterTwo().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(event);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(getActivity(), "Aucun combattant trouvé", Toast.LENGTH_SHORT).show();
        }else{
                 myFighterAdapter.setFilterdList(filteredList);
            }
        }
    public void requestForFighter(){
        String url = "https://fightingtomatoes.com/API/"+apiEventKey+"/Any/Any/Any";

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
                                    fighterList = Arrays.asList(objectMapper.readValue(myResponse, Event[].class));
                                    if (eventList == null) {
                                        eventList = new ArrayList<>();
                                    }
                                    eventList.addAll(fighterList);
                                    myFighterAdapter = new MyFighterAdapter(eventList, getContext());
                                    myFighterAdapter.notifyDataSetChanged();
                                } catch (Exception i) {
                                    i.printStackTrace();
                                }

                                myRecyclerViewFighterEvent.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                                myRecyclerViewFighterEvent.setAdapter(myFighterAdapter);
                            }
                        });
                    }
                }
            }});
    }
}