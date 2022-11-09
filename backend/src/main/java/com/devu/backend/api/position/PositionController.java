package com.devu.backend.api.position;

import com.devu.backend.controller.ResponseErrorDto;
import com.devu.backend.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/position")
public class PositionController {

    private final PositionService positionService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPosition(String keyword, @PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(positionService.getAllPosition(keyword, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/naver")
    public ResponseEntity<?> getNaver(String keyword, @PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(positionService.getNaver(keyword, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> getKakao(String keyword, @PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(positionService.getKakao(keyword, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/line")
    public ResponseEntity<?> getLine(String keyword, @PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(positionService.getLine(keyword, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/coupang")
    public ResponseEntity<?> getCoupang(String keyword, @PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(positionService.getCoupang(keyword, pageable));
        } catch (Exception e) {
            e.printStackTrace();
            ResponseErrorDto errorDto = ResponseErrorDto.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorDto);
        }
    }

    @GetMapping("/baemin")
    public ResponseEntity<?> getBaemin(String keyword, @PageableDefault(size = 20) Pageable pageable) {
        try {
            return ResponseEntity.ok(positionService.getBaemin(keyword, pageable));
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
        positionService.collectAllPosition();
    }
}
