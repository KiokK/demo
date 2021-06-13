package com.kiok.demo.controllers;

import com.kiok.demo.models.Message;
import com.kiok.demo.models.User;
import com.kiok.demo.repo.MessageRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private MessageRepos messageRepos;

    @Value("${upload.path}")
    private String uploadPath;

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
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model
    ) throws IOException {
        Message message = new Message(text, tag, user);

        if (file != null){
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists() && !file.getOriginalFilename().isEmpty()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();// создаем уникальное имя
            String resultFileName = uuidFile + "." + file.getOriginalFilename();//совмешаем с именем пользователя

            file.transferTo(new File(uploadPath + "/" + resultFileName));//загрузка

            message.setFilename(resultFileName);
        }

        messageRepos.save(message);

        Iterable<Message> all = messageRepos.findAll();
        model.put("messages", all);
        return "index";
    }

}
