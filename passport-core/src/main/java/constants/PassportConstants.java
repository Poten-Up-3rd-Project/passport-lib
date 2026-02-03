package constants;

public interface PassportConstants {
    // 헤더
    String PASSPORT_HEADER_NAME = "X-Passport";

    // JWT Claims
    /**
     * 사용자 ID 클레임 키
     * <p>
     * 사용자의 고유한 ID를 저장합니다.
     * <p>
     * Example: "uid": "user-123"
     */
    String PASSPORT_USER_ID = "uid";

    /**
     * 사용자 역할(권한) 클레임 키
     * <p>
     * 사용자의 역할을 쉼표로 구분된 문자열로 저장합니다.
     * 절대로 배열이나 리스트 형태로 저장하면 안 됩니다.
     * <p>
     * Example: "rol": "ROLE_USER,ROLE_ADMIN"
     */
    String PASSPORT_ROLE = "rol";

    /**
     * 분산 추적 ID 클레임 키
     * <p>
     * 요청의 분산 추적을 위한 고유 ID입니다.
     * UUID 형식이어야 합니다.
     * <p>
     * Example: "tid": "550e8400-e29b-41d4-a716-446655440000"
     */
    String PASSPORT_TRACE_ID = "tid";

    /**
     * 역할 구분자
     * <p>
     * 여러 역할을 문자열로 저장할 때 사용하는 구분자입니다.
     * <p>
     * Example: "ROLE_USER,ROLE_ADMIN".split(",")
     */
    String ROLE_SEPARATOR = ",";
}
