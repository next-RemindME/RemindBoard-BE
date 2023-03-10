package com.remind.board.board.service.impl;

import com.remind.board.board.domain.dto.AddUnitBoardCardForm;
import com.remind.board.board.domain.entity.Board;
import com.remind.board.board.domain.entity.BoardCard;
import com.remind.board.board.domain.repository.BoardCardRepository;
import com.remind.board.board.domain.repository.BoardRepository;
import com.remind.board.board.service.BoardCardService;
import com.remind.board.common.exception.customexception.BoardException;
import com.remind.board.common.exception.customexception.MemberException;
import com.remind.board.common.exception.type.BoardErrorCode;
import com.remind.board.common.exception.type.MemberErrorCode;
import com.remind.board.common.security.TokenProvider;
import com.remind.board.member.domain.entity.Member;
import com.remind.board.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardCardServiceImpl implements BoardCardService {

  private final BoardRepository boardRepository;
  private final BoardCardRepository boardCardRepository;
  private final MemberRepository memberRepository;
  private final TokenProvider tokenProvider;

  @Override
  public BoardCard addBoardCard(String refinedToken, AddUnitBoardCardForm form) {

    Board board = boardRepository.findById(form.getBoardId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    Member member = getMemberByToken(refinedToken);

    if (!member.getId().equals(board.getMember().getId())) {
      throw new BoardException(BoardErrorCode.NOT_ACCEPT_REWRITE);
    }

    return boardCardRepository.save(BoardCard.of(board, form));
  }

  private Member getMemberByToken(String refinedToken) {

    String email = tokenProvider.getUsername(refinedToken);
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_EXISTS_MEMBER));
    return member;
  }
}
