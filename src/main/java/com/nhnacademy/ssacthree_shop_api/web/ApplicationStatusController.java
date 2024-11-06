package com.nhnacademy.ssacthree_shop_api.web;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.nhnacademy.ssacthree_shop_api.actuator.ApplicationStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// 외부에서 서버 down 시킬 수 있게 하는 컨트롤러.
// 재배포 이전에 서버를 down시키고 다시 서버에 올림
// shell에서 curl로 down시킬 수 있게함.
@RestController
@RequestMapping("/actuator/status")
public class ApplicationStatusController {

    private final ApplicationInfoManager applicationInfoManager;

    private final ApplicationStatus applicationStatus;

    public ApplicationStatusController(ApplicationInfoManager applicationInfoManager, ApplicationStatus applicationStatus) {
        this.applicationInfoManager = applicationInfoManager;
        this.applicationStatus = applicationStatus;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void stopStatus() {
        // 앱 상태 가져오기
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
        // down 시키기
        applicationStatus.stopService();
    }
}
