package com.edsuuu.list.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    private String message;
    private int statusCode;
}
