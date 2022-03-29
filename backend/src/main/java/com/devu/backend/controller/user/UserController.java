package com.devu.backend.controller.user;

import com.devu.backend.common.exception.EmailConfirmNotCompleteException;
import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.entity.User;
import com.devu.backend.service.UserService;
import com.devu.backend.service.email.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController("/")
public class UserController {
    private final UserService userService;
    private final EmailServiceImpl emailService;


    //회원가입 Form에서 이메일 검증 api => Form Data로 넘어와야함
    @PostMapping("/key")
    private ResponseEntity<?> getKeyFromUser(@RequestParam String postKey) {
        User user = userService.getByAuthKey(postKey);
        if (user == null) {
            return ResponseEntity.badRequest().body("Wrong AuthKey from User");
        }
        user.setEmailConfirm(true);
        userService.createUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/email")
    private ResponseEntity<?> sendEmail(@RequestParam String email) {
        try{
            //email로 인증번호 먼저 보내고, 인증번호 get
            String authKey = emailService.sendValidationMail(email);
            log.info("Email authKey = {}",authKey);
            User user = User.builder()
                    .email(email)
                    .emailAuthKey(authKey)
                    .emailConfirm(false)
                    .build();
            userService.createUser(user);
            return ResponseEntity.ok().body("이메일 전송 완료");
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }

    }


    //회원가입 => 기본 EmailConfirm = false
    //Json으로 넘어와야함
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userCreateRequestDto) {
        try {
            User user = userService.getByEmail(userCreateRequestDto.getEmail());
            user.setUsername(userCreateRequestDto.getUsername());
            user.setPassword(userCreateRequestDto.getPassword());
            userService.createUser(user);
            UserDTO userDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .build();
            return ResponseEntity.ok().body(userDTO);

        } catch (Exception e) {
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //Content-Type : JSON
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword());

            UserDTO responseUserDTO = UserDTO.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            log.warn(e.getMessage());
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

}
