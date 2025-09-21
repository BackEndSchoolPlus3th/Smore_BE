package com.meossamos.smore.domain.chat.webMedia.service;

import java.util.concurrent.atomic.AtomicLong;

public class MessageIdGenerator {
    private final AtomicLong seq = new AtomicLong(1);

    public String next(){
        return  String.format("%018d", seq.getAndIncrement());
    }
}
