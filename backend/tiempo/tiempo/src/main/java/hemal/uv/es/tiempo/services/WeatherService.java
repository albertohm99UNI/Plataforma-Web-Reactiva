package hemal.uv.es.tiempo.services;

import hemal.uv.es.tiempo.repositories.WeatherDataRepository;
import hemal.uv.es.tiempo.domain.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    private final WebClient webClientForecast = WebClient.create("https://api.open-meteo.com/v1/forecast");
    private final WebClient webClientHistorical = WebClient.create("https://historical-forecast-api.open-meteo.com/v1/forecast");

    public Flux<WeatherData> getWeatherForDateRange(LocalDate startDate, LocalDate endDate) {
      
        return Flux.range(0, (int) (endDate.toEpochDay() - startDate.toEpochDay() + 1))
                .map(startDate::plusDays)
                .flatMap(this::checkAndFetchWeather);
    }

    private Mono<WeatherData> checkAndFetchWeather(LocalDate date) {
        
        LocalDate today = LocalDate.now();
        boolean isHistorical = date.isBefore(today) || date.isAfter(today.plusDays(15));
        if (isHistorical) {
            date = date.minusYears(1);
        }
        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        return weatherDataRepository.findByDate(dateString)
                .switchIfEmpty(fetchAndStoreWeather(date, dateString));
    }

    private Mono<WeatherData> fetchAndStoreWeather(LocalDate date, String dateString) {
        LocalDate today = LocalDate.now();
        boolean isHistorical = date.isBefore(today) || date.isAfter(today.plusDays(15));

        WebClient selectedClient = isHistorical ? webClientHistorical : webClientForecast;
        System.out.println("isHistorical: " + isHistorical);
   
        if (isHistorical) {
            date = date.minusYears(1);
        }

        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        String url = String.format(
                "?latitude=40.598&longitude=-0.457&daily=temperature_2m_max,precipitation_sum,temperature_2m_min&start_date=%s&end_date=%s", 
                formattedDate, formattedDate
        );
        System.out.println(formattedDate);
        return selectedClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .flatMap(response -> processWeatherResponse(response))
                .flatMap(weatherDataRepository::save); 
    }

    private Mono<WeatherData> processWeatherResponse(Map<String, Object> response) {
        Map<String, Object> daily = (Map<String, Object>) response.get("daily");
        List<String> times = (List<String>) daily.get("time");
        List<Double> maxTemperatures = (List<Double>) daily.get("temperature_2m_max");
        List<Double> minTemperatures = (List<Double>) daily.get("temperature_2m_min");
        List<Double> precipitation = (List<Double>) daily.get("precipitation_sum");
        
        if (times.isEmpty() || maxTemperatures.isEmpty() || minTemperatures.isEmpty() || precipitation.isEmpty()) {
            return Mono.error(new RuntimeException("No se han encontrado datos para el día solicitado"));
        }

     
        String time = times.get(0); 
        double precip = precipitation.get(0);
        double avgTemperature = (maxTemperatures.get(0) + minTemperatures.get(0)) / 2;
        double minTemperature = ( minTemperatures.get(0)) ;
        double maxTemperature = (maxTemperatures.get(0)) ;
        double totalPrecipitation = precip;

        String classification = classifyDay(avgTemperature, totalPrecipitation);
        WeatherData weatherData = new WeatherData(time, minTemperature,maxTemperature, totalPrecipitation, classification);

    
        return Mono.just(weatherData);
    }

    private String classifyDay(double temperature, double precipitation) {
        if (precipitation > 0) {
            return "Lluvioso";
        } else if (temperature >= 25) {
            return "Caluroso";
        } else if (temperature < 10) {
            return "Frío";
        } else {
            return "Normal";
        }
    }
    
}
