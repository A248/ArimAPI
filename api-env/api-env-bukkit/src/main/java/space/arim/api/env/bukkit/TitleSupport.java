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

package space.arim.api.env.bukkit;

import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.time.Duration;

import static space.arim.api.env.bukkit.PlayerAudience.convertComponent;
import static space.arim.api.env.bukkit.PlayerAudience.notSupported;

interface TitleSupport {

    void showTitle(Player player, Title title);

    void clearTitle(Player player);

    void resetTitle(Player player);

    class NoTitleSupport implements TitleSupport {

        @Override
        public void showTitle(Player player, Title title) {
            throw notSupported();
        }

        @Override
        public void clearTitle(Player player) {
            throw notSupported();
        }

        @Override
        public void resetTitle(Player player) {
            throw notSupported();
        }
    }

    /**
     * Made for Paper servers before the introduction of Adventure
     * but after the addition of com.destroystokyo.paper.Title
     */
    class PaperTitleSupport implements TitleSupport {

        private static final MethodHandle TITLE_CONSTRUCTOR;

        private static final MethodHandle SEND_TITLE;
        private static final MethodHandle HIDE_TITLE;

        static {
            try {
                Class<?> titleClass = Class.forName("com.destroystokyo.paper.Title");
                var lookup = MethodHandles.lookup();
                // Title(BaseComponent[] title, BaseComponent[] subtitle, int fadeIn, int stay, int fadeOut)
                TITLE_CONSTRUCTOR = lookup.findConstructor(titleClass, MethodType.methodType(
                        void.class, BaseComponent[].class, BaseComponent[].class,
                        int.class, int.class, int.class));
                // Player#sendTitle(Title)
                SEND_TITLE = lookup.findVirtual(Player.class, "sendTitle", MethodType.methodType(void.class, titleClass));
                // Player#hideTitle()
                HIDE_TITLE = lookup.findVirtual(Player.class, "hideTitle", MethodType.methodType(void.class));
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }

        private static Object createPaperTitle(BaseComponent[] title, BaseComponent[] subtitle,
                                               int fadeIn, int stay, int fadeOut) {
            try {
                return TITLE_CONSTRUCTOR.invoke(title, subtitle, fadeIn, stay, fadeOut);
            } catch (RuntimeException | Error ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void showTitle(Player player, Title title) {
            BaseComponent[] bungeeTitle = convertComponent(title.title());
            BaseComponent[] bungeeSubtitle = convertComponent(title.subtitle());
            Object paperTitle;
            Title.Times titleTimes = title.times();
            if (titleTimes == null) {
                paperTitle = createPaperTitle(bungeeTitle, bungeeSubtitle, 0, 0, 0);
            } else {
                int fadeIn = durationToTicks(titleTimes.fadeIn());
                int stay = durationToTicks(titleTimes.stay());
                int fadeOut = durationToTicks(titleTimes.fadeOut());
                paperTitle = createPaperTitle(bungeeTitle, bungeeSubtitle, fadeIn, stay, fadeOut);
            }
            try {
                SEND_TITLE.invoke(player, paperTitle);
            } catch (RuntimeException | Error ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }

        private static int durationToTicks(Duration duration) {
            return (int) (duration.toMillis() / 50L);
        }

        @Override
        public void clearTitle(Player player) {
            try {
                HIDE_TITLE.invokeExact(player);
            } catch (RuntimeException | Error ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void resetTitle(Player player) {
            // This method is not deprecated in relevant Paper versions
            //noinspection deprecation
            player.resetTitle();
        }
    }
}
