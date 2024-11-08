package com.nhnacademy.ssacthree_shop_api.actuator;

import org.springframework.stereotype.Component;

// 애플리케이션 상태
@Component
public final class ApplicationStatus {
    private boolean status = true;

    public void stopService(){
        this.status = false;
    }

    public boolean getStatus(){
        return status;
    }
}
