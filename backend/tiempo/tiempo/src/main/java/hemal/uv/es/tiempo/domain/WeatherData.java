package hemal.uv.es.tiempo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "weather_data")
public class WeatherData {

    @Id
    private String date; 
    private double minTemp;
    private double maxTemp;
    private double precipitation;
    private String classification; 

  
    public WeatherData() {}

    public WeatherData(String date, double minTemp,double maxTemp, double precipitation, String classification) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;

        this.precipitation = precipitation;
        this.classification = classification;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}