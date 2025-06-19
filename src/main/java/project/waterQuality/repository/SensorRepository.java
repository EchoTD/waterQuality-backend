package project.waterQuality.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.waterQuality.model.SensorData;


public interface SensorRepository extends JpaRepository<SensorData, Long> {

    List<SensorData> findByTypeOrderByTimestampDesc(String type);
    
    List<SensorData> findByTypeAndTimestampBetweenOrderByTimestampDesc(
        String type, LocalDateTime startDate, LocalDateTime endDate);

}
