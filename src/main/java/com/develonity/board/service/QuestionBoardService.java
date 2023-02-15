package com.develonity.board.service;

import com.develonity.board.dto.BoardPage;
import com.develonity.board.dto.QuestionBoardRequest;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.entity.QuestionBoard;
import com.develonity.user.entity.User;
import org.springframework.data.domain.Page;

public interface QuestionBoardService {

  void createBoard(QuestionBoardRequest request, User user);

//  QuestionBoardResponse createBoard(QuestionBoardRequest request,
//      List<MultipartFile> multipartFiles, User user) throws IOException;

  void updateBoard(Long boardId, QuestionBoardRequest request, User user);

  void deleteBoard(Long boardId, User user);

  Page<QuestionBoardResponse> getQuetionBoardPage(User user,
      BoardPage questionBoardPage);

  QuestionBoardResponse getQuestionBoard(Long boardId, User user);

  QuestionBoard getQuestionBoardAndCheck(Long boardId);

  void checkUser(QuestionBoard questionBoard, Long userId);

  int countLike(Long boardId);
}