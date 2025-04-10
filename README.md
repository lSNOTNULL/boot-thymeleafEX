# 동작 순서
1. 사용자가 웹 브라우저로 접속하면 (/)
2. Controller(MainController)가 요청을 받아 DB에서 단어 목록을 가져와
3. View(index.html + Thymeleaf)에게 전달하여 화면을 그려줍니다.
4. 사용자가 단어 추가/수정/삭제/리셋 버튼을 누르면
5. Controller가 해당 요청을 받아 Repository(WordRepository)를 통해 DB 작업을 하고
6. 다시 첫 화면(/)으로 돌아가 변경된 목록을 보여줍니다.

