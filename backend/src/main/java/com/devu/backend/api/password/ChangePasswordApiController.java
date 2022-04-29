package com.devu.backend.api.password;

import com.devu.backend.common.exception.UserNotFoundException;
import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.service.EmailService;
import com.devu.backend.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChangePasswordApiController {

    private final UserService userService;
    private final EmailService emailService;

    /*
     * 비밀번호 변경 모달은 무조건 main page로부터 접근해야 한다.
     * referer이 무조건 main페이지 url이어야한다. => 그외는 Exception Throws
     * */
    @PostMapping("/password_url_email")
    ResponseEntity<?> sendEmail(@RequestBody RequestPasswordUrlDto requestPasswordUrlDto) {
        try {
            String referUrl = requestPasswordUrlDto.getReferUrl();
            /*if (!referUrl.equals("http://54.180.29.69/")) {
                throw new IllegalArgumentException("정상적인 접근 경로가 아닙니다.");
            }*/
            if (!userService.isEmailExists(requestPasswordUrlDto.getEmail())) {
                throw new UserNotFoundException();
            }
            emailService.sendPasswordChangeMail(requestPasswordUrlDto.getEmail());
            return ResponseEntity.ok().body("OK");
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    /*
    * Front : email값 받고 해당 이메일 값으로  Post 요청 && 비밀번호 변경 Form 렌더링
    * */
    @GetMapping("/change_password/{email}")
    ResponseEntity<?> getPasswordChangeForm(@PathVariable("email") String email) {
        try {
            return ResponseEntity.ok().body(email);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @PostMapping("/change_password/{email}")
    ResponseEntity<?> passwordChange(
            @PathVariable("email") String email,
            @RequestBody RequestChangePasswordDto requestChangePasswordDto) {
        try {
            userService.changePassword(email,requestChangePasswordDto.getPassword());
            return ResponseEntity.ok().body("비밀번호 변경 완료");
        }catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }


}
