package com.nhnacademy.ssacthree_shop_api.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

// 헬스 체크하는 클래스
@Component
public class CustomHealthIndicator implements HealthIndicator {
    private final ApplicationStatus applicationStatus;

    public CustomHealthIndicator(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }


    @Override
    public Health health() {
        // (애플리케이션 상태) 헬스 체크해서 false면(정상상태x) down으로 변경. -> eureka에게 알림
        if (!applicationStatus.getStatus()) {
            return Health.down().build();
        }
        // 이 경우 서비스가 "UP" 상태임을 나타내며, 추가 정보 "service": "start"를 포함하여 서비스가 실행 중임을 알립니다.
        return Health.up().withDetail("service","start").build();
    }
}
