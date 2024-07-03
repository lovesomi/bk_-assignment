package com.api.main;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "user sign DTO")
public class UserDto extends UserLoginDto {
	
    

    @Schema(description = "사용자 이름", required = true, defaultValue = "치킨")
    private String name; 
    
    @Schema(description = "YYMMDD-gabcdef\n"
    		+ "YY: 출생 연도의 끝 두 자리\n"
    		+ "MM: 출생 월\n"
    		+ "DD: 출생 일\n"
    		+ "G: 성별\n"
    		+ "abcdef: 개인을 식별하는 일련의 숫자", required = true, defaultValue = "999999-111111")
    private String regNo;
   
}