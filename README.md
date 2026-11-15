# 파츠세이버 (PartsSaver) for Pleos Connect

> **주행 습관이 브레이크 패드·타이어 수명과 정비 비용에 미치는 영향을 '원화 금액'으로 보여주는 차량용 짠테크·에코 드라이빙 앱**

---

## 📱 앱 소개
* **앱 이름**: 파츠세이버 (PartsSaver)
* **한 줄 소개**: 운전자의 주행 습관이 브레이크 패드·타이어 등 소모품 수명과 정비 비용에 미치는 영향을 실시간 금액으로 환산해, 경제적이고 안전한 운전을 유도하는 차량 맞춤형 짠테크·에코 드라이빙 앱
* **핵심 가치**: 연비 표시나 '안전운전 점수'만으로는 체감하기 어려운 **습관 개선의 실질적 이득(절약 금액)**을 보여주고, 소모품 점검·교체 시점을 미리 알려 필요한 정비를 제때 받도록 돕습니다.

### 주요 기능
| 기능 | 설명 |
| --- | --- |
| 제동 이벤트 기록 | 브레이크 페달이 **미작동 → 작동**으로 바뀔 때 제동 1회 기록 |
| 주행거리 계산 | 주행 시작·종료 시점의 `getTripCumulativeDrivingDistance()` 값 차이로 이번 주행 거리 계산 |
| 급제동 판정 | 속도 신호가 있으면 감속도(≥ 3.0 m/s²)와 페달 작동으로 급제동 기록. **속도 신호가 없으면 페달 작동 횟수만 집계** |
| 예상 절약 금액 | 주행거리와 제동 기록으로 브레이크 패드·타이어 마모 비용을 추정하고, 기준 주행과 비교해 절약 금액 표시 |
| 주행 중 안내 | 급제동이 반복되면 브레이크 아이콘이 주황색으로 바뀌고 상단 배너 표시 (자동 사라짐, 일정 간격 두고 재표시) |
| 주행 종료 리포트 | 기어를 **P(주차)** 로 바꾸면 요약 카드 표시 → '상세 보기'에서 소모품별 절약 금액·에코 포인트 확인 |
| 타이어 공기압 | Get Vehicle Data API로 조회해 리포트에 표시. 권장 공기압과 비교 가능하면 점검 필요 타이어 안내, **데이터가 없으면 '조회 불가'** |

---

## 🎥 시연 영상

> 시연 영상은 추후 첨부 예정입니다.

<!-- 영상 링크 추가 예시
### 기능 시연
[![영상 재생](https://img.youtube.com/vi/영상ID/hqdefault.jpg)](https://youtu.be/영상ID)
-->

---

## 🖥 개발 및 테스트 환경 (Environment & Specs)
공모전 심사 및 검증 시 아래 환경을 권장합니다.

* **개발 환경**: Android Studio (Koala 이상), JDK 17, Kotlin 2.0.21, Jetpack Compose (Material3)
* **SDK 버전**: compileSdk 34 / targetSdk 34 / minSdk 29
* **Pleos Connect 에뮬레이터 버전**: `[사용한 에뮬레이터 버전 입력]`
* **화면 규격 (Display Specs)**:
  * 디스플레이 형태: 가로 와이드 디스플레이 (다크 테마, 세로 스크롤 대응)
  * 해상도 / 밀도: `[사용한 에뮬레이터 해상도 / 밀도 입력]`
* **필수 런타임 권한 (Required Permissions)**:
  * `INTERNET` (타이어 공기압 조회용 Get Vehicle Data API 호출)
  * 차량 데이터 접근 권한은 Pleos Connect SDK 요구사항에 따릅니다.

---

## 🚀 실행 방법 (Getting Started)

### 1. 소스코드 빌드 및 실행
```bash
# 1) 저장소 클론
git clone https://github.com/[YOUR_USERNAME]/[REPO_NAME].git

# 2) local.properties 준비 (아래 '3. 필요한 설정' 참고)
cp local.properties.example local.properties

# 3) release APK 빌드 (Windows)
gradlew assembleRelease

# Mac / Linux
./gradlew assembleRelease
```
빌드 결과물은 `app/build/outputs/apk/release/` 에 생성됩니다.
Android Studio에서 `Build > Build Bundle(s) / APK(s) > Build APK(s)` 로도 빌드할 수 있습니다.

### 2. APK 설치 및 실행
제출용 APK는 저장소의 `apk/` 폴더에 있습니다.
```bash
adb install apk/[팀명]_파츠세이버_release.apk
```
에뮬레이터에 APK를 끌어다 놓아 설치해도 됩니다. 설치 후 **파츠세이버** 아이콘으로 실행합니다.

### 3. 필요한 설정 (`local.properties`)
| 키 | 설명 | 기본값 |
| --- | --- | --- |
| `sdk.dir` | Android SDK 경로 | - |
| `USE_MOCK_VEHICLE` | `true`: 모의 주행 데이터로 동작 / `false`: Pleos Connect SDK 데이터 사용 | `true` |
| `PLEOS_MANAGER_CLASS` | `USE_MOCK_VEHICLE=false` 일 때 사용할 Pleos Connect SDK 차량 정보 매니저 클래스의 전체 이름 | (빈 값) |
| `PLEOS_VIN`, `PLEOS_API_TOKEN` | 타이어 공기압 조회(Get Vehicle Data API)용. 비우면 '조회 불가'로 표시 | (빈 값) |
| `RECOMMENDED_TIRE_KPA` | 차량 권장 공기압(kPa). `0`이면 권장값과 비교하지 않고 측정값만 표시 | `0` |

> ⚠️ `PLEOS_API_TOKEN` 은 빌드된 APK 안에 포함됩니다. 제출·공개용 APK에는 실제 토큰을 넣지 마세요.

### 4. 사용 방법
1. **모의 주행 모드(기본값)**: 앱을 실행하면 약 40초짜리 가상 주행이 시작됩니다. 화면 상단 버튼으로 `정상 주행 / 급제동 반복 / 속도 신호 누락` 시나리오를 전환할 수 있습니다.
2. **Pleos Connect 환경**: `USE_MOCK_VEHICLE=false` 로 설정하면 차량 신호로 동작합니다. 주행 후 기어를 **P(주차)** 로 바꾸면 주행 요약 카드가 표시되고, **상세 보기**에서 소모품별 절약 금액·에코 포인트·타이어 공기압을 확인할 수 있습니다.

### 5. 단위 테스트
```bash
./gradlew test
```
제동 이벤트·급제동 판정, 절약 금액 계산, 공기압 변환·응답 파싱을 검증합니다.

---

## 🗂 프로젝트 구조
```
app/src/main/java/com/partssaver/
├─ domain/   제동 이벤트·급제동 판정(TripTracker), 마모·절약 추정(WearEstimator),
│            배너 정책(BannerPolicy), 공기압 변환·판정(TirePressure)   ← Android 의존성 없는 순수 Kotlin
├─ data/     VehicleDataSource, PleosVehicleSignalReader(SDK 연동), MockVehicleDataSource,
│            TirePressureRepository(Get Vehicle Data API), ConsumableStore
└─ ui/       TripViewModel, Screens(Jetpack Compose)
```

## ℹ️ 참고
* 절약 금액·마모량은 대표 부품 가격/수명 가정(`CostModel`)에 기반한 **예상치**이며, 정밀한 마모 측정값이 아닙니다.
* 속도 신호나 공기압 데이터를 제공하지 않는 환경에서는 해당 항목을 추정하지 않고 '페달 횟수만 집계' 또는 '조회 불가'로 표시합니다.
