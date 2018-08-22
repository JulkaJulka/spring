package com.lesson2.rout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RoutController {
    @Autowired
    Route route;
    @Autowired
    Step step;
    @Autowired
    Service service;

    private void callByBean() {
        route.getId();
        route.getSteps();

        step.getId();
        step.getParamsServiceFrom();
        step.getParamsServiceTo();
        step.getServiceTo();
        step.getServiceFrom();


        service.getId();
        service.getName();
        service.getParamsToCall();

    }
}
