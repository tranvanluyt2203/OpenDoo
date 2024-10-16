package com.example.opendoor.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {

    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private boolean success;

    // Getters và Setters


    public boolean isSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class Data {
        @SerializedName("humidity_forecast")
        private List<Double> humidityForecast;

        @SerializedName("rain_prob")
        private List<Double> rainProb;

        @SerializedName("temp_forecast")
        private List<Double> tempForecast;

        // Getters và Setters

        public List<Double> getHumidityForecast() {
            return humidityForecast;
        }

        public List<Double> getRainProb() {
            return rainProb;
        }

        public List<Double> getTempForecast() {
            return tempForecast;
        }
    }
}

