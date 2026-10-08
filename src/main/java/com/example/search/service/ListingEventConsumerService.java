package com.example.search.service;

import com.example.search.dto.ListingEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ListingEventConsumerService {
    @Autowired
    private SearchIndexService searchIndexService;


    @KafkaListener(topics = "listing-events", groupId = "search-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ListingEventDTO event) {

        System.out.println("Event Recieved " + event + "Event Type "+event.getEventType());

        switch (event.getEventType()) {

            case LISTING_CREATED -> searchIndexService.handleListingCreated(event);

            case LISTING_UPDATED -> searchIndexService.handleListingUpdated(event);

            case LISTING_DELETED -> searchIndexService.handleListingDeleted(event);
        }
    }
}
