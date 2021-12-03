package com.ppb.cpp.client.pph;

import com.ppb.cpp.slack.CPPCommand;
import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.openapitools.client.api.PromotionApi;
import org.openapitools.client.model.Promotion;
import org.openapitools.client.model.PromotionsRequest;
import org.openapitools.client.model.PromotionsResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class PPHClient {
    private static final Logger LOG = getLogger(CPPCommand.class);
    @Autowired
    private PromotionApi client;

    public Optional<Promotion> findPromotion(final String promoCode) {
        final var request = new PromotionsRequest();
        request.setClientId("slack-bot");
        request.setPromoCodes(List.of(promoCode));
        LOG.info("retrievePromotion promoCode={}", promoCode);
        return Try.of(() -> client.retrievePromotions(request))
                .onSuccess(r -> LOG.info("Successfully retrieved promotions={}", r.getPromotions()))
                .onFailure(t -> LOG.error("failed fetching promo, errorMessage={}", t.getMessage()))
                .map(PromotionsResponse::getPromotions)
                .map(List::stream)
                .map(Stream::findFirst)
                .getOrElse(Optional::empty);
    }
}
