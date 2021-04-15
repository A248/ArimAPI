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

package space.arim.api.jsonchat.adventure.internal;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import space.arim.api.jsonchat.ChatMessagePart;
import space.arim.api.jsonchat.ClickEventInfo;
import space.arim.api.jsonchat.adventure.FormattingSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ComponentToMessagePartIterator extends IteratorBase<ChatMessagePart> {

    private final PeekingIterator<Component> componentIterator;
    private final FormattingSerializer formattingSerializer;

    public ComponentToMessagePartIterator(PeekingIterator<Component> componentIterator,
                                          FormattingSerializer formattingSerializer) {
        this.componentIterator = componentIterator;
        this.formattingSerializer = formattingSerializer;
    }

    @Override
    protected ChatMessagePart getNext() {
        if (!componentIterator.hasNext()) {
            return null;
        }
        Component firstComponent = componentIterator.next();
        HoverEvent<?> currentHover = firstComponent.hoverEvent();
        ClickEvent currentClick = firstComponent.clickEvent();
        String currentInsertion = firstComponent.insertion();

        List<Component> componentsWithSameEvents = new ArrayList<>();
        componentsWithSameEvents.add(firstComponent);

        while (true) {
            if (!componentIterator.hasNext()) {
                break;
            }
            Component nextComponent = componentIterator.peek();
            if (!Objects.equals(currentHover, nextComponent.hoverEvent())
                    || !Objects.equals(currentClick, nextComponent.clickEvent())
                    || !Objects.equals(currentInsertion, nextComponent.insertion())) {
                // Stop here. Don't consume this Component
                break;
            }
            componentIterator.next();
            componentsWithSameEvents.add(nextComponent);
        }
        return new ChatMessagePart.Builder()
                .associatedHoverEvent(convertHover(currentHover))
                .associatedClickEvent(convertClick(currentClick))
                .associatedInsertion(currentInsertion)
                .build(formattingSerializer.writeFormatting(componentsWithSameEvents));
    }

    private CharSequence convertHover(HoverEvent<?> hover) {
        if (hover == null) {
            return null;
        }
        if (hover.action() != HoverEvent.Action.SHOW_TEXT) {
            throw new UnsupportedOperationException("Hover event action " + hover.action() + " not supported");
        }
        @SuppressWarnings("unchecked")
        HoverEvent<Component> casted = (HoverEvent<Component>) hover;
        return formattingSerializer.writeFormatting(() -> new ComponentIterator(casted.value()));
    }

    private ClickEventInfo convertClick(ClickEvent clickEvent) {
        if (clickEvent == null) {
            return null;
        }
        return new ClickEventInfo(convertClickType(clickEvent.action()), clickEvent.value());
    }

    private ClickEventInfo.ClickType convertClickType(ClickEvent.Action action) {
        switch (action) {
        case OPEN_URL:
            return ClickEventInfo.ClickType.OPEN_URL;
        case RUN_COMMAND:
            return ClickEventInfo.ClickType.RUN_COMMAND;
        case SUGGEST_COMMAND:
            return ClickEventInfo.ClickType.SUGGEST_COMMAND;
        case OPEN_FILE:
        case CHANGE_PAGE:
        case COPY_TO_CLIPBOARD:
        default:
            throw new UnsupportedOperationException("Click event action " + action + " is not supported");
        }
    }
}
