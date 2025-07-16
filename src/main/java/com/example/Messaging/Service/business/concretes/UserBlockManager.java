package com.example.Messaging.Service.business.concretes;

import com.example.Messaging.Service.business.abstracts.UserBlockService;
import com.example.Messaging.Service.core.exception.BlockerUserNotFoundException;
import com.example.Messaging.Service.core.exception.UserToBeBlockedNotFoundException;
import com.example.Messaging.Service.core.utilies.CustomExceptionMessage;
import com.example.Messaging.Service.dao.UserBlockRepo;
import com.example.Messaging.Service.dao.UserRepo;
import com.example.Messaging.Service.dto.BlockedUserDTO;
import com.example.Messaging.Service.entity.UserBlock;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBlockManager implements UserBlockService {
    private final UserBlockRepo userBlockRepo;
    private final UserRepo userRepo;
    private static final Logger logger = LoggerFactory.getLogger(UserBlockManager.class);

    public UserBlockManager(UserBlockRepo userBlockRepo,UserRepo userRepo) {
        this.userBlockRepo = userBlockRepo;
        this.userRepo=userRepo;
    }

    @Override
    public UserBlock blockUser(String blockerUsername, String blockedUsername) {
        // Kendi kendini engelleme kontrolü
        if (blockerUsername.equalsIgnoreCase(blockedUsername)) {
            logger.warn("The user tried to block herself: {}");
            //return "The user cannot block himself.";//CUastom kendşni engellleyemz ıstısna fırlat
        }

        // Engelleyen ve engellenen kullanıcıların varlık kontrolü
        userRepo.findByUsername(blockerUsername)
                .orElseThrow(() -> new BlockerUserNotFoundException(CustomExceptionMessage.BLOCKER_USER_NOT_FOUND));

        userRepo.findByUsername(blockedUsername)
                .orElseThrow(() -> new UserToBeBlockedNotFoundException(CustomExceptionMessage.USER_TO_BE_BLOCKED_NOT_FOUND));

        // Aynı bloklama daha önce yapılmış mı kontrolü
        boolean alreadyBlocked = userBlockRepo.existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase(
                blockerUsername.trim(), blockedUsername.trim());

        if (alreadyBlocked) {
            logger.info("User {} has already blocked user {}");
            //return "User was already blocked.";// Üstteki gibi hata fırlat
        }

        // Yeni blok kaydı oluştur
        UserBlock userBlock = new UserBlock();
        userBlock.setBlockerUsername(blockerUsername);
        userBlock.setBlockedUsername(blockedUsername);

        userBlockRepo.save(userBlock);
        logger.info("User {} -> Successfully blocked user {}");

        return userBlock;
    }

    @Override
    public String unblockUser(String blockerUsername, String blockedUsername) {
        return null;
    }

    @Override
    public boolean isBlocked(String blockerUsername, String blockedUsername) {
        return userBlockRepo.existsByBlockerUsernameIgnoreCaseAndBlockedUsernameIgnoreCase(
                blockerUsername.trim(), blockedUsername.trim());
    }

    @Override
    public List<BlockedUserDTO> getBlockedUsersByBlocker(String blockerUsername) {
        List<UserBlock> blocks = userBlockRepo.findByBlockerUsername(blockerUsername);
        return blocks.stream()
                .map(block -> new BlockedUserDTO(block.getBlockedUsername()))
                .collect(Collectors.toList());
    }
}
