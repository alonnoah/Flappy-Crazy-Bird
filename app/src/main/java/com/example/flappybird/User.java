package com.example.flappybird;

import java.util.ArrayList;


public class User implements Comparable<User> {
    private String email, username;
    private int bestScore, moneyCount;
    private String currentAvatar;
    private ArrayList<String> AvatarArrayList;

    public User() {

    }

    public ArrayList<String> getAvatarArrayList() {
        return AvatarArrayList;
    }

    public void setAvatarArrayList(ArrayList<String> AvatarArrayList) {
        this.AvatarArrayList = AvatarArrayList;
    }

    public User(String username, String email) {
        this.email = email;
        this.username = username;
        this.bestScore = 0;
        this.moneyCount = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getMoneyCount() {
        return moneyCount;
    }

    public void setMoneyCount(int moneyCount) {
        this.moneyCount = moneyCount;
    }

    public int compareTo(User o) {
        return o.getBestScore() - this.getBestScore();
    }

    public String getCurrentAvatar() {
        return currentAvatar;
    }

    public void setCurrentAvatar(String currentAvatar) {
        this.currentAvatar = currentAvatar;
    }

    public String toString() {
        return this.username + " " + this.bestScore;
    }
}
