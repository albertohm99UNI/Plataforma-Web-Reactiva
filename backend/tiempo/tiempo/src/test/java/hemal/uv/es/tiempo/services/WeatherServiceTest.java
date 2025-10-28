package hemal.uv.es.tiempo.services;

import hemal.uv.es.tiempo.domain.WeatherData;
import hemal.uv.es.tiempo.repositories.WeatherDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @Mock
    private WeatherDataRepository weatherDataRepository;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testClassifyDay() {
        String result1 = invokeClassifyDay(26.0, 0.0);
        String result2 = invokeClassifyDay(8.0, 0.0);
        String result3 = invokeClassifyDay(18.0, 0.0);
        String result4 = invokeClassifyDay(15.0, 5.0);

        assert result1.equals("Caluroso");
        assert result2.equals("Frío");
        assert result3.equals("Normal");
        assert result4.equals("Lluvioso");
    }

    private String invokeClassifyDay(double temp, double prec) {
        return org.springframework.test.util.ReflectionTestUtils
                .invokeMethod(weatherService, "classifyDay", temp, prec);
    }

    @Test
    void testProcessWeatherResponse_success() {
        Map<String, Object> response = Map.of(
                "daily", Map.of(
                        "time", List.of("2023-04-01"),
                        "temperature_2m_max", List.of(22.0),
                        "temperature_2m_min", List.of(12.0),
                        "precipitation_sum", List.of(0.0)
                )
        );

        Mono<WeatherData> result = org.springframework.test.util
                .ReflectionTestUtils.invokeMethod(weatherService, "processWeatherResponse", response);

        StepVerifier.create(result)
                .expectNextMatches(data -> data.getClassification().equals("Normal"))
                .verifyComplete();
    }

    @Test
    void testProcessWeatherResponse_missingData() {
        Map<String, Object> response = Map.of(
                "daily", Map.of(
                        "time", List.of(),
                        "temperature_2m_max", List.of(),
                        "temperature_2m_min", List.of(),
                        "precipitation_sum", List.of()
                )
        );

        Mono<WeatherData> result = org.springframework.test.util
                .ReflectionTestUtils.invokeMethod(weatherService, "processWeatherResponse", response);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err.getMessage().contains("No se han encontrado datos"))
                .verify();
    }

    @Test
    void testCheckAndFetchWeather_foundInRepo() {
        LocalDate date = LocalDate.now();
        String dateStr = date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        WeatherData mockData = new WeatherData(dateStr, 10, 20, 0, "Normal");

        when(weatherDataRepository.findByDate(dateStr)).thenReturn(Mono.just(mockData));

        StepVerifier.create(weatherService.getWeatherForDateRange(date, date))
                .expectNextMatches(wd -> wd.getClassification().equals("Normal"))
                .verifyComplete();

        verify(weatherDataRepository).findByDate(dateStr);
    }
}
