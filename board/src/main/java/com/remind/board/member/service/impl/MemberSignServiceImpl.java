package com.remind.board.member.service.impl;

import static com.remind.board.common.security.TokenProvider.ROLE_MEMBER;

import com.remind.board.common.exception.customexception.MemberException;
import com.remind.board.common.exception.type.MemberErrorCode;
import com.remind.board.member.domain.dto.SignUpForm;
import com.remind.board.member.domain.dto.SignUpResponse;
import com.remind.board.member.domain.entity.Member;
import com.remind.board.member.domain.repository.MemberRepository;
import com.remind.board.member.service.MemberSignService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberSignServiceImpl implements MemberSignService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  @Override
  public SignUpResponse memberSignUp(SignUpForm form) {

    duplicateMailValidation(form.getEmail());

    String encodedPassword = passwordEncoder.encode(form.getPassword());

    Member member = memberRepository.save(Member.of(form, encodedPassword));
    return Member.from(member);
  }

  @Override
  public Member getMemberByEmail(String email) {

    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_EXISTS_MEMBER));
    return member;
  }

  public void duplicateMailValidation(String email) {

    if (memberRepository.existsByEmail(email)) {
      throw new MemberException(MemberErrorCode.ALREADY_EXIST_EMAIL);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("입력한 이메일을 사용하는 회원이 없습니다."));

    List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
    grantedAuthorityList.add(new SimpleGrantedAuthority(ROLE_MEMBER)); //MEMBER 권한(board CRUD)

    return new User(member.getEmail(), member.getPassword(), grantedAuthorityList);
  }
}
