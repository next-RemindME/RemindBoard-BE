package com.remind.board.board.domain.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.remind.board.board.domain.dto.BoardDto;
import com.remind.board.board.domain.dto.QBoardDto;
import com.remind.board.board.domain.dto.QBoardDto_BoardCardDto;
import com.remind.board.board.domain.entity.QBoard;
import com.remind.board.board.domain.entity.QBoardCard;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<BoardDto> showMembersBoard(Long memberId) {

    //board order 후, board card order에 따라 내부 card들을 정렬합니다.
    QBoard board = QBoard.board;
    QBoardCard boardCard = QBoardCard.boardCard;

    Map<Long, BoardDto> resultMap = jpaQueryFactory
        .from(board)
        .join(board.boardCards, boardCard)
        .on(board.member.id.eq(memberId))
        .orderBy(board.boardOrders.asc(), boardCard.cardOrders.asc())
        .transform(
            groupBy(board.id).as(
                new QBoardDto(
                    board.id,
                    board.boardName,
                    board.boardOrders,
                    set(new QBoardDto_BoardCardDto(boardCard.id, boardCard.url,
                        boardCard.description, boardCard.cardOrders)),
                    board.shareYn
                ))
        );

    return resultMap.keySet().stream()
        .map(resultMap::get)
        .collect(Collectors.toList());
  }

  @Override
  public List<BoardDto> showSharedBoard() {

    //board data 변경 순대로 정렬한 후, board card order에 따라 내부 card들을 정렬합니다.
    QBoard board = QBoard.board;
    QBoardCard boardCard = QBoardCard.boardCard;

    Map<Long, BoardDto> resultMap = jpaQueryFactory
        .from(board)
        .join(board.boardCards, boardCard)
        .on(board.shareYn.eq(true))
        .orderBy(board.modifiedAt.desc(), boardCard.cardOrders.asc())
        .transform(
            groupBy(board.id).as(
                new QBoardDto(
                    board.id,
                    board.boardName,
                    board.boardOrders,
                    set(new QBoardDto_BoardCardDto(boardCard.id, boardCard.url,
                        boardCard.description, boardCard.cardOrders)),
                    board.shareYn
                ))
        );

    return resultMap.keySet().stream()
        .map(resultMap::get)
        .collect(Collectors.toList());
  }

}
