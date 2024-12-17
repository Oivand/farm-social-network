package com.example.kolos.repository;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {
    //найти группу по ее названию
    List<Groups> findByGroupNameContainingOrderByCreatedAtDesc(String groupName) ;

    //найти все созданные человеком группы
    List<Groups> findByGroupMasterOrderByCreatedAtDesc (User groupMaster);

//    // Обновление имени группы
//    @Modifying
//    @Query("UPDATE groups g SET g.group_name = :name WHERE g.id_group = :id")
//    void updateUserName(@Param("id_group") Long id, @Param("group_name") String name);
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE Groups g SET g.groupName = :groupName WHERE g.idGroup = :idGroup")
//    void updateGroupName(Long idGroup, String groupName);
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE Groups g SET g.groupMaster.idUser = :groupMaster WHERE g.idGroup = :idGroup")
//    void updateGroupMaster(Long idGroup, Long groupMaster);
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE Groups g SET g.pathGroupPicture = :pathGroupPicture WHERE g.idGroup = :idGroup")
//    void updatePathGroupPicture(Long idGroup, String pathGroupPicture);
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE Groups g SET g.groupName = :groupName, g.groupMaster.idUser = :groupMaster, g.pathGroupPicture = :pathGroupPicture WHERE g.idGroup = :idGroup")
//    void updateAllFields(Long idGroup, String groupName, Long groupMaster, String pathGroupPicture);

}
