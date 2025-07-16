package com.messagingservice.api;

import com.messagingservice.business.abstracts.UserBlockService;
import com.messagingservice.dto.BlockRequestDTO;
import com.messagingservice.dto.BlockedUserDTO;
import com.messagingservice.entity.UserBlock;
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


    //Blocks a user specified in the request DTO.
    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestBody BlockRequestDTO request) {
        UserBlock result =  userBlockService.blockUser(request.getBlockerUsername(), request.getBlockedUsername());
        return ResponseEntity.ok(Map.of(
                "message", "User blocked successfully",
                "blockedUsername", request.getBlockedUsername(),
                "blockId", result.getId()
        ));
    }

    //Retrieves a list of users blocked by the specified blocker.
    @GetMapping("/blocker/{username}")
    public ResponseEntity<List<BlockedUserDTO>> getBlockerUser(@PathVariable("username") String username) {
        List<BlockedUserDTO> blockedUsers = userBlockService.getBlockedUsersByBlocker(username);
        return ResponseEntity.ok(blockedUsers);
    }
}
