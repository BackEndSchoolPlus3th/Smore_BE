package com.meossamos.smore.domain.chat.webMedia.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class MessageIdGenerator {
    private final AtomicLong seq = new AtomicLong(1);

    public String next(){
        return  String.format("%018d", seq.getAndIncrement());
    }
}
