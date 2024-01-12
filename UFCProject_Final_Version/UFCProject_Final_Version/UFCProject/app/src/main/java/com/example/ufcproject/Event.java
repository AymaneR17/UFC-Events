package com.example.ufcproject;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Event {

    @JsonProperty("date")
    public String date;
    @JsonProperty("promotion")
    public String promotion;
    @JsonProperty("event")
    public String event;
    @JsonProperty("card_placement")
    public String card_placement;
    @JsonProperty("fighter_1")
    public String fighterOne;
    @JsonProperty("fighter_2")
    public String fighterTwo;
    @JsonProperty("winner")
    public String winner;
    @JsonProperty("round")
    public String round;
    @JsonProperty("method")
    public String method;

    @JsonProperty("main_or_prelim")
    public String mainOrPrelim;

    @JsonProperty("time")
    public String timeOfMatch;

    @JsonProperty("rematch")
    public String rematch;

    @JsonProperty("fighting_tomatoes_aggregate_rating")
    public String agregateRating;

    @JsonProperty("fighting_tomatoes_number_ratings")
    public String numberRating;


    public String getDate() {
        return date;
    }
    public String getPromotion() {
        return promotion;
    }
    public String getEvent() {
        return event;
    }
    public String getCardPlacement() {
        return card_placement;
    }
    public String getFighterOne() {
        return fighterOne;
    }
    public String getFighterTwo() {
        return fighterTwo;
    }
    public String getWinner(){return winner;}
    public String getMainOrPrelim() {
        return mainOrPrelim;
    }

    public String getRound(){return round;}
    public String getMethod(){return method;}

    public String getTimeOfMatch() {
        return timeOfMatch;
    }
    public String getRematch() {
        return rematch;
    }
    public String getAgregateRating() {
        return agregateRating;
    }
    public String getNumberRating() {
        return numberRating;
    }
}
