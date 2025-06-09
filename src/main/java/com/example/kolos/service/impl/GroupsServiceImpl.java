package com.example.kolos.service.impl;

import com.example.kolos.model.Groups;
import com.example.kolos.model.User;
import com.example.kolos.repository.GroupsRepository;
import com.example.kolos.repository.UserRepository; // We'll need UserRepository to validate Users
import com.example.kolos.service.GroupsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For transactional methods
import java.util.List;
import java.util.Optional;

@Service
public class GroupsServiceImpl implements GroupsService {

    private final GroupsRepository groupsRepository;
    private final UserRepository userRepository; // Inject UserRepository to validate User objects

    public GroupsServiceImpl(GroupsRepository groupsRepository, UserRepository userRepository) {
        this.groupsRepository = groupsRepository;
        this.userRepository = userRepository;
    }

    // --- CRUD Operations ---

    @Override
    @Transactional // Ensures atomicity: all or nothing
    public Groups save(Groups group) {
        if (group == null) {
            throw new IllegalArgumentException("Group object cannot be null.");
        }
        if (group.getGroupName() == null || group.getGroupName().trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty.");
        }
        if (group.getGroupMaster() == null || group.getGroupMaster().getIdUser() == null) {
            throw new IllegalArgumentException("Group must have a valid master (user ID).");
        }

        // Validate if group name is unique (for new groups)
        if (group.getIdGroup() == null) {
            if (groupsRepository.findByGroupNameIgnoreCase(group.getGroupName()).isPresent()) {
                throw new IllegalArgumentException("Group with name '" + group.getGroupName() + "' already exists.");
            }
        }

        // Validate and set the Group Master (User must exist)
        User master = userRepository.findById(group.getGroupMaster().getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Group master with ID " + group.getGroupMaster().getIdUser() + " not found."));
        group.setGroupMaster(master);

        // Members are handled during update or addMember/removeMember operations
        // If members are present during save, ensure they exist in the DB too
        if (group.getMembers() != null && !group.getMembers().isEmpty()) {
            group.getMembers().forEach(member -> {
                userRepository.findById(member.getIdUser())
                        .orElseThrow(() -> new IllegalArgumentException("Member with ID " + member.getIdUser() + " not found."));
            });
        }

        return groupsRepository.save(group);
    }

    @Override
    @Transactional
    public Groups update(Long groupId, Groups updatedGroup) {
        if (groupId == null) {
            throw new IllegalArgumentException("Group ID for update cannot be null.");
        }
        if (updatedGroup == null) {
            throw new IllegalArgumentException("Updated group object cannot be null.");
        }
        if (updatedGroup.getGroupName() == null || updatedGroup.getGroupName().trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty during update.");
        }

        Groups existingGroup = groupsRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with ID " + groupId + " not found for update."));

        // Validate if group name is unique (if changed and not the current group)
        if (!existingGroup.getGroupName().equalsIgnoreCase(updatedGroup.getGroupName())) {
            if (groupsRepository.findByGroupNameIgnoreCase(updatedGroup.getGroupName()).isPresent()) {
                throw new IllegalArgumentException("Group with name '" + updatedGroup.getGroupName() + "' already exists.");
            }
        }

        existingGroup.setGroupName(updatedGroup.getGroupName());
        existingGroup.setPathGroupPicture(updatedGroup.getPathGroupPicture());
        existingGroup.setLastSinceTime(updatedGroup.getLastSinceTime());

        // Update Group Master if provided
        if (updatedGroup.getGroupMaster() != null && updatedGroup.getGroupMaster().getIdUser() != null) {
            User newMaster = userRepository.findById(updatedGroup.getGroupMaster().getIdUser())
                    .orElseThrow(() -> new IllegalArgumentException("New group master with ID " + updatedGroup.getGroupMaster().getIdUser() + " not found."));
            existingGroup.setGroupMaster(newMaster);
        } else if (updatedGroup.getGroupMaster() != null) {
            throw new IllegalArgumentException("Group master ID cannot be null if group master object is provided for update.");
        }
        // Note: Members are typically managed via separate add/remove methods, not directly via a full group update.
        // If you send members in updateGroup, they will overwrite the existing set.
        // It's generally safer to manage ManyToMany relationships with dedicated methods.

        return groupsRepository.save(existingGroup);
    }

    @Override
    @Transactional
    public void deleteById(Long groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("Group ID for deletion cannot be null.");
        }
        if (!groupsRepository.existsById(groupId)) {
            throw new IllegalArgumentException("Group with ID " + groupId + " not found for deletion.");
        }
        groupsRepository.deleteById(groupId);
    }

