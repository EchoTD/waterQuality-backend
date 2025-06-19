package project.waterQuality.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import project.waterQuality.model.SensorData;
import project.waterQuality.repository.SensorRepository;

@Service
public class SensorService {
    
    private final SensorRepository repository;

    public SensorService(SensorRepository repository) {
        this.repository = repository;
    }

    public List<SensorData> getSensorDataByType(String type) {
        return repository.findByTypeOrderByTimestampDesc(type);
    }

    public List<SensorData> getSensorDataByTypeAndDateRange(
            String type, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByTypeAndTimestampBetweenOrderByTimestampDesc(
            type, startDate, endDate);
    }
}
