package com.balita.springexamplecrud.playload;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
public class ApiResponse {
    private Boolean success;
    private HttpStatus status;
    private String message;
    private Object data;

    public ApiResponse(Boolean success, HttpStatus status, String message, Object data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
