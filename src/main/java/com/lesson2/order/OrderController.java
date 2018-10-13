package com.lesson2.order;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {
    @Autowired
    OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, value = "/orderSave", produces = "text/plain")
    public @ResponseBody
    String saveOrder() {
        orderService.save(null);
        return "ok";
    }

     /*public Order put(Order order) {
        orderService.test(0, 1111);
        return orderService.put(order);
    }*/

   /* private OrderService getOrderService() {
        if (orderService == null)
            orderService = new OrderService();
        return orderService;
    }*/
}
