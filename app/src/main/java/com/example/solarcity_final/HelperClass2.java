package com.example.solarcity_final;

public class HelperClass2 {
    int temperature, humidity, dewpoint, precipitation;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getDewpoint() {
        return dewpoint;
    }

    public void setDewpoint(int dewpoint) {
        this.dewpoint = dewpoint;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    public HelperClass2(int temperature, int humidity, int dewpoint, int precipitation) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.dewpoint = dewpoint;
        this.precipitation = precipitation;
    }

    public HelperClass2() {
    }
}
