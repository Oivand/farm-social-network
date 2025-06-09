package com.example.kolos.controller;

import com.example.kolos.model.Region;
import com.example.kolos.model.RolesUsers;
import com.example.kolos.model.Sector;
import com.example.kolos.model.User;
import com.example.kolos.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users") // Base path for all user-related endpoints
public class UserController {

    private final UserService userService;

    // Inject the UserService dependency
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- CRUD Operations ---

    /**
     * Handles POST requests to create a new user.
     * Maps to /users
     * @param user The User object from the request body.
     * @return ResponseEntity with the created User and HTTP status 201 (Created), or 400 (Bad Request) if validation fails.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            // Return 201 Created status with the location of the new resource
            return ResponseEntity.created(URI.create("/users/" + savedUser.getIdUser())).body(savedUser);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request for validation errors
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles GET requests to retrieve all users.
     * Maps to /users
     * @return ResponseEntity with a list of users and HTTP status 200 (OK), or 204 (No Content) if no users are found.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }

    /**
     * Handles GET requests to retrieve a user by ID.
     * Maps to /users/{id}
     * @param id The ID of the user to retrieve from the path.
     * @return ResponseEntity with the User object and HTTP status 200 (OK), or 404 (Not Found) if no user exists.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok) // If found, return 200 OK with the user
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    /**
     * Handles PUT requests to update an existing user.
     * Maps to /users/{id}
     * @param id The ID of the user to update from the path.
     * @param userDetails The User object from the request body with updated data.
     * @return ResponseEntity with the updated User and HTTP status 200 (OK), or 400 (Bad Request) if validation fails,
     * or 404 (Not Found) if the user does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User userDetails) {
        try {
            // Ensure the ID in the path matches the ID in the request body, if provided
            if (userDetails.getIdUser() != null && !userDetails.getIdUser().equals(id)) {
                // You might want to throw an exception or handle this as a bad request
                return ResponseEntity.badRequest().body(null); // Or throw new IllegalArgumentException("ID mismatch");
            }
            User updatedUser = userService.update(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            // This handles "user not found" or "validation failed" cases from the service
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or provide a specific error message
        }
    }

    /**
     * Handles DELETE requests to delete a user by ID.
     * Maps to /users/{id}
     * @param id The ID of the user to delete from the path.
     * @return ResponseEntity with HTTP status 204 (No Content) on successful deletion, or 404 (Not Found) if the user does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
        } catch (IllegalArgumentException e) {
            // This handles "user not found" cases from the service
            return ResponseEntity.notFound().build();
        }
    }

    // --- Search Operations ---

    /**
     * Handles GET requests to find users by email.
     * Maps to /users/search/by-email?email=test@example.com
     * @param email The email to search for.
     * @return ResponseEntity with the User (if found) or 404.
     */
    @GetMapping("/search/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        try {
            return userService.findUserByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles GET requests to find users by phone number.
     * Maps to /users/search/by-phone?phone=+1234567890
     * @param phone The phone number to search for.
     * @return ResponseEntity with the User (if found) or 404.
     */
    @GetMapping("/search/by-phone")
    public ResponseEntity<User> getUserByPhone(@RequestParam String phone) {
        try {
            return userService.findUserByPhone(phone)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles GET requests to find users by name (partial match).
     * Maps to /users/search/by-name?name=john
     * @param name The name to search for.
     * @return ResponseEntity with a list of users.
     */
    @GetMapping("/search/by-name")
    public ResponseEntity<List<User>> getUsersByName(@RequestParam String name) {
        try {
            List<User> users = userService.findUserByName(name);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles GET requests to find users by surname (partial match).
     * Maps to /users/search/by-surname?surname=doe
     * @param surname The surname to search for.
     * @return ResponseEntity with a list of users.
     */
    @GetMapping("/search/by-surname")
    public ResponseEntity<List<User>> getUsersBySurname(@RequestParam String surname) {
        try {
            List<User> users = userService.findUserBySurname(surname);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles GET requests to find users by full name (name and surname).
     * Maps to /users/search/by-fullname?name=John&surname=Doe
     * @param name The first name.
     * @param surname The last name.
     * @return ResponseEntity with a list of users.
     */
    @GetMapping("/search/by-fullname")
    public ResponseEntity<List<User>> getUsersByNameAndSurname(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname) {
        try {
            List<User> users = userService.findUserByNameAndSurname(name, surname);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles GET requests to find users by region.
     * Maps to /users/search/by-region?regionId=1
     * Note: You need to pass the ID of the region, and the service will fetch the Region object.
     * @param regionId The ID of the region.
     * @return ResponseEntity with a list of users.
     */
    @GetMapping("/search/by-region")
    public ResponseEntity<List<User>> getUsersByRegion(@RequestParam Long regionId) {
        try {
            // Create a dummy Region object with just the ID. The service will fetch the actual object.
            Region region = new Region();
            region.setIdRegion(regionId); // Assuming getIdRegion/setIdRegion for Region model
            List<User> users = userService.findUserByRegion(region);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Or provide error message
        }
    }

    /**
     * Handles GET requests to find users by role.
     * Maps to /users/search/by-role?roleId=1
     * @param roleId The ID of the role.
     * @return ResponseEntity with a list of users.
     */
    @GetMapping("/search/by-role")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam Long roleId) {
        try {
            // Create a dummy RolesUsers object with just the ID. The service will fetch the actual object.
            RolesUsers role = new RolesUsers();
            role.setIdRoleUser(roleId); // Assuming getIdRoleUser/setIdRoleUser for RolesUsers model
            List<User> users = userService.findUserByRole(role);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Handles GET requests to find users by sector.
     * Maps to /users/search/by-sector?sectorId=1
     * @param sectorId The ID of the sector.
     * @return ResponseEntity with a list of users.
     */
    @GetMapping("/search/by-sector")
    public ResponseEntity<List<User>> getUsersBySector(@RequestParam Long sectorId) {
        try {
            // Create a dummy Sector object with just the ID. The service will fetch the actual object.
            Sector sector = new Sector();
            sector.setIdSector(sectorId); // Assuming getIdSector/setIdSector for Sector model
            List<User> users = userService.findUserBySector(sector);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Handles GET requests to find users by nickname (partial match).
     * Maps to /users/search/by-nickname?nickname=user
     * @param nickname The nickname to search for.
     * @return ResponseEntity with a list of users.
     */
    @GetMapping("/search/by-nickname")
    public ResponseEntity<List<User>> getUsersByNickname(@RequestParam String nickname) {
        try {
            List<User> users = userService.findUserByNickname(nickname);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handles GET requests to find users by date of birth.
     * Maps to /users/search/by-dob?dateOfBirth=YYYY-MM-DD
     * @param dateOfBirth The date of birth in YYYY-MM-DD format.
     * @return ResponseEntity with a list of users.
     */
    @GetMapping("/search/by-dob")
    public ResponseEntity<List<User>> getUsersByDateOfBirth(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth) {
        try {
            List<User> users = userService.findUserByDateOfBirth(dateOfBirth);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}