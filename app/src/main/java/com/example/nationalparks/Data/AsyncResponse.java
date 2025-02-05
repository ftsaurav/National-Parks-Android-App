package com.example.nationalparks.Data;

import com.example.nationalparks.Model.Park;

import java.util.List;

public interface AsyncResponse {
    void processPark(List<Park> parks);
}
