package com.census.cargo.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.census.cargo.scrape.VehicleTypeBreakdownInfo;


/*
 * Author: @ram
 * 
 * Service class for Kafka Consumer. 
 * 
 * Provides asynchronous web page scraping support
 * 
 */
@Service
public class TopicListener {

	@Autowired
	private VehicleTypeBreakdownInfo vehicleTypeBreakdownInfo;

    @Value("${topic.name.consumer")
    private String topicName;

    @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
    public void consume(ConsumerRecord<String, String> payload){
    	vehicleTypeBreakdownInfo.scrape(payload.value().trim());
    }

}