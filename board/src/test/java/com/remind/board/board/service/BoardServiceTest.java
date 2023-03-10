package com.remind.board.board.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.remind.board.board.domain.dto.AddBoardCardForm;
import com.remind.board.board.domain.dto.AddBoardForm;
import com.remind.board.board.domain.dto.BoardDto;
import com.remind.board.board.domain.dto.BoardDto.BoardCardDto;
import com.remind.board.board.domain.dto.UpdateUnitBoardForm;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
class BoardServiceTest {

  @Autowired
  private BoardCardRepository boardCardRepository;

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private BoardService boardService;

  @Autowired
  private MemberSignService memberSignService;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private MemberSignApplication memberSignApplication;

  @Test
  public void ADD_BOARD_WITH_CARDS_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();
    List<AddBoardCardForm> addBoardCardForms = makeBoardCard();

    //when
    Board addedBoard = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test??????????????????1")
        .boardOrders("AAA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());

    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //then
    assertEquals("test??????????????????1", findBoard.getBoardName());
    assertEquals(true, findBoard.isShareYn());
    assertEquals("AAA", findBoard.getBoardOrders());
    assertEquals(4, findBoard.getBoardCards().size());
  }

  @Test
  public void ADD_BOARD_WITHOUT_CARDS_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    //when
    Board addedBoard = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test??????????????????_????????????????????????")
        .boardOrders("ADA")
        .shareYn(false)
        .build());

    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //ten
    assertEquals("ADA", findBoard.getBoardOrders());
    assertEquals("test??????????????????_????????????????????????", findBoard.getBoardName());
    assertEquals(false, findBoard.isShareYn());
    assertEquals(null, findBoard.getBoardCards());
  }

  @Test
  public void UPDATE_BOARD_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    Board addedBoard = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test??????????????????_????????????????????????")
        .boardOrders("ADA")
        .shareYn(false)
        .build());
    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //when
    boardService.updateBoard(jwt,
        UpdateUnitBoardForm.builder()
            .boardId(findBoard.getId())
            .boardName("???????????????")
            .boardOrders("AAA")
            .shareYn(true)
            .build());
    Board updatedBoard = boardRepository.findById(findBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //then
    assertEquals("???????????????", updatedBoard.getBoardName());
    assertEquals("AAA", updatedBoard.getBoardOrders());
    assertEquals(true, updatedBoard.isShareYn());
  }

  @Test
  public void DELETE_BOARD_WITH_CARDS_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    List<AddBoardCardForm> addBoardCardForms = makeBoardCard();
    Board addedBoard = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test??????????????????1")
        .boardOrders("AAA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());

    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //when
    boardService.deleteBoard(jwt, findBoard.getId());
    Optional<Board> optionalBoard = boardRepository.findById(findBoard.getId());
    List<BoardCard> boardCardsByDeletedBoardId = boardCardRepository.findAllByBoard_IdOrderByCardOrdersAsc(findBoard.getId());

    //then
    assertEquals(false, optionalBoard.isPresent());
    assertEquals(0, boardCardsByDeletedBoardId.size());
  }

  @Test
  public void DELETE_BOARD_WITHOUT_CARDS_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    Board addedBoard = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test??????????????????1")
        .boardOrders("AAA")
        .shareYn(true)
        .build());
    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //when
    boardService.deleteBoard(jwt, findBoard.getId());
    Optional<Board> optionalBoard = boardRepository.findById(findBoard.getId());

    //then
    assertEquals(false, optionalBoard.isPresent());
  }

  @Test
  public void SHOW_MEMBERS_BOARDS_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    List<AddBoardCardForm> addBoardCardForms = makeBoardCard();
    Board addedBoardThird = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test??????????????????3????????????")
        .boardOrders("ACA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());
    Board addedBoardFirst = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test??????????????????1????????????")
        .boardOrders("AAA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());
    Board addedBoardSecond = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test??????????????????2????????????")
        .boardOrders("ABA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());

    //when
    List<BoardDto> selectMembersBoardsResult = boardService.showMembersBoards(jwt);

    //BoardDto ?????? Set<BoardCardDto> boardCards(=????????? set)??? arrayList??? ????????????
    //???????????? ?????? ????????? boardCard ????????? ????????? ?????????????????? ???????????????.
    Set<BoardCardDto> cardsSet = selectMembersBoardsResult.get(0).getBoardCards();
    List<BoardCardDto> convertCardsListOrdered = new ArrayList<>(cardsSet);

    //then
    assertEquals(3, selectMembersBoardsResult.size());
    assertEquals(4, selectMembersBoardsResult.get(0).getBoardCards().size());
    assertEquals("test??????????????????1????????????", selectMembersBoardsResult.get(0).getBoardName());
    assertEquals("test??????????????????2????????????", selectMembersBoardsResult.get(1).getBoardName());
    assertEquals("test??????????????????3????????????", selectMembersBoardsResult.get(2).getBoardName());
    assertEquals("???????????????1?????????", convertCardsListOrdered.get(0).getDescription());
    assertEquals("???????????????2?????????", convertCardsListOrdered.get(1).getDescription());
    assertEquals("???????????????3?????????", convertCardsListOrdered.get(2).getDescription());
    assertEquals("???????????????4?????????", convertCardsListOrdered.get(3).getDescription());
  }

  private List<AddBoardCardForm> makeBoardCard() {

    AddBoardCardForm addBoardCardForm1 = AddBoardCardForm.builder()
        .cardOrders("AAA")
        .description("???????????????1?????????")
        .url("http://localhost:8081")
        .build();

    AddBoardCardForm addBoardCardForm2 = AddBoardCardForm.builder()
        .cardOrders("ABA")
        .description("???????????????2?????????")
        .url("http://localhost:8082")
        .build();

    AddBoardCardForm addBoardCardForm3 = AddBoardCardForm.builder()
        .cardOrders("ACA")
        .description("???????????????3?????????")
        .url("http://localhost:8083")
        .build();

    AddBoardCardForm addBoardCardForm4 = AddBoardCardForm.builder()
        .cardOrders("ADA")
        .description("???????????????4?????????")
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