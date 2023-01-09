package com.example.pomeserver.global.util.sms.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "네이버에서 원하는 정보를 담기 위한 객체")
public class SmsNaverReq {
    private String type;
    private String from;
    private String Content;
    private List<MessageDTO> messages;
}
