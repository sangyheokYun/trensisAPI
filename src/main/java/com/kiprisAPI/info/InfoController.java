package com.kiprisAPI.info;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @GetMapping(value = "/info")
    public String projectInfo(){
        return "Project name is kiprisAPI";
    }
}
