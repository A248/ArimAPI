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

package space.arim.api.env.compat.compile;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.player.PlayerSettings;
import com.velocitypowered.api.proxy.player.TabList;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.MessagePosition;
import com.velocitypowered.api.util.ModInfo;
import com.velocitypowered.api.util.title.Title;
import net.kyori.adventure.identity.Identity;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VelocityPlayer implements Player {
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public Optional<ServerConnection> getCurrentServer() {
        return Optional.empty();
    }

    @Override
    public PlayerSettings getPlayerSettings() {
        return null;
    }

    @Override
    public Optional<ModInfo> getModInfo() {
        return Optional.empty();
    }

    @Override
    public long getPing() {
        return 0;
    }

    @Override
    public boolean isOnlineMode() {
        return false;
    }

    @Override
    public ConnectionRequestBuilder createConnectionRequest(RegisteredServer server) {
        return null;
    }

    @Override
    public List<GameProfile.Property> getGameProfileProperties() {
        return null;
    }

    @Override
    public void setGameProfileProperties(List<GameProfile.Property> properties) {

    }

    @Override
    public GameProfile getGameProfile() {
        return null;
    }

    @Override
    public void clearHeaderAndFooter() {

    }

    @Override
    public TabList getTabList() {
        return null;
    }

    @Override
    public void sendTitle(Title title) {

    }

    @Override
    public void spoofChatInput(String input) {

    }

    @Override
    public void sendResourcePack(String url) {

    }

    @Override
    public void sendResourcePack(String url, byte[] hash) {

    }

    @Override
    public void disconnect(net.kyori.text.Component reason) {

    }

    @Override
    public void disconnect(net.kyori.adventure.text.Component reason) {

    }

    @Override
    public void setHeaderAndFooter(net.kyori.text.Component header, net.kyori.text.Component footer) {

    }

    @Override
    public void sendMessage(net.kyori.text.Component component, MessagePosition position) {

    }

    @Override
    public Tristate getPermissionValue(String permission) {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public Optional<InetSocketAddress> getVirtualHost() {
        return Optional.empty();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return null;
    }

    @Override
    public boolean sendPluginMessage(ChannelIdentifier identifier, byte[] data) {
        return false;
    }

    @Override
    public @org.checkerframework.checker.nullness.qual.NonNull Identity identity() {
        return null;
    }
}
