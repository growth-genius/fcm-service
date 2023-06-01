package com.gg.tgather.fcmservice.modules.fcm.repository;

import com.gg.tgather.fcmservice.modules.fcm.entity.FcmMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmRepository extends JpaRepository<FcmMessage, Long> {}
