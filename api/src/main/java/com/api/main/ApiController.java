package com.api.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.main.entity.UserEntity;
import com.api.main.service.JwtService;
import com.api.main.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;


@RestController
@RequiredArgsConstructor
@Tag(name = "API", description = "API")
@RequestMapping("/api")
public class ApiController {

	private final UserService userService;

	private final JwtService jwtService;
	

	@PostMapping("/signup")
	@ResponseBody
	@Operation(summary = "회원가입 API", description = "회원가입 API")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success", content = {
			@Content(schema = @Schema(implementation = UserDto.class)) }) })
	public ResponseEntity<?> signup(@RequestBody UserDto userDto) {

		try {

			/* 회원 인가 영역 */
			String authorizedUsers = "{\n" + "  \"치킨\": \"999999-1111111\",\n"
					+ "  \"피자\": \"888888-222222\"\n" + "}";

			ObjectMapper objectMapper = new ObjectMapper();

			Map<String, String> map = objectMapper.readValue(authorizedUsers, Map.class);

			// 불일치 시 인가되지 않은 사용자
			// 사용자이름 검증
			if (map.get(userDto.getName()) == null) {
				return ResponseEntity.badRequest().body("인가되지 않은 사용자");
			} else {
				System.out.println(userDto.getRegNo());
				// 주민등록번호 검증
				if (!map.get(userDto.getName()).equals(userDto.getRegNo())) {
					return ResponseEntity.badRequest().body("인가되지 않은 사용자");
				}
			}
			
			// id 체크
			if (userService.getUserId(userDto) != null) {
				return ResponseEntity.badRequest().body("사용중인 id");
			}
			
			
			/* 회원 인가 영역 */

			/* 회원 정보 저장 */
			// 저장 영역 jpa save 사용 중복 등 로직 필요
			userService.signUp(userDto);
			/* 회원 정보 저장 */

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.toString());
		}

		return ResponseEntity.ok().body("등록되었습니다.");
	}

	@PostMapping("/login")
	@ResponseBody
	@Operation(summary = "login API", description = " API")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success", content = {
			@Content(schema = @Schema(implementation = UserDto.class)) }) })
	public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {

		// user 조회 조회 실패 시 throws
		UserEntity userEntity = userService.login(userLoginDto);

		// accessToken 발급
		Map<String, String> map = new HashMap<>();
		map.put("accessToken", getToken(userEntity));

		return ResponseEntity.ok().body(map);
	}

	// 토큰 발급 토큰에 개인 정보 저장
	// id 등 을 저장하여야하나 주민 등록번호 까지 저장되어 있음
	private String getToken(UserEntity userEntity) {
		Map<String, Object> paramClaim = new HashMap<String, Object>();
		paramClaim.put("userId", userEntity.getUserId());
		paramClaim.put("name", userEntity.getName());
		paramClaim.put("regNo", userEntity.getRegNo());

		return jwtService.create(paramClaim);
	}
	
	@GetMapping("/parsing")
	@ResponseBody
	@Operation(summary = "page content API", description = "API")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success", content = {
			@Content(schema = @Schema(implementation = UserDto.class)) }) })
	public ResponseEntity<?> parsing(@RequestParam(value="url" , defaultValue="https://bringko.com/pc/myp/privacy.php")  String url) throws Exception {

		Map<String, Integer> sortedWordFreq = new LinkedHashMap<>();
		
		try {

			JSONObject jsonObject = new JSONObject();
			String jsonData = jsonObject.toString();

			Connection.Response response = Jsoup.connect(url)
					.timeout(200000) // 20초
					.header("X-API-KEY", "aXC8zK6puHIf9l53L8TiQg==")
					.ignoreContentType(true)
					.header("Content-Type", "application/json")
					.method(Connection.Method.GET) // JSON																							// 전송
					.execute();
			
			ObjectMapper objectMapper = new ObjectMapper();
				
			String text = response.parse().text();
		
			
		    // 단어 빈도수를 저장할 Map
	        Map<String, Integer> wordFreq = new HashMap<>();

	        // StringTokenizer를 사용하여 단어 분리
	        StringTokenizer tokenizer = new StringTokenizer(text, " .,?!;");

	        // 각 단어의 빈도수 계산
	        while (tokenizer.hasMoreTokens()) {
	            String word = tokenizer.nextToken();
	            wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
	        }

	        // 단어 빈도수 기준으로 정렬하기 위해 List에 Map.Entry를 저장
	        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordFreq.entrySet());

	        // 빈도수 기준으로 내림차순 정렬
	        entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

	        // 정렬된 결과를 LinkedHashMap에 저장
	        
	        for (Map.Entry<String, Integer> entry : entryList) {
	            sortedWordFreq.put(entry.getKey(), entry.getValue());
	        }
	        


			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(sortedWordFreq);

		return ResponseEntity.ok().body(sortedWordFreq);
	}


}
