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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import space.arim.api.chat.PredefinedColour;
import space.arim.api.jsonchat.adventure.internal.ComponentSerialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formatting serializer which uses {@literal '&'} formatting codes as well as a syntax
 * for RGB colors. <br>
 * <br>
 * The RGB syntax permits arbitrary colors denoted by the pseudo regex
 * <pre>{@literal <#(6*(A-F|0-9))>}</pre>. Furthermore, 3 character hex
 * values are permitted. For example, all of the following are accepted: <br>
 * <pre>
 * {@literal &a, &1, &f}
 * {@literal <#AA00AA>}
 * {@literal <#00FFFF>}
 * {@literal <#0FF>} (equivalent to the previous)
 * </pre>
 *
 */
public final class JsonSkFormattingSerializer implements FormattingSerializer {

    private static final Pattern COLOUR_PATTERN = Pattern.compile(
            // Legacy colour codes
            "(&[0-9A-Fa-fK-Rk-r])|"
            // Hex codes such as <#00AAFF>
            + "(<#[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]>)|"
            // and the shorter <#4BC>
            + "(<#[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]>)");

    /**
     * Creates the formatting serializer
     */
    public JsonSkFormattingSerializer() {}

    @Override
    public List<? extends ComponentLike> readFormatting(String text) {
        ReadingState currentState = new ReadingState();
        int beginIndex = 0;

        Matcher matcher = COLOUR_PATTERN.matcher(text);
        while (matcher.find()) {

            int currentIndex = matcher.start();
            String segment = text.substring(beginIndex, currentIndex);
            if (!segment.isEmpty()) {
                currentState.addComponent(segment);
            }
            currentState.updateFormatting(matcher.group());
            // prepare for the next segment by updating the starting index
            beginIndex = matcher.end();
        }
        String remaining = text.substring(beginIndex);
        if (!remaining.isEmpty()) {
            currentState.addComponent(remaining);
        }
        return currentState.finish();
    }

    private static class ReadingState {

        private final List<TextComponent.Builder> components = new ArrayList<>();
        private TextColor currentColor = NamedTextColor.WHITE;
        private final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);

        private void addComponent(String text) {
            TextComponent.Builder componentBuilder = Component.text().content(text).color(currentColor);
            componentBuilder.decorations(decorations, true);
            components.add(componentBuilder);
        }

        private boolean updateFormattingFromCodeAndDetermineWhetherToClearDecorations(String formatCode) {
            char codeChar = formatCode.charAt(1);
            switch (codeChar) {
            case 'K':
            case 'k':
                decorations.add(TextDecoration.OBFUSCATED);
                break;
            case 'L':
            case 'l':
                decorations.add(TextDecoration.BOLD);
                break;
            case 'M':
            case 'm':
                decorations.add(TextDecoration.STRIKETHROUGH);
                break;
            case 'N':
            case 'n':
                decorations.add(TextDecoration.UNDERLINED);
                break;
            case 'O':
            case 'o':
                decorations.add(TextDecoration.ITALIC);
                break;
            case 'R':
            case 'r':
                currentColor = NamedTextColor.WHITE;
                return true;
            default:
                int color = PredefinedColour.getByChar(codeChar).getColour();
                currentColor = TextColor.color(color);
                decorations.clear();
                return true;
            }
            return false;
        }

        void updateFormatting(String formatGroup) {
            boolean clearDecorations = true;
            switch (formatGroup.length()) {
            case 2:
                clearDecorations = updateFormattingFromCodeAndDetermineWhetherToClearDecorations(formatGroup);
                break;
            case 6:
                // Shortened hex colour code
                char[] shortHex = formatGroup.substring(2, 5).toCharArray();
                char[] fullHex = new char[] {shortHex[0], shortHex[0], shortHex[1], shortHex[1], shortHex[2], shortHex[2]};
                setHexColor(String.valueOf(fullHex));
                break;
            case 9:
                // Full hex colour code
                setHexColor(formatGroup.substring(2, 8));
                break;
            default:
                throw new IllegalStateException("Matched formatting " + formatGroup + " has no known way to be handled");
            }
            if (clearDecorations) {
                decorations.clear();
            }
        }

        private void setHexColor(String hex) {
            int color = Integer.parseInt(hex, 16);
            currentColor = TextColor.color(color);
        }

        List<? extends ComponentLike> finish() {
            return components;
        }
    }

    @Override
    public void writeFormatting(Iterable<Component> components, Appendable output) throws IOException {
        WritingState writingState = new WritingState(output);

        for (Component component : components) {
            if (component == Component.empty() || !(component instanceof TextComponent)) {
                continue;
            }
            writingState.writeComponent((TextComponent) component);
        }
    }

    private static class WritingState {

        private Style currentStyle = Style.empty();
        private TextColor currentColor = NamedTextColor.WHITE;
        private final Appendable output;

        WritingState(Appendable output) {
            this.output = output;
        }

        void writeComponent(TextComponent component) throws IOException {
            String content = component.content();
            if (content.isEmpty()) {
                return;
            }
            TextColor color = Objects.requireNonNullElse(component.color(), NamedTextColor.WHITE);
            Style style = component.style();

            boolean decorsDiffer = !currentStyleDecorationsMatch(style);
            boolean colorDiffers = !currentColor.equals(color);
            if (decorsDiffer) {
                // Change to the new styles

                // If there were no existing styles, no need to reset
                // If the colors differ, they reset the style
                boolean reset = anyDecorationPresent(currentStyle) && !colorDiffers;
                if (reset) {
                    output.append("&r");
                }
                if (colorDiffers
                        // If we didn't reset or the new color is white, don't write &f
                        || reset && !color.equals(NamedTextColor.WHITE)) {
                    appendColor(color);
                }
                appendStyles(style);
            } else if (colorDiffers) {
                // Same styles, different colour
                appendColor(color);
            }
            currentColor = color;
            currentStyle = style;
            output.append(content);
        }

        private boolean currentStyleDecorationsMatch(Style otherStyle) {
            if (currentStyle == otherStyle) {
                return true;
            }
            for (TextDecoration decoration : TextDecoration.values()) {
                if (currentStyle.hasDecoration(decoration) != otherStyle.hasDecoration(decoration)) {
                    return false;
                }
            }
            return true;
        }

        private static boolean anyDecorationPresent(Style style) {
            for (TextDecoration decoration : TextDecoration.values()) {
                if (style.hasDecoration(decoration)) {
                    return true;
                }
            }
            return false;
        }

        private void appendColor(TextColor color) throws IOException {
            ComponentSerialization.appendColor(color, output);
        }

        private void appendStyles(Style style) throws IOException {
            for (TextDecoration decoration : TextDecoration.values()) {
                if (style.hasDecoration(decoration)) {
                    output.append('&').append(getCharacter(decoration));
                }
            }
        }

        private static char getCharacter(TextDecoration decoration) {
            switch (decoration) {
            case OBFUSCATED:
                return 'k';
            case BOLD:
                return 'l';
            case STRIKETHROUGH:
                return 'm';
            case UNDERLINED:
                return 'n';
            case ITALIC:
                return 'o';
            default:
                throw new IllegalStateException("Unknown text decoration " + decoration);
            }
        }
    }

}
