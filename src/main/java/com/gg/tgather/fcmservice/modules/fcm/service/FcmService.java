package com.gg.tgather.fcmservice.modules.fcm.service;

import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.dto.fcm.FcmMessageDto;
import com.gg.tgather.fcmservice.infra.properties.FcmProperties;
import com.gg.tgather.fcmservice.modules.fcm.entity.FcmMessage;
import com.gg.tgather.fcmservice.modules.fcm.repository.FcmRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class FcmService {

    private final FcmProperties fcmProperties;
    private final FcmRepository fcmRepository;

    public boolean pushMessage(FcmMessageDto fcmMessageDto) throws FirebaseMessagingException, IOException {
        firebaseInit();
        Message message = getMessage(fcmMessageDto);
        String response = FirebaseMessaging.getInstance().send(message);
        log.info("fcmService.message response : {}", response);
        fcmRepository.save(FcmMessage.from(fcmMessageDto));
        return true;
    }

    private void firebaseInit() throws IOException {
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault().createScoped(fcmProperties.getScopes())).build();
        FirebaseApp.initializeApp(firebaseOptions);
    }

    public boolean pushMessages(List<FcmMessageDto> fcmMessages) throws FirebaseMessagingException, IOException {
        firebaseInit();
        List<Message> messages = fcmMessages.stream().map(FcmService::getMessage).toList();
        BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
        // See the BatchResponse reference documentation
        // for the contents of response.
        log.info("{} fcmMessages were sent successfully", response.getSuccessCount());
        fcmRepository.saveAll(fcmMessages.stream().map(FcmMessage::from).toList());
        return true;
    }

    private static Message getMessage(FcmMessageDto fcmMessageDto) {
        Notification notification = Notification.builder().setTitle(fcmMessageDto.getTitle()).setBody(fcmMessageDto.getMessage())
            .setImage(fcmMessageDto.getImage()).build();
        return Message.builder().setNotification(notification).setToken(fcmMessageDto.getToken()).build();
    }


}
