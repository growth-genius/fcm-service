package com.gg.tgather.fcmservice.modules.fcm.controller;

import static com.gg.tgather.commonservice.utils.ApiUtil.success;

import com.gg.tgather.commonservice.annotation.RestBaseAnnotation;
import com.gg.tgather.commonservice.dto.fcm.FcmMessageDto;
import com.gg.tgather.commonservice.utils.ApiUtil.ApiResult;
import com.gg.tgather.fcmservice.modules.fcm.service.FcmService;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestBaseAnnotation
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping
    public ApiResult<Boolean> pushMessage(@RequestBody FcmMessageDto fcmMessageDto) throws IOException, FirebaseMessagingException {
        return success(fcmService.pushMessage(fcmMessageDto));
    }

    @PostMapping("/list")
    public ApiResult<Boolean> pushMessages(@RequestBody List<FcmMessageDto> fcmMessageDtoList) throws IOException, FirebaseMessagingException {
        return success(fcmService.pushMessages(fcmMessageDtoList));
    }

}
