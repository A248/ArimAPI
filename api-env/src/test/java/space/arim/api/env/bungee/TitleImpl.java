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

package space.arim.api.env.bungee;

import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.Objects;

public class TitleImpl implements Title {

    @Override
    public Title title(BaseComponent text) {
        return title(new BaseComponent[] {text});
    }

    private BaseComponent[] title;
    @Override
    public Title title(BaseComponent... text) {
        title = text;
        return this;
    }

    @Override
    public Title subTitle(BaseComponent text) {
        return subTitle(new BaseComponent[] {text});
    }

    private BaseComponent[] subTitle;
    @Override
    public Title subTitle(BaseComponent... text) {
        subTitle = text;
        return this;
    }

    private int fadeIn = -1;
    @Override
    public Title fadeIn(int ticks) {
        fadeIn = ticks;
        return this;
    }

    private int stay = -1;
    @Override
    public Title stay(int ticks) {
        stay = ticks;
        return this;
    }

    private int fadeOut = -1;
    @Override
    public Title fadeOut(int ticks) {
        fadeOut = ticks;
        return this;
    }

    private boolean clear;
    @Override
    public Title clear() {
        clear = true;
        return this;
    }

    private boolean reset;
    @Override
    public Title reset() {
        reset = true;
        return this;
    }

    @Override
    public Title send(ProxiedPlayer player) {
        player.sendTitle(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleImpl title1 = (TitleImpl) o;
        return fadeIn == title1.fadeIn && stay == title1.stay && fadeOut == title1.fadeOut
                && clear == title1.clear && reset == title1.reset
                && Arrays.equals(title, title1.title) && Arrays.equals(subTitle, title1.subTitle);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(title);
        result = 31 * result + Arrays.hashCode(subTitle);
        result = 31 * result + fadeIn;
        result = 31 * result + stay;
        result = 31 * result + fadeOut;
        result = 31 * result + (clear ? 1 : 0);
        result = 31 * result + (reset ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TitleImpl{" +
                "title=" + Arrays.toString(title) +
                ", subTitle=" + Arrays.toString(subTitle) +
                ", fadeIn=" + fadeIn +
                ", stay=" + stay +
                ", fadeOut=" + fadeOut +
                ", clear=" + clear +
                ", reset=" + reset +
                '}';
    }
}
