package com.example.kolos.controller;
import com.example.kolos.service.GroupsService;
import org.springframework.stereotype.Controller;

@Controller
public class GroupsContoller {

    private final GroupsService groupsService;

    public GroupsContoller(GroupsService groupsService){
        this.groupsService=groupsService;
    }
}
