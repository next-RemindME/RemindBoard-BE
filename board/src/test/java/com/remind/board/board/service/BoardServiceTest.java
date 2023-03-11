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
        .boardName("test공유보드추가1")
        .boardOrders("AAA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());

    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //then
    assertEquals("test공유보드추가1", findBoard.getBoardName());
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
        .boardName("test카드목록없이_보드객체단일추가")
        .boardOrders("ADA")
        .shareYn(false)
        .build());

    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //ten
    assertEquals("ADA", findBoard.getBoardOrders());
    assertEquals("test카드목록없이_보드객체단일추가", findBoard.getBoardName());
    assertEquals(false, findBoard.isShareYn());
    assertEquals(null, findBoard.getBoardCards());
  }

  @Test
  public void UPDATE_BOARD_SUCCESS() throws Exception {

    //given
    memberSignUp();
    String jwt = memberSignIn();

    Board addedBoard = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test카드목록없이_보드객체단일추가")
        .boardOrders("ADA")
        .shareYn(false)
        .build());
    Board findBoard = boardRepository.findById(addedBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //when
    boardService.updateBoard(jwt,
        UpdateUnitBoardForm.builder()
            .boardId(findBoard.getId())
            .boardName("수정보드명")
            .boardOrders("AAA")
            .shareYn(true)
            .build());
    Board updatedBoard = boardRepository.findById(findBoard.getId())
        .orElseThrow(() -> new BoardException(BoardErrorCode.NOT_EXIST_BOARD));

    //then
    assertEquals("수정보드명", updatedBoard.getBoardName());
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
        .boardName("test공유보드추가1")
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
        .boardName("test공유보드추가1")
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
        .boardName("test공유보드추가3번째순서")
        .boardOrders("ACA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());
    Board addedBoardFirst = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test공유보드추가1번째순서")
        .boardOrders("AAA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());
    Board addedBoardSecond = boardService.addBoard(jwt, AddBoardForm.builder()
        .boardName("test공유보드추가2번째순서")
        .boardOrders("ABA")
        .boardCards(addBoardCardForms)
        .shareYn(true)
        .build());

    //when
    List<BoardDto> selectMembersBoardsResult = boardService.showMembersBoards(jwt);

    //BoardDto 내의 Set<BoardCardDto> boardCards(=정렬된 set)을 arrayList로 변환해서
    //인덱스로 값을 꺼내서 boardCard 순서가 제대로 정렬되었는지 확인합니다.
    Set<BoardCardDto> cardsSet = selectMembersBoardsResult.get(0).getBoardCards();
    List<BoardCardDto> convertCardsListOrdered = new ArrayList<>(cardsSet);

    //then
    assertEquals(3, selectMembersBoardsResult.size());
    assertEquals(4, selectMembersBoardsResult.get(0).getBoardCards().size());
    assertEquals("test공유보드추가1번째순서", selectMembersBoardsResult.get(0).getBoardName());
    assertEquals("test공유보드추가2번째순서", selectMembersBoardsResult.get(1).getBoardName());
    assertEquals("test공유보드추가3번째순서", selectMembersBoardsResult.get(2).getBoardName());
    assertEquals("카드의정렬1번객체", convertCardsListOrdered.get(0).getDescription());
    assertEquals("카드의정렬2번객체", convertCardsListOrdered.get(1).getDescription());
    assertEquals("카드의정렬3번객체", convertCardsListOrdered.get(2).getDescription());
    assertEquals("카드의정렬4번객체", convertCardsListOrdered.get(3).getDescription());
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