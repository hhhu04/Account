간략한 명세   https://docs.google.com/spreadsheets/d/1fJJUAeLgherpyKE2881T-3-BjgjsISJMV9hHPTEoXO0/edit?usp=sharing 

도커 
docker run -i -t -p 8080:8080 hhhu04/account_book


postman import https://drive.google.com/file/d/1JuUGJbWISXBAAraqmr3ozRsDpbdDZw4g/view?usp=sharing


user int = // 1:성공 -1:아디(email)중복 -2:아디없음/비번틀림 -3그 밖의 에러  0 : 형식 틀림

account int =  // 1:성공 -1:로그인안됨/토큰만료됨  -2:다른사람거임  -3: 에러  0 : 형식 틀림

1. /user/join
2. /user/login 
3. /user/testApi 
4. /account/new 
5. /user/myAccount/{email}
6. /user/myDelete/{email}
7. /account/deleteOrBack
8. /account/realDelete
9. /user/myAccount/{email}/{account_id}
10. /user/logout
