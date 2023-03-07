package com.remind.board.member.application;

import static com.remind.board.common.security.TokenProvider.ROLE_MEMBER;

import com.remind.board.common.exception.customexception.MemberException;
import com.remind.board.common.exception.type.MemberErrorCode;
import com.remind.board.common.security.TokenProvider;
import com.remind.board.member.domain.dto.SignInForm;
import com.remind.board.member.domain.entity.Member;
import com.remind.board.member.service.MemberSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSignApplication {

  private final TokenProvider tokenProvider;
  private final MemberSignService memberSignService;
  private final PasswordEncoder passwordEncoder;

  public String memberSignIn(SignInForm form) {

    Member member = memberSignService.getMemberByEmail(form.getEmail());

    if (!this.passwordEncoder.matches(form.getPassword(), member.getPassword())) {
      throw new MemberException(MemberErrorCode.INVALID_LOGIN_INFO);
    }

    return this.tokenProvider.generateToken(form.getEmail(), ROLE_MEMBER);
  }

}
