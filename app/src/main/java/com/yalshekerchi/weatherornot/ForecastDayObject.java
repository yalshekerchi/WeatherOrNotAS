package com.yalshekerchi.weatherornot;

public class ForecastDayObject {
    private String minTemperature;
    private String maxTemperature;
    private String dayDescription;
    private String dayIcon;
    private String nightDescription;
    private String nightIcon;

    ForecastDayObject (String _minTemp, String _maxTemp, String _dayDesc, String _dayIcon,
                       String _nightDesc, String _nightIcon){
        this.minTemperature = _minTemp;
        this.maxTemperature = _maxTemp;
        this.dayDescription = _dayDesc;
        this.dayIcon = _dayIcon;
        this.nightDescription = _nightDesc;
        this.nightIcon = _nightIcon;
    }

    ForecastDayObject (String _minTemp, String _maxTemp, String _dayDesc, String _dayIcon){
        this(_minTemp, _maxTemp, _dayDesc, _dayIcon, null, null);
    }
    ForecastDayObject (){
        this(null, null, null, null, null, null);
    }

    public String getMinTemperature() {
        return this.minTemperature;
    }
    public void setMinTemperature(String _minTemp) {
        this.minTemperature = _minTemp;
    }

    public String getMaxTemperature() {
        return this.maxTemperature;
    }
    public void setMaxTemperature(String _maxTemp) {
        this.maxTemperature = _maxTemp;
    }

    public String getDayDescription() {
        return this.dayDescription;
    }
    public void setDayDescription(String _dayDesc) {
        this.dayDescription = _dayDesc;
    }

    public String getDayIcon() {
        return this.dayIcon;
    }
    public void setDayIcon(String _dayIcon) {
        this.dayIcon = _dayIcon;
    }

    public String getNightDescription() {
        return this.nightDescription;
    }
    public void setNightDescription(String _nightDesc) {
        this.nightDescription = _nightDesc;
    }

    public String getNightIcon() {
        return this.nightIcon;
    }
    public void setNightIcon(String _nightIcon) {
        this.dayIcon = _nightIcon;
    }
}