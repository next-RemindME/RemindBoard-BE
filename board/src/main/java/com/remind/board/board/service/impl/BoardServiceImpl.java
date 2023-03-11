package com.remind.board.board.service.impl;

import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.BoardDto;
import com.remind.board.board.domain.dto.UpdateUnitBoardForm;
import com.remind.board.board.domain.entity.Board;
import com.remind.board.board.domain.repository.BoardRepository;
import com.remind.board.board.service.BoardService;
import com.remind.board.common.exception.customexception.BoardException;
import com.remind.board.common.exception.customexception.MemberException;
import com.remind.board.common.exception.type.BoardErrorCode;
import com.remind.board.common.exception.type.MemberErrorCode;
import com.remind.board.common.security.TokenProvider;
import com.remind.board.member.domain.entity.Member;
import com.remind.board.member.domain.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public List<BoardDto> showSharedBoards() {
    return boardRepository.showSharedBoard();
  }

  @Transactional
  @Override
  public Board addBoard(String refinedToken, AddBoardForm form) {

    Member member = getMemberByToken(refinedToken);
    return boardRepository.save(Board.of(member, form));
  }

  @Transactional
  @Override
  public Board updateBoard(String refinedToken, UpdateUnitBoardForm form) {

    Member member = getMemberByToken(refinedToken);
    Board board = getBoardById(form.getBoardId());
    validateWriter(member, board);

    return board.updateBoard(form);
  }

  @Transactional
  @Override
  public void deleteBoard(String refinedToken, Long boardId) {

    Member member = getMemberByToken(refinedToken);
    Board board = getBoardById(boardId);
    validateWriter(member, board);

    boardRepository.delete(board);
  }

  private void validateWriter(Member member, Board board) {

    //로그인한 회원의 id와 board의 작성 회원 id가 동일하지 않을 시, 예외처리합니다.
    if (!member.getId().equals(board.getMember().getId())) {
      throw new BoardException(BoardErrorCode.NOT_ACCEPT_REWRITE);
    }
  }

  private Board getBoardById(Long boardId) {

    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));
    return board;
  }

  private Member getMemberByToken(String refinedToken) {

    String email = tokenProvider.getUsername(refinedToken);
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_EXISTS_MEMBER));

    return member;
  }
}
