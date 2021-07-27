package com.example.toktik.network.reception;

import java.util.Map;

import lombok.Data;

@Data
public class Reception {
    private Map<String, Object> data;
    private int code;
    private String msg;
}
