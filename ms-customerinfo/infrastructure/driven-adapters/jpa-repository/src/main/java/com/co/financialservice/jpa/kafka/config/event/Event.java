package com.co.financialservice.jpa.kafka.config.event;

import lombok.Data;

import java.util.Date;

@Data
public class Event <T> {
    private String id;
    private Date date;
    private String type;
    private T data;
}