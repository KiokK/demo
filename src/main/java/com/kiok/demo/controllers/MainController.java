package com.kiok.demo.controllers;

import com.kiok.demo.models.Message;
import com.kiok.demo.repo.MessageRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MessageRepos messageRepos;

    //localhost:8081/?name=bra
//    @GetMapping("/")
//    private String MainPage(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
//                            Model model){
//        Iterable<Message> all = messageRepos.findAll();
//        model.addAttribute("messages", all);
//        return "index";
//    }

    @GetMapping("/")
    private String indexPage(Model model){
        return "greeting";
    }

    @GetMapping("/main")
    private String mainPage(Model model){
        Iterable<Message> all = messageRepos.findAll();
        model.addAttribute("messages", all);
        return "index";
    }
    @PostMapping("/main")
    private String addMessage(@RequestParam String text, @RequestParam String tag, Map<String, Object> model){
        Message message = new Message(text, tag);
        System.out.println(message);
//        System.out.println("loujjhdgrsfeadsFAE");
        messageRepos.save(message);

        Iterable<Message> all = messageRepos.findAll();
        model.put("messages", all);
        return "index";
    }

    @PostMapping("filter")
    private String filter(@RequestParam String filter, Map<String, Object> model){
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty())
            messages = messageRepos.findByTag(filter);
        else
            messages = messageRepos.findAll();
        model.put("messages", messages);
        return "index";
    }
}