    // --- Search Operations ---

    @Override
    public Optional<Groups> findGroupByExactName(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name for exact search cannot be null or empty.");
        }
        return groupsRepository.findByGroupNameIgnoreCase(groupName);
    }

    @Override
    public List<Groups> findGroupsByNameContaining(String groupName) {
        if (groupName == null || groupName.trim().isEmpty()) {
            // Returning an empty list or throwing an exception can be debated.
            // For 'containing' searches, an empty input often means "no criteria", so returning all is sometimes okay.
            // However, your previous implementation threw an error, so I'll follow that pattern for consistency
            // if an empty string means "invalid search input".
            // If you want to return all for empty input, change this to return findAllGroupsOrderedByDate();
            throw new IllegalArgumentException("Group name for partial search cannot be null or empty.");
        }
        return groupsRepository.findByGroupNameContainingIgnoreCaseOrderByCreatedAtDesc(groupName);
    }

    @Override
    public List<Groups> findGroupsByMasterId(Long groupMasterId) {
        if (groupMasterId == null) {
            throw new IllegalArgumentException("Group Master ID cannot be null.");
        }
        return groupsRepository.findByGroupMaster_IdUserOrderByCreatedAtDesc(groupMasterId);
    }

    @Override
    public List<Groups> findGroupsByMaster(User groupMaster) {
        if (groupMaster == null || groupMaster.getIdUser() == null) {
            throw new IllegalArgumentException("Group master object or its ID cannot be null.");
        }
        // Ensure the User object is a managed entity or exists in the DB if needed for specific operations
        User existingMaster = userRepository.findById(groupMaster.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Group master with ID " + groupMaster.getIdUser() + " not found."));

        return groupsRepository.findByGroupMasterOrderByCreatedAtDesc(existingMaster);
    }

    @Override
    public Optional<Groups> findById(Long groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("Group ID cannot be null.");
        }
        return groupsRepository.findById(groupId);
    }

    @Override
    public List<Groups> findGroupsByMember(User user) {
        if (user == null || user.getIdUser() == null) {
            throw new IllegalArgumentException("Member user object or its ID cannot be null.");
        }
        // Ensure the User object is a managed entity or exists in the DB if needed
        User existingUser = userRepository.findById(user.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Member with ID " + user.getIdUser() + " not found."));

        return groupsRepository.findByMembersContainingOrderByCreatedAtDesc(existingUser);
    }

    @Override
    public List<Groups> findGroupsByMemberId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Member User ID cannot be null.");
        }
        // It's good practice to ensure the user actually exists before searching for groups they are a member of
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found to search for group membership."));

        return groupsRepository.findByMembers_IdUserOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Groups> findAllGroupsOrderedByDate() {
        return groupsRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Groups> findAll() {
        return groupsRepository.findAll(); // This calls the default JpaRepository.findAll(), no specific order guaranteed
    }

    // --- Specific Methods for Managing Members (Highly Recommended) ---

    /**
     * Adds a member to a group.
     * @param groupId The ID of the group.
     * @param userId The ID of the user to add as a member.
     * @return The updated Groups object.
     */
    @Transactional
    public Groups addMemberToGroup(Long groupId, Long userId) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with ID " + groupId + " not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

        if (group.getMembers().contains(user)) {
            throw new IllegalArgumentException("User with ID " + userId + " is already a member of group " + groupId + ".");
        }

        group.getMembers().add(user);
        return groupsRepository.save(group); // Save to update the ManyToMany relationship
    }

    /**
     * Removes a member from a group.
     * @param groupId The ID of the group.
     * @param userId The ID of the user to remove.
     * @return The updated Groups object.
     */
    @Transactional
    public Groups removeMemberFromGroup(Long groupId, Long userId) {
        Groups group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with ID " + groupId + " not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));

        if (!group.getMembers().contains(user)) {
            throw new IllegalArgumentException("User with ID " + userId + " is not a member of group " + groupId + ".");
        }

        group.getMembers().remove(user);
        return groupsRepository.save(group); // Save to update the ManyToMany relationship
    }
}