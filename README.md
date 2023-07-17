# 네이버 api 프로젝트
네이버 검색 API를 활용해 이미지 검색을 할 수 있는 어플리케이션 개발

## 개발 인원
 **유수형**

## 개발 환경
- OS : Window
- IDE : Android Studio Flamingo | 2022.2.1 Patch 1
- SDK : API 33 : Android 9.0(pie)
- 언어 : java

## 사용 기술
- ### retrofit2 (naver api)
1. naver api를 통해 이미지 검색을 위한 retrofit2 구현
2. Constants Class를 통한 json, xml 구분
3. json : GsonConverter 사용 // xml : TikXmlConverter 사용
4. Callback을 통한 비동기 처리

- ### Fragment 
1. NavigationBarView를 통한 메뉴 처리
2. 해당 메뉴를 통한 fragment 전환
3. 각 fragment의 이동에서 hide, show를 통한 화면 전환

- ### FCM
1. firebase에 ojt 프로젝트 등록
2. FCM에서 전송되는 메시지에 페이로드 데이터 입력
3. 페이로드 데이터에 대한 앱 반응

- ### View
1. ListView
2. GridView
3. RecyclerView
4. ViewPager2

## 오류 및 수정
<details><summary> Fragment 초기화 현상 </summary>

- 오류
1. Fragment상에서 navgationbar를 통해 화면을 이동할 때, 화면이 초기화 되면서 재출력 되는 현상
2. fragment를 add하여 화면을 추가하고 화면을 넘길 때, replace로 화면을 대체하여 문제 발생

- 수정
1. 해당 오류를 수정하기 위해 replace로 fragment를 변경하던 코드 수정
2. ex) getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
3. ex) getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();

</details>

<details><summary> FCM 페이로드 데이터 </summary>

- 오류
1. 앱 종료 상태에서 fcm메시지에 대해 onMessageReceived함수 적용 안됨 현상
2. 페이로드 데이터를 입력하더라도 FirebaseMessagingService 클래스 내의 함수가 적용되지 않음

- 수정
1. 메세지가 오면 앱자체가 실행되도록 구현
2. 앱이 실행되고 페이로드 데이터가 있으면 페이로드 데이터에 대한 처리 실행
3. 페이로드 데이터 내에 action(앱이 실행할 화면), keyword(검색 단어)를 넣어 MainActivity에서 처리

</details>

<details><summary> API호출 비동기 </summary>

- 수정 사항
1. 동기로 호출하는 API를 비동기로 처리

- 수정
1. 기존 json, xml데이터를 return하는 형식 변경
2. Callback함수를 통해 해당 API를 호출하고 callback return
3. callback에 대한 성공여부에 따른 데이터 저장

</details>

<details><summary> XML데이터 저장 </summary>

- 수정 사항 및 문제
1. string문자열로 return하던 방식 -> xml자체적으로 return하도록 수정
2. return한 xml데이터를 바로 활용
3. android에서 xml converter에 대한 자체 라이브러리 X

- 수정
1. xml converter에 대한 검색
2. TikXmlConverter 라이브러리 오픈소스 탐색
3. addConverterFactory(TikXmlConverterFactory.create(new TikXml.Builder().exceptionOnUnreadXml(false).build()))
4. 해당 xml데이터에 맞는 Class 생성 (ApiResponseXML.class)

</details>

## 개발 내역
<details><summary>기본 요구</summary>

- 2023.04.17 ~ 2023.04.21 : 프로젝트 요구사항 분석
- 2023.04.24 ~ 2023.04.28 : 프로젝트 요구사항 설계
- 2023.05.02 : 깃 연동 및 초기화면 개발
- 2023.05.03 ~ 2023.05.04 : EditText입력에 대한 Naver API호출, JSON데이터 확인
- 2023.05.08 : json데이터 arraylist로 파싱 후 이미지 주소 화면 출력
- 2023.05.08 : listview 클릭 이벤트 화면 처리
- 2023.05.09 ~ 2023.05.11 : listview 상세 화면 이미지 출력, ImageView 인덱스 부여, 키보드 이벤트
- 2023.05.15 ~ 2023.05.16 : 상세화면 이미지 추가 로딩, 추가된 이미지 링크 데이터 MainActivity로 intent 전송, x버튼을 통해 MainActivity로 이동 시 마지막 이미지 봤던 이미지로 이동
- 2023.05.17 ~ 2023.05.19 : API데이터 저장 방식(JSON, XML) retrofit2으로 구현
- 2023.05.22 ~ 2023.05.23 : 상세 UI개발, 프로젝트 문서 재검토 및 수정
- 2023.05.24 ~ 2023.05.25 : 문서 및 코드 피드백
- 2023.05.25 ~ 2023.05.26 : 코드 및 문서 수정
</details>

<details><summary>2차 고도화</summary>

- 2023.05.30 ~ 2023.05.31 : 프로젝트 2차 고도화 요구사항 분석 및 설계
- 2023.06.01 ~ 2023.06.02 : 프로젝트 2차 고도화 설계서 작성
- 2023.06.05 : 기본 UI(Fragment) 개발
- 2023.06.07 : Fragment상의 검색화면 개발
- 2023.06.08 ~ 2023.06.09 : 홈 화면 recyclerView 개발
- 2023.06.12 : 데이터 전달 구축
- 2023.06.13 ~ 2023.06.14 : gridView 개발 (homefragment의 상세화면)
- 2023.06.15 ~ 2023.06.16 : api데이터 저장 방식 개선
- 2023.06.19 ~ 2023.06.21 : xml형태 api호출 및 저장방식 개선
- 2023.06.22 : 패키지 수정 및 데이터 저장 형태 변경
- 2023.06.23 : 상세 UI 개선
- 2023.06.26 ~ 2023.06.28 : FCM 푸시 및 어플 실행 개발
- 2023.06.29 ~ 2023.06.30 : retrofit 비동기 처리 개발
- 2023.07.03 ~ 2023.07.04 : test 및 피드백 후 코드 수정
</details>
