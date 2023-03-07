package com.remind.board.member.service;

import com.remind.board.member.domain.dto.SignUpForm;
import com.remind.board.member.domain.dto.SignUpResponse;
import com.remind.board.member.domain.entity.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberSignService extends UserDetailsService {

  SignUpResponse memberSignUp(SignUpForm form);

  Member getMemberByEmail(String email);

}
