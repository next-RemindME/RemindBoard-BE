package com.remind.board.board.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.remind.board.board.domain.dto.AddBoardCardForm;
import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.AddUnitBoardCardForm;
import com.remind.board.board.domain.dto.UpdateUnitBoardCardForm;
import com.remind.board.board.domain.entity.Board;
import com.remind.board.board.domain.entity.BoardCard;
import com.remind.board.board.domain.repository.BoardCardRepository;
import com.remind.board.board.domain.repository.BoardRepository;
import com.remind.board.common.exception.customexception.BoardException;
import com.remind.board.common.exception.type.BoardErrorCode;
import com.remind.board.common.security.TokenProvider;
import com.remind.board.member.application.MemberSignApplication;
import com.remind.board.member.domain.dto.SignInForm;
import com.remind.board.member.domain.dto.SignUpForm;
import com.remind.board.member.domain.dto.SignUpResponse;
import com.remind.board.member.service.MemberSignService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
class BoardCardServiceTest {

  @Autowired
  private BoardCardRepository boardCardRepository;

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private BoardCardService boardCardService;

  @Autowired
  private BoardService boardService;

  @Autowired
  private MemberSignService memberSignService;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private MemberSignApplication memberSignApplication;

  @Test
  public void ADD_BOARD_CARD_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    Board addedBoard = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test카드목록없이_보드객체단일추가")
        .boardOrders("AGA")
        .shareYn(false)
        .build());
    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //when
    BoardCard boardCard = boardCardService.addBoardCard(jwt, AddUnitBoardCardForm.builder()
            .boardId(findBoard.getId())
            .cardOrders("AAA")
            .description("TEST_CARD_ADD")
            .url("http://localhost:8080")
        .build());
    BoardCard findBoardCard = boardCardRepository.findById(boardCard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_CARD));

    //then
    assertEquals("AAA", findBoardCard.getCardOrders());
    assertEquals("TEST_CARD_ADD", findBoardCard.getDescription());
    assertEquals("http://localhost:8080", findBoardCard.getUrl());
  }

  @Test
  public void UPDATE_BOARD_CARD_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    Board addedBoard1 = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test카드목록없이_보드객체단일추가")
        .boardOrders("AGA")
        .shareYn(false)
        .build());
    Board findBoard1 = boardRepository.findById(addedBoard1.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    Board addedBoard2 = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test카드목록없이_보드객체단일추가")
        .boardOrders("AGA")
        .shareYn(false)
        .build());
    Board findBoard2 = boardRepository.findById(addedBoard2.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    BoardCard boardCard = boardCardService.addBoardCard(jwt, AddUnitBoardCardForm.builder()
        .boardId(findBoard1.getId())
        .cardOrders("AAA")
        .description("TEST_CARD_ADD")
        .url("http://localhost:8080")
        .build());
    BoardCard findBoardCard = boardCardRepository.findById(boardCard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_CARD));

    //when
    boardCardService.updateBoardCard(jwt, UpdateUnitBoardCardForm.builder()
            .boardId(findBoard2.getId())
            .boardCardId(findBoardCard.getId())
            .cardOrders("ACA")
            .description("UPDATE_TEST_CARD")
            .url("http://localhost:3000")
        .build());
    BoardCard updatedBoardCard = boardCardRepository.findById(findBoardCard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_CARD));

    //then
    assertEquals(findBoard2.getId(), updatedBoardCard.getBoard().getId());
    assertEquals("ACA", updatedBoardCard.getCardOrders());
    assertEquals("http://localhost:3000", updatedBoardCard.getUrl());
  }

  @Test
  public void DELETE_BOARD_CARD_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    Board addedBoard = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test카드목록없이_보드객체단일추가")
        .boardOrders("AGA")
        .shareYn(false)
        .build());
    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    BoardCard boardCard1 = boardCardService.addBoardCard(jwt, AddUnitBoardCardForm.builder()
        .boardId(findBoard.getId())
        .cardOrders("AAA")
        .description("TEST_CARD_ADD")
        .url("http://localhost:8080")
        .build());
    BoardCard findBoardCard1 = boardCardRepository.findById(boardCard1.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_CARD));

    BoardCard boardCard2 = boardCardService.addBoardCard(jwt, AddUnitBoardCardForm.builder()
        .boardId(findBoard.getId())
        .cardOrders("ABA")
        .description("TEST_CARD_ADD")
        .url("http://localhost:3000")
        .build());
    BoardCard findBoardCard2 = boardCardRepository.findById(boardCard2.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_CARD));

    //when
    boardCardService.deleteBoardCard(jwt, findBoardCard1.getId());
    List<BoardCard> boardCards = boardCardRepository.findAllByBoard_IdOrderByCardOrdersAsc(findBoard.getId());

    //then
    assertEquals(1, boardCards.size());
    assertEquals("ABA", boardCards.get(0).getCardOrders());
    assertEquals("TEST_CARD_ADD", boardCards.get(0).getDescription());
    assertEquals("http://localhost:3000", boardCards.get(0).getUrl());
  }

  private List<AddBoardCardForm> makeBoardCard() {

    AddBoardCardForm addBoardCardForm1 = AddBoardCardForm.builder()
        .cardOrders("AAA")
        .description("카드의정렬1번객체")
        .url("http://localhost:8081")
        .build();

    AddBoardCardForm addBoardCardForm2 = AddBoardCardForm.builder()
        .cardOrders("ABA")
        .description("카드의정렬2번객체")
        .url("http://localhost:8082")
        .build();

    AddBoardCardForm addBoardCardForm3 = AddBoardCardForm.builder()
        .cardOrders("ACA")
        .description("카드의정렬3번객체")
        .url("http://localhost:8083")
        .build();

    AddBoardCardForm addBoardCardForm4 = AddBoardCardForm.builder()
        .cardOrders("ADA")
        .description("카드의정렬4번객체")
        .url("http://localhost:8084")
        .build();
    return Arrays.asList(addBoardCardForm3, addBoardCardForm1, addBoardCardForm4, addBoardCardForm2);
  }

  private String memberSignIn() {

    SignInForm loginForm = SignInForm.builder()
        .email("boardmember@google.com")
        .password("1234")
        .build();

    String jwt = memberSignApplication.memberSignIn(loginForm);
    return jwt;
  }

  private void memberSignUp() {
    SignUpForm form = makeSignUpForm();
    SignUpResponse response = memberSignService.memberSignUp(form);
  }

  private SignUpForm makeSignUpForm() {
    return SignUpForm.builder()
        .email("boardmember@google.com")
        .password("1234")
        .nickname("firstmember")
        .build();
  }

}