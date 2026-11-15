# Android Kotlin 호스트 — 미빌드·미검증

이 프로젝트는 테스트된 웹 시제품을 Android WebView 안에서 구동하는 **하이브리드 앱 호스트 소스**입니다. Kotlin 네이티브 UI 앱이나 Pleos SDK 연동 완료 앱이 아닙니다.

현재 작업 환경에 Android SDK, sdkmanager, Gradle이 없어 빌드와 Android 실행 테스트를 수행하지 못했습니다. APK를 제공하지 않습니다.

로컬 준비: JDK 17, Android SDK Platform 35, Gradle 8.9. Android Studio에서 이 폴더를 열어 Gradle 동기화하세요. Wrapper는 배포본에 포함되지 않았으므로 설치된 Gradle로 `gradle wrapper --gradle-version 8.9`를 먼저 실행할 수 있습니다.

디버그 빌드: `gradle :app:assembleDebug`. release는 Android Studio의 Generate Signed Bundle / APK에서 본인 키로 서명 후 기기에서 검증해야 합니다. 키를 저장소에 올리지 마세요. 빌드 결과가 존재한다고 가정하거나 unsigned/debug APK를 공식 release 제출물로 표기하지 마세요.

웹 자산은 `../../dist`를 참조합니다. 전체 ZIP의 폴더 구조를 유지하세요. 인터넷·차량 권한과 JS-native bridge는 사용하지 않습니다. JSON 백업 다운로드는 Android 호스트 미구현으로 웹 브라우저에서만 이용하세요. AAOS 런처/앱 심사 요건, Gleo Interface, 주행 제한 API, Pleos 에뮬레이터는 모두 미검증입니다.
