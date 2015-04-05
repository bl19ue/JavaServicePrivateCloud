package com.privatecloud.team01.controllers;

import org.json.simple.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.*;

@RequestMapping("/")
@RestController
public class VMController {
	@RequestMapping(value="/vm/{id}", method=RequestMethod.GET)
    @ResponseBody
    public String getVM(@PathVariable("id") int id){
    	return "You have got the VM " + id;
    }
}
