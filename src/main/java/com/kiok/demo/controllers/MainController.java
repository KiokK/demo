package com.kiok.demo.controllers;

import com.kiok.demo.models.Message;
import com.kiok.demo.models.User;
import com.kiok.demo.repo.MessageRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MessageRepos messageRepos;

    @GetMapping("/")
    private String indexPage(Model model){
        return "greeting";
    }

    @GetMapping("/main")
    private String mainPage(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model){
        Iterable<Message> all = messageRepos.findAll();

        if (filter != null && !filter.isEmpty())
            all = messageRepos.findByTag(filter);
        else
            all = messageRepos.findAll();

        model.addAttribute("messages", all);
        model.addAttribute("filter", all);
        return "index";
    }

    @PostMapping("/main")
    private String addMessage(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model){
        Message message = new Message(text, tag, user);
//        System.out.println(message);
        messageRepos.save(message);

        Iterable<Message> all = messageRepos.findAll();
        model.put("messages", all);
        return "index";
    }

}
