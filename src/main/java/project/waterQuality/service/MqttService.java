package project.waterQuality.service;

import java.time.LocalDateTime;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import project.waterQuality.model.SensorData;
import project.waterQuality.repository.SensorRepository;

@Service
public class MqttService {
	@Value("${mqtt.broker-url}")
	private String brokerUrl;
	@Value("${mqtt.username}")
	private String mqttUser;
	@Value("${mqtt.password}")
	private String mqttPass;
	private MqttClient client;

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private final SensorRepository repository;

	public MqttService(SensorRepository repository) {
		this.repository = repository;
	}

	@PostConstruct
	public void init() throws MqttException {
		client = new MqttClient(brokerUrl, MqttClient.generateClientId());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName(mqttUser);
		options.setPassword(mqttPass.toCharArray());
		options.setAutomaticReconnect(true);
		client.connect(options);
		client.subscribe("sensors/#", this::handleMessage);
	}

	private void handleMessage(String topic, MqttMessage message) {
		String payload = new String(message.getPayload());
		System.out.println("Received on topic " + topic + ": " + payload);
		try {
			JsonNode root = MAPPER.readTree(message.getPayload());
			String type = root.path("type").asText("unknown");

			if (root.has("values")) {
                for (JsonNode n : root.get("values")) insert(type, n.asDouble());
            } else {
                insert(type, root.path("value").asDouble());
            }
        } catch (Exception e) {
            System.err.println("MQTT parse error: " + e.getMessage());
		}
	}

	private void insert(String type, double value) {
		SensorData d = new SensorData();
		d.setType(type);
		d.setValue(value);
		d.setTimestamp(LocalDateTime.now());
		repository.save(d);
	}

	@PreDestroy
    public void cleanup() throws MqttException {
        if (client != null && client.isConnected()) client.disconnect();
    }
}