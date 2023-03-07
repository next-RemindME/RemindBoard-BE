package com.remind.board.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.remind.board.common.exception.customexception.MemberException;
import com.remind.board.common.security.TokenProvider;
import com.remind.board.member.application.MemberSignApplication;
import com.remind.board.member.domain.dto.SignInForm;
import com.remind.board.member.domain.dto.SignUpForm;
import com.remind.board.member.domain.dto.SignUpResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
class MemberSignServiceTest {

  @Autowired
  private MemberSignService memberSignService;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private MemberSignApplication memberSignApplication;

  @Test
  public void MEMBER_SIGN_UP_SUCCESS() throws Exception {

    //given
    SignUpForm form = makeSignUpForm();

    //when
    SignUpResponse response = memberSignService.memberSignUp(form);

    //then
    assertNotNull(response);
    assertEquals(true, response.isSignUpResult());
    assertEquals(form.getEmail(), response.getEmail());
    assertEquals(form.getNickname(), response.getNickname());
  }

  @Test
  public void DUPLICATE_MEMBER_SIGN_UP_FAIL() throws Exception {

    //given
    SignUpForm form = makeSignUpForm();
    SignUpForm duplicateForm = makeSignUpForm();

    //when
    SignUpResponse response = memberSignService.memberSignUp(form);

    //then
    Exception exception = assertThrows(MemberException.class, () -> memberSignService.memberSignUp(duplicateForm));
  }

  @Test
  public void MEMBER_SIGN_IN_SUCCESS() throws Exception {

    //given
    SignUpForm signUpForm = makeSignUpForm();
    SignUpResponse response = memberSignService.memberSignUp(signUpForm);

    SignInForm loginForm = SignInForm.builder()
        .email(response.getEmail())
        .password(signUpForm.getPassword())
        .build();

    //when
    String jwt = memberSignApplication.memberSignIn(loginForm);

    //then
    assertEquals(tokenProvider.getUsername(jwt), loginForm.getEmail());
    assertEquals(tokenProvider.getKeyRoles(jwt), tokenProvider.ROLE_MEMBER);
    assertEquals(tokenProvider.validateToken(jwt), false); // 유효기간이 현재 시보다 미래인지 확인
  }

  @Test
  public void MEMBER_SIGN_IN_FAIL() throws Exception {

    //given
    SignUpForm signUpForm = makeSignUpForm();
    SignUpResponse response = memberSignService.memberSignUp(signUpForm);

    SignInForm loginForm = SignInForm.builder()
        .email(response.getEmail())
        .password("passworderror")
        .build();

    //when

    //then
    Exception exception = assertThrows(MemberException.class, () -> memberSignApplication.memberSignIn(loginForm));
  }

  private SignUpForm makeSignUpForm() {
    return SignUpForm.builder()
        .email("boardmember@google.com")
        .password("1234")
        .nickname("firstmember")
        .build();
  }

}