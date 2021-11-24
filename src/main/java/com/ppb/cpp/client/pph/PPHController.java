package com.ppb.cpp.client.pph;

import java.util.List;
import org.openapitools.client.model.Promotion;
import org.openapitools.client.model.PromotionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PPHController {

    @Autowired
    PPHClient pphClient;

    @RequestMapping(value = "mypromos", method = RequestMethod.GET)
    public ResponseEntity<Promotion> callService() {
        return ResponseEntity.ok(pphClient.retrievePromotions("POKHOMEGAMES"));
    }
}
