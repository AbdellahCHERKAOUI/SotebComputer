package com.example.computershop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    @RequestMapping(value = "/pro")
    public String index(){
        return "product";
    }
}
