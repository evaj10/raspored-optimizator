package org.acme.schooltimetabling.rest;

import org.acme.schooltimetabling.domain.MeetingAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wrapper.url}")
    private String url;

    public void sendNotification(List<MeetingAssignment> meetingAssignments) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<MeetingAssignment>> entity = new HttpEntity<>(meetingAssignments, headers);
        this.restTemplate.postForLocation(url, entity);
    }
}
