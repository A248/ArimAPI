/*
 * ArimAPI
 * Copyright © 2021 Anand Beh
 *
 * ArimAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ArimAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ArimAPI. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU General Public License.
 */

package space.arim.api.jsonchat.adventure;

import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import space.arim.api.jsonchat.ChatMessageParser;
import space.arim.api.jsonchat.ClickEventInfo;
import space.arim.api.jsonchat.ParsingVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A {@link ParsingVisitor} intended to be used with {@link ChatMessageParser}
 * in order to parse a message in the JSON.sk format. <br>
 * <br>
 * After creating an instance and running the parser, {@link #buildResult()}
 * should be used to retrieve the resulting {@code Component}. <br>
 * <br>
 * {@code ComponentVisitor}s are mutable and non-reusable. They are not thread safe.
 *
 */
public final class ComponentVisitor implements ParsingVisitor {

    private final FormattingSerializer formattingSerializer;
    /**
     * Either a Component or TextComponent.Builder. Components will be changed
     * to builders if they require modification
     */
    private final List<ComponentLike> componentsOrBuilders = new ArrayList<>();

    private int index;
    private HoverEvent<Component> currentHover;
    private ClickEvent currentClick;
    private String currentInsertion;

    /**
     * Creates from a given formatting serializer
     *
     * @param formattingSerializer the formatting serializer
     */
    public ComponentVisitor(FormattingSerializer formattingSerializer) {
        this.formattingSerializer = Objects.requireNonNull(formattingSerializer, "formattingSerializer");
    }

    /**
     * Creates using the default formatting serializer
     *
     */
    public ComponentVisitor() {
        this(new JsonSkFormattingSerializer());
    }

    @Override
    public void visitHoverEvent(String hoverValue) {
        Component hoverComponent = combineComponents(formattingSerializer.readFormatting(hoverValue));
        currentHover = HoverEvent.showText(hoverComponent);
    }

    @Override
    public void visitClickEvent(ClickEventInfo.ClickType clickType, String value) {
        currentClick = ClickEvent.clickEvent(clickTypeToAction(clickType), value);
    }

    static ClickEvent.Action clickTypeToAction(ClickEventInfo.ClickType clickType) {
        switch (clickType) {
        case RUN_COMMAND:
            return ClickEvent.Action.RUN_COMMAND;
        case SUGGEST_COMMAND:
            return ClickEvent.Action.SUGGEST_COMMAND;
        case OPEN_URL:
            return ClickEvent.Action.OPEN_URL;
        default:
            throw new IllegalStateException("Unknown click type " + clickType);
        }
    }

    @Override
    public void visitInsertion(String value) {
        currentInsertion = value;
    }

    @Override
    public void visitPlainText(String text) {
        updateBuilders();
        currentHover = null;
        currentClick = null;
        currentInsertion = null;
        componentsOrBuilders.addAll(formattingSerializer.readFormatting(text));
    }

    private void updateBuilders() {
        for (int n = index; n < componentsOrBuilders.size(); n++) {

            ComponentLike componentOrBuilder = componentsOrBuilders.get(n);
            assert verifyContract(componentOrBuilder);
            ComponentBuilder<?, ?> componentBuilder;
            if (componentOrBuilder instanceof ComponentBuilder) {
                // Use existing component builder
                componentBuilder = (ComponentBuilder<?, ?>) componentOrBuilder;
            } else {
                componentBuilder = componentLikeToBuilder(componentOrBuilder);
                componentsOrBuilders.set(n, componentBuilder);
            }
            componentBuilder.hoverEvent(currentHover).clickEvent(currentClick).insertion(currentInsertion);
        }
        index = componentsOrBuilders.size();
    }

    private static ComponentBuilder<?, ?> componentLikeToBuilder(ComponentLike componentLike) {
        if (componentLike instanceof ComponentBuilder) {
            return (ComponentBuilder<?, ?>) componentLike;
        }
        if (componentLike instanceof BuildableComponent) {
            return ((BuildableComponent<?, ?>) componentLike).toBuilder();
        }
        TextComponent.Builder textBuilder = Component.text();
        textBuilder.style(componentLike.asComponent().style());
        return textBuilder;
    }

    /**
     * Creates the resulting component. The component will be empty except for the fact
     * that it will have child component corresponding to segments of the parsed message. <br>
     * <br>
     * This {@code ComponentVisitor} should be disposed of after use.
     *
     * @return the component
     */
    public Component buildResult() {
        updateBuilders();
        return combineComponents(componentsOrBuilders);
    }

    /**
     * Combines multiple components into one. Applies an optimization whereby the first component
     * is used as the parent component, which is safe due to the contract of
     * {@link FormattingSerializer#readFormatting(String)}'s requirement against NOT_SET states.
     *
     * @param components components produced from {@code FormattingSerializer#readFormatting(String)}
     * @return the combined component
     */
    private static <C extends ComponentLike> Component combineComponents(List<C> components) {
        switch (components.size()) {
        case 0:
            return Component.empty();
        case 1:
            return components.get(0).asComponent();
        default:
            break;
        }
        return Component.empty().children(components);
    }

    private static boolean verifyContract(Component component) {
        assert component.children().isEmpty()
                : "FormattingSerializer violated contract of #readFormatting by returning components " +
                "with child components";
        assert component.hoverEvent() == null && component.clickEvent() == null && component.insertion() == null
                : "FormattingSerializer violated contract of #readFormatting by returning components " +
                "with hover events, click events, and/or insertions";
        return true;
    }

    private static boolean verifyContract(ComponentLike componentLike) {
        assert verifyContract(componentLike.asComponent());
        return true;
    }
}
