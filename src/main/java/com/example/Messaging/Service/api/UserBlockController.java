package com.example.Messaging.Service.api;

import com.example.Messaging.Service.business.abstracts.UserBlockService;
import com.example.Messaging.Service.business.abstracts.UserService;
import com.example.Messaging.Service.dto.BlockRequestDTO;
import com.example.Messaging.Service.dto.BlockedUserDTO;
import com.example.Messaging.Service.entity.UserBlock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blocks")
public class UserBlockController {
    private final UserBlockService userBlockService;

    public UserBlockController(UserBlockService userBlockService) {
        this.userBlockService = userBlockService;
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestBody BlockRequestDTO request) {
        UserBlock result =  userBlockService.blockUser(request.getBlockerUsername(), request.getBlockedUsername());
        return ResponseEntity.ok(Map.of(
                "message", "User blocked successfully",
                "blockedUsername", request.getBlockedUsername(),
                "blockId", result.getId()
        ));
    }
    @GetMapping("/blocker/{username}")
    public ResponseEntity<List<BlockedUserDTO>> getBlockerUser(@PathVariable("username") String username) {
        List<BlockedUserDTO> blockedUsers = userBlockService.getBlockedUsersByBlocker(username);
        return ResponseEntity.ok(blockedUsers);
    }
}
