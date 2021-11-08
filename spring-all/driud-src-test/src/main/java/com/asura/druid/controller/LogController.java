package com.asura.druid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.asura.druid.service.ILogService;

@RestController
@RequestMapping("/asura/log")
public class LogController {
    @Autowired
    private ILogService iLogService;

    @GetMapping("/get")
    public String selectLog() {
       int result = iLogService.select();
        return "count:" + result;
    }
}
