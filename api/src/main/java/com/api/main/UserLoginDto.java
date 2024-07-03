package com.api.main;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "user login DTO")
public class UserLoginDto {
	
	@Schema(description = "유저 아이디", required = true, defaultValue = "test")
    private String userId;
	
    @Schema(description = "유저 비밀번호", required = true, defaultValue = "123456")
    private String password;


}