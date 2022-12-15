package com.example.workoutlog;

public class Rekord {
    private int id;
    private Double Waga;
    private String Data;
    private String Time;

    public Rekord(int id, Double waga, String data, String time) {
        this.id = id;
        Waga = waga;
        Data = data;
        Time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getWaga() {
        return Waga;
    }

    public void setWaga(Double waga) {
        Waga = waga;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}






