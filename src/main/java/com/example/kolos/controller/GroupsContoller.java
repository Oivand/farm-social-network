package com.example.kolos.controller;
import com.example.kolos.service.GroupsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupsContoller {
    private final GroupsService groupsService;

    public GroupsContoller(GroupsService groupsService) {
        this.groupsService = groupsService;
    }
}