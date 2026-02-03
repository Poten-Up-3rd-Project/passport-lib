# Passport Library

[![](https://jitpack.io/v/Poten-Up-3rd-Project/passport-lib.svg)](https://jitpack.io/#Poten-Up-3rd-Project/passport-lib)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-supported-green)
![Java](https://img.shields.io/badge/Java-17-blue)

스프링 애플리케이션에서 JWT 기반의 경량 인증 토큰(Passport)을 발급·검증하고, 요청 컨텍스트/보안 컨텍스트에 쉽게 연동할 수 있도록 제공하는 멀티 모듈 라이브러리입니다.

## 모듈 구성

- `passport-core`: JWT 인코딩/검증, 클레임 스펙, 컨텍스트 유틸리티
- `passport-bean`: Spring Boot 자동 구성(키/프로퍼티), 서블릿 필터(`PassportFilter`)로 `X-Passport` 헤더 검증 및 `PassportContext` 저장, MDC(
  traceId) 설정
- `passport-security`: Spring Security 연동 필터(`PassportAuthenticationFilter`) 및 어댑터

## 요구 사항

- Java 17+
- Spring Boot 3.5.x

## 설치(의존성 추가)

JitPack을 사용합니다. 원하는 방식으로 개별 모듈만 추가하거나, 저장소 전체(모든 모듈 번들)를 추가할 수 있습니다.

1) JitPack 저장소 추가

- Gradle (settings.gradle)

```gradle
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}
```

2) 의존성 선언

- 모든 모듈 한 번에 사용

```gradle
dependencies {
  implementation 'com.github.Poten-Up-3rd-Project:passport-lib:{TAG}'
}
```

- 개별 모듈만 사용

```gradle
dependencies {
  implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-core:{TAG}'
  implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-bean:{TAG}'
  implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-security:{TAG}'
}
```

- 최신 태그(Tags) 또는 사용 가능한 버전은 상단 뱃지 링크(JitPack 페이지)에서 확인하세요.

## 기본 설정

`application.yml`에 서명 키와 만료 시간을 설정합니다.

```yaml
passport:
  key:
    secretKey: "your-256bit-secret-key-here" # HMAC 서명용 비밀키(충분한 길이 권장)
    durationMillis: 3600000                    # 토큰 만료(ms)
```

해당 설정은 `PassportBeanAutoConfiguration`을 통해 `KeyProperties`/`SecretKey` 빈으로 주입됩니다.

## 동작 개요

- 수신: 서블릿 필터 `PassportFilter`가 요청 헤더 `X-Passport`를 읽어 서명·만료를 검증하고, 성공 시 `PassportContext`에 `PassportClaims`를 바인딩합니다. 또한
  MDC에 `traceId`를 넣습니다.
- 보안 컨텍스트(Spring Security): `PassportAuthenticationFilter`가 `PassportContext` 값을 `Authentication`으로 변환하여
  `SecurityContextHolder`에 설정합니다.

## 사용법

### 1) 토큰 발급(서버 사이드)

```java
KeyProperties props; // @Autowired로 주입 받기
PassportEncoder encoder = new PassportEncoder(props);

PassportClaims claims = new PassportClaims(
    "user-123",                       // userId
    List.of("ROLE_USER", "ROLE_ADMIN"), // roles(문자열 리스트)
    UUID.randomUUID().toString(),      // traceId
    null                               // rawToken(발급 시에는 null)
);
String jwt = encoder.encode(claims);   // 서명된 JWT 문자열
```

발급한 JWT는 호출자에게 `X-Passport` 헤더로 전달하세요.

### 2) 필터 등록 및 보안 연동

- Spring MVC만 사용하는 경우: `passport-bean` 모듈의 `PassportFilter`가 `@Component`로 자동 등록됩니다.
- Spring Security를 사용하는 경우: `passport-security`의 필터를 체인에 추가합니다.

```java

@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http,
                                        PassportAuthenticationFilter passportAuthenticationFilter) throws Exception {
    return http
        // ... 기존 보안 설정
        .addFilterBefore(passportAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}
```

### 3) 컨트롤러/서비스에서 클레임 접근

`PassportContext`에서 현재 요청의 클레임을 꺼낼 수 있습니다.

```java
PassportClaims claims = PassportContext.get(); // 없으면 null 반환
String userId = claims != null ? claims.userId() : null;
```

Spring Security를 쓴다면 표준 방식으로 인증 정보에 접근 가능합니다.

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String userId = auth != null ? (String) auth.getPrincipal() : null;
Collection<? extends GrantedAuthority> authorities = auth != null ? auth.getAuthorities() : List.of();
```

### 4) 하행(Outbound) 호출 시 헤더 전파

현재 요청의 토큰을 다른 마이크로서비스에 전달하려면 `PassportHeaderProvider`를 사용하세요.

```java
PassportHeaderProvider provider = new DefaultPassportHeaderProvider();
Map<String, String> headers = provider.headers(); // { "X-Passport": "<jwt>" }
```

이 값을 `RestTemplate` 인터셉터나 Feign `RequestInterceptor`에서 활용하면 됩니다.

## 클레임 스펙

- 헤더: `X-Passport`
- 클레임 키
    - `uid`(사용자 ID)
    - `rol`(권한, 콤마 구분 문자열)
    - `tid`(분산 추적 ID, UUID)

## 빌드

```bash
./gradlew clean build
```

## 트러블슈팅

- 401 Unauthorized가 발생한다면
    - `passport.key.secretKey`가 충분한 길이인지 확인하세요(HMAC-SHA 키는 256bit 이상 권장)
    - `durationMillis` 만료 여부 확인
    - `X-Passport` 헤더가 누락되었는지 확인
- 보안 컨텍스트가 비어 있다면(Spring Security)
    - `PassportAuthenticationFilter`가 체인에 추가되었는지 확인

