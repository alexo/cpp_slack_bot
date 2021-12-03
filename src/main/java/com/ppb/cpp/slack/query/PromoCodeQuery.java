package com.ppb.cpp.slack.query;

import com.ppb.cpp.client.pph.PPHClient;
import com.ppb.cpp.support.Text;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.SectionBlock.SectionBlockBuilder;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.block.element.ImageElement;
import org.jetbrains.annotations.NotNull;
import org.openapitools.client.model.Promotion;

import java.util.Optional;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class PromoCodeQuery implements Query {
    private static final String INSTRUCTION = "promoCode";
    private PPHClient pphClient;

    public PromoCodeQuery(final PPHClient pphClient) {
        this.pphClient = pphClient;
    }

    @Override
    public boolean accept(final String text) {
        return new Text(text).isFirstWord(INSTRUCTION);
    }

    @Override
    public Response handle(final SlashCommandRequest req) {
        return findPromoCode(req)
                .flatMap(pphClient::findPromotion)
                .map(promo -> responseForPromo(req, promo))
                .orElseGet(() -> req.getContext().ack(format(":wave: Unknown promoCode. Try *help* for available instructions..")));
    }

    @Override
    public LayoutBlock describe() {
        return section(section -> section.text(markdownText("*promocode*\nUsage: `promocode PROMOCODE`")));
    }

    private Response responseForPromo(final SlashCommandRequest req, final Promotion promo) {
        return req.getContext()
                .ack(res -> res.blocks(asList(section(section -> firtSection(promo, section)),
                        section(section -> secondSection(promo, section)),
                        section(section -> thirdSection(promo, section)))
        ));
    }

    private SectionBlockBuilder thirdSection(final Promotion promo, final SectionBlockBuilder section) {
        return section.text(markdownText("*Brand:* " + promo.getBrand()));
    }

    @NotNull
    private SectionBlockBuilder secondSection(final Promotion promo, final SectionBlockBuilder section) {
        section.text(markdownText("*Description:* " + promo.getDescription()));
        section.accessory(ImageElement.builder()
                .imageUrl(promo.getImages().get(4).getUrl())
                .altText(promo.getPromoCode())
                .build());
        return section;
    }

    @NotNull
    private SectionBlockBuilder firtSection(final Promotion promo, final SectionBlockBuilder section) {
        section.text(markdownText("*Name:* " + promo.getName()));
        section.accessory(ButtonElement.builder()
                .text(plainText("View"))
                .value(promo.getTermsAndConditions().getUrl())
                .url(promo.getTermsAndConditions().getUrl())
                .build());
        return section;
    }

    private Optional<String> findPromoCode(final SlashCommandRequest req) {
        return new Text(req.getPayload().getText()).findLastWord();
    }
}
