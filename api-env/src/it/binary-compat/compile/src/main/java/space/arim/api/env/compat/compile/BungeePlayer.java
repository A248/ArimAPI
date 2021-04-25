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

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ServerConnectRequest;
import net.md_5.bungee.api.SkinConfiguration;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BungeePlayer implements ProxiedPlayer {
    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String s) {

    }

    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent... baseComponents) {

    }

    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent baseComponent) {

    }

    @Override
    public void sendMessage(UUID uuid, BaseComponent... baseComponents) {

    }

    @Override
    public void sendMessage(UUID uuid, BaseComponent baseComponent) {

    }

    @Override
    public void connect(ServerInfo serverInfo) {

    }

    @Override
    public void connect(ServerInfo serverInfo, ServerConnectEvent.Reason reason) {

    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback) {

    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {

    }

    @Override
    public void connect(ServerConnectRequest serverConnectRequest) {

    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public int getPing() {
        return 0;
    }

    @Override
    public void sendData(String s, byte[] bytes) {

    }

    @Override
    public PendingConnection getPendingConnection() {
        return null;
    }

    @Override
    public void chat(String s) {

    }

    @Override
    public ServerInfo getReconnectServer() {
        return null;
    }

    @Override
    public void setReconnectServer(ServerInfo serverInfo) {

    }

    @Override
    public String getUUID() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public byte getViewDistance() {
        return 0;
    }

    @Override
    public ChatMode getChatMode() {
        return null;
    }

    @Override
    public boolean hasChatColors() {
        return false;
    }

    @Override
    public SkinConfiguration getSkinParts() {
        return null;
    }

    @Override
    public MainHand getMainHand() {
        return null;
    }

    @Override
    public void setTabHeader(BaseComponent baseComponent, BaseComponent baseComponent1) {

    }

    @Override
    public void setTabHeader(BaseComponent[] baseComponents, BaseComponent[] baseComponents1) {

    }

    @Override
    public void resetTabHeader() {

    }

    @Override
    public void sendTitle(Title title) {

    }

    @Override
    public boolean isForgeUser() {
        return false;
    }

    @Override
    public Map<String, String> getModList() {
        return null;
    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void sendMessage(String s) {

    }

    @Override
    public void sendMessages(String... strings) {

    }

    @Override
    public void sendMessage(BaseComponent... baseComponents) {

    }

    @Override
    public void sendMessage(BaseComponent baseComponent) {

    }

    @Override
    public Collection<String> getGroups() {
        return null;
    }

    @Override
    public void addGroups(String... strings) {

    }

    @Override
    public void removeGroups(String... strings) {

    }

    @Override
    public boolean hasPermission(String s) {
        return false;
    }

    @Override
    public void setPermission(String s, boolean b) {

    }

    @Override
    public Collection<String> getPermissions() {
        return null;
    }

    @Override
    public InetSocketAddress getAddress() {
        return null;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return null;
    }

    @Override
    public void disconnect(String s) {

    }

    @Override
    public void disconnect(BaseComponent... baseComponents) {

    }

    @Override
    public void disconnect(BaseComponent baseComponent) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public Unsafe unsafe() {
        return null;
    }
}
