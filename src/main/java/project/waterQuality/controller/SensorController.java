package project.waterQuality.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.waterQuality.model.SensorData;
import project.waterQuality.repository.SensorRepository;
import project.waterQuality.service.SensorService;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class SensorController {

	private final SensorRepository repository;

	private final SensorService sensorService;

	public SensorController(SensorRepository repository, SensorService sensorService) {
		this.repository = repository;
		this.sensorService = sensorService;
	}

	@GetMapping("/sensor-data")
	public ResponseEntity<List<SensorData>> getAllSensorData() {
		List<SensorData> data = repository.findAll();
		return ResponseEntity.ok(data);
	}

	@GetMapping("/sensor-data/{id}")
	public ResponseEntity<SensorData> getSensorDataById(@PathVariable Long id) {
		return repository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/sensor-data/filter") 
	public ResponseEntity<List<SensorData>> filterSensorData(
		@RequestParam String type,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate ) {
		
    	System.out.println("Received request - type: " + type + 
                      ", startDate: " + startDate + 
                      ", endDate: " + endDate);

		List<SensorData> filteredData;
		if (startDate != null && endDate != null) {
			filteredData = sensorService.getSensorDataByTypeAndDateRange(type, startDate, endDate);
		} else {
			filteredData = sensorService.getSensorDataByType(type);
		}
		return ResponseEntity.ok(filteredData);
	}

}
