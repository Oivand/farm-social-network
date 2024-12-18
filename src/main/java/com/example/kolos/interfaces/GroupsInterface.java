package com.example.kolos.interfaces;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User;

import java.util.List;

public interface GroupsInterface {
    //найти группу по ее названию
    List<Groups> findGroupByGroupName(String groupName) ;

    //найти все созданные человеком группы
    List<Groups> findGroupByMaster (User groupMaster);
}
