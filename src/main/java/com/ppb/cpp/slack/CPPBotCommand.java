package com.ppb.cpp.slack;

import com.ppb.cpp.client.pph.PPHClient;
import com.slack.api.app_backend.slash_commands.payload.SlashCommandPayload;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.block.element.ImageElement;
import org.openapitools.client.model.Promotion;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static io.vavr.API.*;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Encapsulates instruction handling for cpp-bot command.
 */
@Component
public class CPPBotCommand {
    private static final Logger LOG = getLogger(CPPBotCommand.class);

    @Autowired
    private PPHClient pphClient;

    private static class Instruction {
        private SlashCommandPayload payload;
        public Instruction(final SlashCommandPayload payload) {
            this.payload = payload;
        }

        public boolean is(final String instruction) {
            return payload.getText().matches(format("(?i)^%s\\b.*", instruction));
        }
    }

    public Response handle(final SlashCommandRequest req) {
        LOG.info("handling request={}", req.getPayload().getText());
        return Match(new Instruction(req.getPayload())).of(
                Case($(r -> r.is("promocode")), () -> promoCodeResponse(req)),
                Case($(r -> r.is("help")), () -> helpResponse(req)),
                Case($(), unknownInstruction(req)));
    }

    private Response unknownInstruction(final SlashCommandRequest req) {
        return req.getContext().ack(":wave: Unknown instruction. Try *help* for available instructions..");
    }

    private Response helpResponse(final SlashCommandRequest req) {
        return req.getContext().ack(res -> res.responseType("in_channel").blocks(
                asBlocks(
                        section(section -> section.text(markdownText("*promocode*"))),
                        divider(),
                        section(section -> section.text(markdownText("*help*")))
                )));
    }

    private Response promoCodeResponse(final SlashCommandRequest req) {
        return findPromoCode(req)
                .flatMap(pphClient::findPromotion)
                .map(promo -> responseForPromo(req, promo))
                .orElseGet(() -> req.getContext().ack(format(":wave: Unknown promoCode. Try *help* for available instructions..")));
    }

    private Response responseForPromo(final SlashCommandRequest req, final Promotion promo) {
        return req.getContext().ack(res -> res.responseType("in_channel").blocks(
                asBlocks(
                    section(section -> {
                        section.text(markdownText("*Name:* " + promo.getName()));
                        section.accessory(ButtonElement.builder()
                                .text(plainText("View"))
                                .value(promo.getTermsAndConditions().getUrl())
                                .url(promo.getTermsAndConditions().getUrl())
                                .build());
                        return section;
                    }),
                    section(section -> {
                        section.text(markdownText("*Description:* " + promo.getDescription()));
                        section.accessory(ImageElement.builder()
                                .imageUrl(promo.getImages().get(4).getUrl())
                                .altText(promo.getPromoCode())
                                .build());
                        return section;
                    }),
                    section(section -> section.text(markdownText("*Brand:* " + promo.getBrand()))))
                ));
    }

    private Optional<String> findPromoCode(final SlashCommandRequest req) {
        return stream(req.getPayload()
                .getText()
                .split("\\w+"))
                .reduce((first, second) -> second);
    }
}
