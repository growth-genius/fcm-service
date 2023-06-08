package com.gg.tgather.fcmservice.modules.fcm.service.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.dto.fcm.FcmMessageDto;
import com.gg.tgather.fcmservice.modules.fcm.service.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class FcmKafkaConsumer {

    private final FcmService fcmService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.fcm-topic.send-single-fcm-topic}")
    public void sendSingleFcm(String kafkaMessage) throws IOException, FirebaseMessagingException {
        FcmMessageDto fcmMessageDto = objectMapper.readValue(kafkaMessage, FcmMessageDto.class);
        fcmService.pushMessage(fcmMessageDto);
    }

    @KafkaListener(topics = "${kafka.fcm-topic.send-multiple-fcm-topic}")
    public void sendMultipleFcm(String kafkaMessage) throws IOException, FirebaseMessagingException {
        List<FcmMessageDto> fcmMessageDtos = objectMapper.readValue(kafkaMessage, new TypeReference<List<FcmMessageDto>>() {});
        fcmService.pushMessages(fcmMessageDtos);
    }

}
