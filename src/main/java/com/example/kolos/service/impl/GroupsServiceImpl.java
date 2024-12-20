package com.example.kolos.service.impl;

import com.example.kolos.repository.GroupsRepository;
import com.example.kolos.service.GroupsService;
import com.example.kolos.model.Groups;
import com.example.kolos.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupsServiceImpl implements GroupsService {
    private final GroupsRepository groupsRepository;

    public GroupsServiceImpl(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @Override
    public List<Groups> findGroupByGroupName(String groupName) {
        if (groupName == null || groupName.isEmpty()) {
            return findAll();
        }
        return groupsRepository.findByGroupNameContainingOrderByCreatedAtDesc(groupName);
    }

    @Override
    public List<Groups> findGroupByMaster(User groupMaster) {
        if (groupMaster == null) {
            throw new IllegalArgumentException("Group master cannot be null");
        }
        return groupsRepository.findByGroupMasterOrderByCreatedAtDesc(groupMaster);
    }

    @Override
    public Optional<Groups> findById(Long idGroup) {
        if (idGroup == null) {
            throw new IllegalArgumentException("Group ID cannot be null");
        }
        return groupsRepository.findById(idGroup);
    }

    @Override
    public List<Groups> findAll() {
        List<Groups> allGroups = groupsRepository.findAll();
        if (allGroups.isEmpty()) {
            throw new IllegalStateException("No groups found");
        }
        return allGroups;
    }
}
