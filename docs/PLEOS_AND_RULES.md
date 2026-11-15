# 공식 안내 대조 및 Pleos 연동 범위

확인일: 2026-11-15. 사용자 제공 기획서 `파츠세이버_공모전_기획서_강민구_단독수정본.docx`와 공식 참가신청서의 현행 내용을 먼저 읽고 앱명·8종·점검/교체 구분을 유지했습니다. 개인 연락처는 이 배포물에서 제외했습니다.

## 제출 인정 범위

출처: [지원·제출 방법](https://appdevchallenge.auto-hmg.io/guide/submission), [FAQ](https://appdevchallenge.auto-hmg.io/faq).

| 제출물 | 공식 확인 | 이번 결과 |
|---|---|---|
| 접수양식 | 작성·서명한 신청서/동의서를 이메일 제출 | 원본은 변경·제출하지 않음 |
| 시연 영상 | 목업·프로토타입·화면 녹화 인정, 선택 제출 | 실제 웹 화면 48.1초 MP4 |
| GitHub | 소스와 APK 함께, APK는 apk/ 또는 Releases | 업로드 가능한 소스 ZIP; GitHub 게시 미실행 |
| APK | 실행 가능한 release, 팀명_앱이름_release.apk 형식 안내 | 미제공; 소스+APK 단계 충족 주장 불가 |
| ZIP 다운로드 링크 | 저장소 공개가 어려운 경우 소스+APK 압축 링크 대안 | 웹 소스만인 현재 ZIP은 이 대안의 완전 충족을 보장하지 않음 |
| 숏폼 | 본상 가산점 없음, 별도 행사 | 이번 가로형 영상은 개발 시연용 |

가산점의 구체 배점이나 웹 전용 ZIP의 독립 인정 수준은 확인한 공개 안내에 명시되지 않아 점수를 보장하지 않습니다. 목업 영상도 인정되지만 이 결과물은 입력과 계산이 작동하는 웹 시제품입니다.

접수 이메일: appdevchallenge@hyundai-partners.com. 기한은 11월 15일 23:59(KST). 비공개 저장소는 11월 16일까지 공개 전환하고 11월 23일까지 공개 유지하며, 접수 메일에 URL과 최종 commit hash를 적도록 안내합니다. 신청서/동의서와 전체 자료를 갖춰 사용자가 제출해야 하며 이 작업은 이메일을 발송하지 않았습니다. 공개 앱 소개·영상·코드에는 신청자 개인정보를 넣지 마세요.

## SDK·권한 확인

공식 출처:
- [Vehicle 소개](https://document.pleos.ai/docs/pleos-only/connect-sdk-pleos/Vehicle/intro)
- [SDK 가져오기](https://document.pleos.ai/docs/pleos-only/connect-sdk-pleos/Vehicle/project-setup)
- [권한 선언](https://document.pleos.ai/docs/pleos-only/connect-sdk-pleos/Vehicle/vehicle-permission)
- [Virtual Console](https://document.pleos.ai/docs/pleos-only/app-test-guide/virtual-console-guide)

공식 SDK 가져오기 예제는 Maven `https://nexus-playground.pleos.ai/repository/maven-releases/`와 `ai.pleos.playground:Vehicle:2.3.2`를 안내합니다. 이 시제품에서 다운로드·통합·API 호출은 수행하지 않았습니다.

| 도메인 | 문서상 용도 | 권한 페이지의 대표 조회 권한 |
|---|---|---|
| CarInfo | 차량 기본 정보, 제조사·연식 등 | pleos.car.permission.CAR_INFO |
| Odometer | 총 누적 거리, 트립 거리/시간, 거리 단위 | pleos.car.permission.CAR_DRIVE |
| Tire | 타이어 상태 | pleos.car.permission.CAR_SAFETY |

대표 권한은 개별 함수 권한과 같다고 단정할 수 없습니다. 공식 권한 페이지도 각 함수의 @RequiresPermission을 확인하도록 명시합니다. 샘플 가이드에는 CAR_MILEAGE 같은 세부 권한도 나타나므로 실제로 채택한 SDK 버전과 함수 어노테이션을 대조해야 합니다. 개별 Odometer 함수명·세부 권한의 실호출 검증은 미완료입니다. 본문을 읽을 수 없는 API 참조는 지원 증거로 확대 해석하지 않았습니다.

Vehicle 객체 initialize()/release(), 도메인 획득, capability 확인, 비동기 onComplete/onFailed 처리 패턴은 공식 소개에서 확인했습니다. Virtual Console 문서에는 0~1,000,000 km 시연용 누적 거리 조절 기능이 안내되어 있으나 이번 영상은 그것을 사용한 영상이 아닙니다.

## 다음 구현 단계

1. SDK 사용 조건과 계정/프로젝트 승인 확인, 실제 배포 버전 고정.
2. `provider.js`의 가상 입력 경계를 Kotlin 차량 어댑터로 연결. 단위 정규화, 출처/시각/오래된 신호 상태를 함께 보관.
3. CarInfo/Odometer의 해당 함수 capability와 읽기 권한 검사 후 조회·구독. 권한 거부/미지원/신호 중단 시 수동 입력과 출처 표시.
4. 정상 종료/화면 생명주기에 맞춰 콜백 해제, 차량 서비스 release. 차량 제어 API는 목적상 필요하지 않음.
5. 실차 주행 제한 신호와 공식 앱 UX 요구를 적용. 수동 시연 스위치를 안전 기능으로 출시하지 않음.
6. 제조사 정비표를 차종·연식·엔진·가혹 조건별로 검증해 기준 DB 구축.
7. Pleos Emulator와 Virtual Console에서 권한 실패, 단위 변환, 신호 끊김, 거리 변경을 시험 후 실제 APK를 서명·검증.

타이어 마모, 브레이크 패드 잔량, 배터리 잔여 수명은 확인된 SDK 측정값으로 취급하지 않습니다. Tire 상태 조회 가능성과 마모 측정 가능성은 별개의 사실입니다.
