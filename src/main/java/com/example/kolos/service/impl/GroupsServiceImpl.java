package com.example.kolos.service.impl;

import com.example.kolos.interfaces.GroupsInterface;
import com.example.kolos.model.Groups;
import com.example.kolos.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupsServiceImpl implements GroupsInterface {
    @Override
    public List<Groups> findGroupByGroupName(String groupName) {
        return List.of();
    }

    @Override
    public List<Groups> findGroupByMaster(User groupMaster) {
        return List.of();
    }
}
