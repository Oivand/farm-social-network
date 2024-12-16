package com.example.kolos.repository;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {
    //найти группу по ее айди
    List<Groups> findByIdGroup(Long idGroup);

    //найти группу по ее названию
    List<Groups> findByGroupName(String groupName) ;

    //найти все созданные человеком группы
    List<Groups> findByGroupMaster (User groupMaster);
}
