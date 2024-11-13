package com.nhnacademy.ssacthree_shop_api.elastic.controller;

import com.nhnacademy.ssacthree_shop_api.elastic.service.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/search")  // Search 경로로 접근
public class ElasticController {

  private final ElasticService elasticService;

  @Autowired
  public ElasticController(ElasticService elasticService) {
    this.elasticService = elasticService;
  }


}
