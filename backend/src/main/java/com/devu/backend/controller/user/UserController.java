package com.devu.backend.controller.user;

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
    private static String authKey;// 두 가지 메서드에서 동시에 사용해야하기에 class 변수로 선언!


    //회원가입 Form에서 이메일 검증 api
    @PostMapping("/key")
    private ResponseEntity<?> getKeyFromUser(@RequestParam String postKey) {
        if (!postKey.equals(authKey)) {
            ResponseErrorDto errorDto = new ResponseErrorDto("Email key is not same.");
            return ResponseEntity.badRequest().body(errorDto);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }


    //회원가입 => 기본 EmailConfirm = false
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userCreateRequestDto) {
        try {
            //email로 인증번호 먼저 보내고, 인증번호 get
            String to = userCreateRequestDto.getEmail();

            authKey = emailService.sendValidationMail(to);
            log.info("Email authKey = {}",authKey);

            User user = User.builder()
                    .email(userCreateRequestDto.getEmail())
                    .username(userCreateRequestDto.getUsername())
                    .password(userCreateRequestDto.getPassword())
                    .emailConfirm(false)
                    .emailAuthKey(authKey)
                    .build();

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

    /*@PostMapping("/sigin")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {

    }*/

}
