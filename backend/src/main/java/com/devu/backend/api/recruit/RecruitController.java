package com.devu.backend.api.recruit;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruit")
public class RecruitController {

    private final RecruitService recruitService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRecruit(@PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(recruitService.getAllRecruit(pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/naver")
    public ResponseEntity<?> getNaver(@PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(recruitService.getNaver(pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> getKakao(@PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(recruitService.getKakao(pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/line")
    public ResponseEntity<?> getLine(@PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(recruitService.getLine(pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/coupang")
    public ResponseEntity<?> getCoupang(@PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(recruitService.getCoupang(pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/baemin")
    public ResponseEntity<?> getBaemin(@PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(recruitService.getBaemin(pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    //수동으로 db에 삽입(나중에 삭제하고 Scheduled으로 변경)
    @GetMapping("/db")
    public void toDb() {
        recruitService.collectAllRecruit();
    }
}
