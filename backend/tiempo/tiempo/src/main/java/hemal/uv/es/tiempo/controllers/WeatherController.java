package hemal.uv.es.tiempo.controllers;

import hemal.uv.es.tiempo.domain.WeatherData;
import hemal.uv.es.tiempo.services.WeatherService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public Flux<WeatherData> getWeather(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        if (startDate.isAfter(endDate)) {
            return Flux.error(new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin."));
        }

        return weatherService.getWeatherForDateRange(startDate, endDate);
    }
}
