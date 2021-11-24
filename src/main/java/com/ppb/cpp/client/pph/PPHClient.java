package com.ppb.cpp.client.pph;

import io.vavr.control.Try;
import java.util.List;
import org.openapitools.client.api.PromotionApi;
import org.openapitools.client.model.Promotion;
import org.openapitools.client.model.PromotionsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PPHClient {

    @Autowired
    private PromotionApi client;

    public Promotion retrievePromotions(String promotionId) {
        var request = new PromotionsRequest();
        request.setClientId("slack-bot");
        request.setPromoCodes(List.of(promotionId));

        return Try.of(() -> client.retrievePromotions(request))
            .onSuccess(response -> System.out.println("Successfully retrieved promotions" + response.getPromotions()))
            .onFailure(e -> System.out.println("Error calling PPH client: " + e.getMessage()))
            .get().getPromotions().get(0);
    }
}
