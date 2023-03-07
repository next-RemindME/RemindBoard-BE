package com.remind.board.member.controller;

import com.remind.board.member.application.MemberSignApplication;
import com.remind.board.member.domain.dto.SignInForm;
import com.remind.board.member.domain.dto.SignUpForm;
import com.remind.board.member.domain.dto.SignUpResponse;
import com.remind.board.member.service.MemberSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/remind-board/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberSignService memberSignService;
  private final MemberSignApplication memberSignApplication;

  @PostMapping("/sign-up")
  public ResponseEntity<SignUpResponse> memberSignUp(@RequestBody SignUpForm form) {
    return ResponseEntity.ok(memberSignService.memberSignUp(form));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<String> memberSignIn(@RequestBody SignInForm form) {
    return ResponseEntity.ok(memberSignApplication.memberSignIn(form));
  }

}
