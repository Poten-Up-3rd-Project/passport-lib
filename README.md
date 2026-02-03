# Passport Library

[![](https://jitpack.io/v/Poten-Up-3rd-Project/passport-lib.svg)](https://jitpack.io/#Poten-Up-3rd-Project/passport-lib)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-supported-green)
![Java](https://img.shields.io/badge/Java-17-blue)

스프링 애플리케이션에서 JWT 기반의 경량 인증 토큰(Passport)을 발급·검증하고, 요청 컨텍스트/보안 컨텍스트에 쉽게 연동할 수 있도록 제공하는 멀티 모듈 라이브러리입니다.

<br>

## 모듈 구성

- `passport-core`: JWT 인코딩/검증, 클레임 스펙, 컨텍스트 유틸리티
- `passport-bean`: Spring Boot 자동 구성(키/프로퍼티/필터). 서블릿 필터(`PassportFilter`)로 `X-Passport` 헤더 검증 및 `PassportContext` 저장, MDC(traceId) 설정. Filter 빈은 자동 등록됩니다.
- `passport-authorization`: 보안 미사용(MVC) 환경용 편의 기능. `@CurrentUserId` 인자 주입, `@RequireRole` AOP 권한 체크.
- `passport-security`: Spring Security 연동. `PassportAuthenticationFilter`/`AuthenticationEntryPoint` 빈을 자동 구성합니다(체인 적용은 소비자 설정에서 수행).

<br>

## 요구 사항

- Java 17+
- Spring Boot 3.5.x

<br>

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

- 스타터로 간단 적용(core + bean 번들)

```gradle
dependencies {
  implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-starter:{$version}'
  // Spring Security를 사용하는 서비스라면 추가로 보안 모듈을 의존하세요
  // implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-security:{$version}'
}
```

- 개별 모듈만 사용

```gradle
dependencies {
  implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-core:{$version}'
  implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-bean:{$version}'
  // 보안 미사용 서비스
  implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-authorization:{$version}'
  // 보안 사용 서비스(Spring Security)
  implementation 'com.github.Poten-Up-3rd-Project.passport-lib:passport-security:{$version}'
}
```

- 최신 태그(Tags) 또는 사용 가능한 버전은 상단 뱃지 링크(JitPack 페이지)에서 확인하세요.

<br>

## 기본 설정

`application.yml`에 서명 키/만료 시간과(선택) 제외 경로를 설정합니다.

```yaml
passport:
  key:
    secretKey: "your-256bit-secret-key-here" # HMAC 서명용 비밀키(HS256/HS512, 충분한 길이 권장)
    durationMillis: 3600000                    # 토큰 만료(ms)
  filter:
    exclude-paths: []                          # 선택: 필터를 건너뛸 Ant 패턴 목록 (기본값 빈 목록)
```

해당 설정은 `PassportBeanAutoConfiguration`을 통해 `KeyProperties`/`SecretKey`/`PassportFilter` 빈으로 주입·등록됩니다.

<br>

## 동작 개요

- 수신: 서블릿 필터 `PassportFilter`가 요청 헤더 `X-Passport`를 읽어 서명·만료를 검증하고, 성공 시 `PassportContext`에 `PassportClaims`를 바인딩합니다. 또한
  MDC에 `traceId`를 넣습니다.
- 보안 컨텍스트(Spring Security): `PassportAuthenticationFilter`가 `PassportContext` 값을 `Authentication`으로 변환하여
  `SecurityContextHolder`에 설정합니다.

<br>

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

<br>

### 2) 필터 등록 및 보안 연동

- Spring MVC(보안 미사용): `passport-bean`의 `PassportFilter`가 Servlet 필터로 자동 등록되어 `X-Passport`를 검증하고 `PassportContext`를 채웁니다.
- Spring Security 사용: `passport-security`가 필터/엔트리포인트 빈을 제공하며, 다음 순서를 보장해야 합니다.
  - PassportFilter(서블릿 필터) → PassportAuthenticationFilter(보안 체인 내부)

서블릿 필터 순서 보장(FilterRegistrationBean 사용)
```java path=null start=null
@Bean
public FilterRegistrationBean<PassportFilter> passportFilterRegistration(PassportFilter filter) {
    var reg = new FilterRegistrationBean<>(filter);
    reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 10); // SecurityFilterChain(필터 프록시)보다 앞서도록 충분히 높게
    return reg;
}
```

보안 체인에 연동
```java path=null start=null
@Bean
SecurityFilterChain security(HttpSecurity http,
                             PassportAuthenticationFilter passportAuthenticationFilter,
                             AuthenticationEntryPoint passportAuthenticationEntryPoint) throws Exception {
    return http
        .addFilterBefore(passportAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(e -> e.authenticationEntryPoint(passportAuthenticationEntryPoint))
        .build();
}
```

필터 제외가 필요하면 `passport.filter.exclude-paths`에 패턴을 추가하세요.

```yaml path=null start=null
passport:
  filter:
    exclude-paths:
      - /auth/login
      - /auth/refresh
      - /actuator/**
```

<br>

### 3) 컨트롤러/서비스에서 클레임 접근

- 공통: `PassportContext`에서 현재 요청의 클레임을 가져올 수 있습니다.
```java path=null start=null
PassportClaims claims = PassportContext.get(); // 없으면 null
String userId = claims != null ? claims.userId() : null;
```
- 보안 미사용(MVC): `passport-authorization`을 추가하면 `@CurrentUserId`로 주입 가능합니다.
```java path=null start=null
@GetMapping("/me")
public MeResponse me(@CurrentUserId String userId) {
    // ...
}
```
- 보안 사용(Security): 표준 방식으로 인증 정보 접근
```java path=null start=null
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String userId = auth != null ? (String) auth.getPrincipal() : null;
Collection<? extends GrantedAuthority> authorities = auth != null ? auth.getAuthorities() : List.of();
```
- 역할 검사: `@RequireRole`(anyOf 지원)
```java path=null start=null
@RequireRole(value = {"ROLE_INSTRUCTOR"}, anyOf = {"ROLE_ADMIN"})
@PutMapping("/role")
public void updateRole(@CurrentUserId String userId) { /* ... */ }
```

<br>

### 4) 하행(Outbound) 호출 시 헤더 전파

현재 요청의 토큰을 다른 마이크로서비스에 전달하려면 `PassportHeaderProvider`를 사용하세요.

```java
PassportHeaderProvider provider = new DefaultPassportHeaderProvider();
Map<String, String> headers = provider.headers(); // { "X-Passport": "<jwt>" }
```

이 값을 `RestTemplate` 인터셉터나 Feign `RequestInterceptor`에서 활용하면 됩니다.

<br>

## 클레임 스펙

- 헤더: `X-Passport`
- 클레임 키
    - `uid`(사용자 ID)
    - `rol`(권한, 콤마 구분 문자열)
    - `tid`(분산 추적 ID, UUID)

<br>

## 빌드

```bash
./gradlew clean build
```

<br>

## 트러블슈팅

- 401 Unauthorized
  - `passport.key.secretKey`가 발급·검증 서비스 모두 동일한지 확인(길이 충분)
  - 토큰 만료(exp) 확인, 서버 시간 드리프트 점검
  - `X-Passport` 헤더가 백엔드까지 전달되는지 확인(게이트웨이 경로/헤더 재작성 주의)
- 403 Forbidden(@RequireRole)
  - 토큰 `rol` 값과 요구 역할이 일치하는지 확인
  - 접두사 차이 허용(ROLE_ / non-prefixed) — v1.1.x부터 지원
- 필터가 너무 일찍/늦게 동작
  - `passport.filter.exclude-paths`로 제외 범위 조정
- 진단 로그
```properties path=null start=null
logging.level.com.lxp.passport=DEBUG
```

