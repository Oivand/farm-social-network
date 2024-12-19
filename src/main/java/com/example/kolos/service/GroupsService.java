package com.example.kolos.service;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User;

import java.util.List;
import java.util.Optional;

public interface GroupsService {
    //найти группу по ее названию
    List<Groups> findGroupByGroupName(String groupName) ;

    //найти все созданные человеком группы
    List<Groups> findGroupByMaster (User groupMaster);

    Optional<Groups> findById(Long idGroup);

    List<Groups> findAll();
}
