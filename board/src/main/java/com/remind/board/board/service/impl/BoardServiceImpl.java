package com.remind.board.board.service.impl;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.AddBoardResponse;
import com.remind.board.board.domain.dto.BoardDto;
import com.remind.board.board.domain.entity.Board;
import com.remind.board.board.domain.repository.BoardRepository;
import com.remind.board.board.service.BoardService;
import com.remind.board.common.exception.customexception.MemberException;
import com.remind.board.common.exception.type.MemberErrorCode;
import com.remind.board.common.security.TokenProvider;
import com.remind.board.member.domain.entity.Member;
import com.remind.board.member.domain.repository.MemberRepository;
import com.remind.board.member.service.MemberSignService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final TokenProvider tokenProvider;
  private final MemberRepository memberRepository;
  private final BoardRepository boardRepository;

  @Override
  public List<BoardDto> showMembersBoards(String refinedToken) {

    Member member = getMemberByToken(refinedToken);
    return boardRepository.showMembersBoard(member.getId());
  }

  @Override
  public Board addBoard(String refinedToken, AddBoardForm form) {

    Member member = getMemberByToken(refinedToken);
    return boardRepository.save(Board.of(member, form));
  }

  private Member getMemberByToken(String refinedToken) {

    String email = tokenProvider.getUsername(refinedToken);
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_EXISTS_MEMBER));

    return member;
  }
}
