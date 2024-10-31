package com.arcvad.schoolquest.web_socket;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.data.WebSocketState;
import com.github.czyzby.websocket.serialization.Serializer;

public class ArcSocket implements WebSocket {

    private WebSocketState currentState = WebSocketState.CLOSED;
    private WebSocket socket;
    private String url;
    public ArcSocket(String url) {
        this.url = url;
        socket = WebSockets.newSocket((WebSockets.toWebSocketUrl(url, 677)));
    }

    @Override
    public void connect() throws WebSocketException {
        try {
            Gdx.app.debug("ARC-SOCKET", "Connecting to server");
            currentState = WebSocketState.CONNECTING;
            try {
                socket.connect();
            }catch (WebSocketException e) {
                Gdx.app.error("ARC-SOCKET", "Unable to connect to host");
            }

            currentState = WebSocketState.OPEN;
            Gdx.app.debug("ARC-SOCKET", "Connection established to " + url);
        } catch (WebSocketException e) {
            Gdx.app.error("ARC-SOCKET", "Failed to connect to server: " + e.getMessage());
            currentState = WebSocketState.CLOSED;
            throw e;
        }
    }

    @Override
    public WebSocketState getState() {
        return currentState;
    }

    @Override
    public boolean isOpen() {
        return currentState == WebSocketState.OPEN;
    }

    @Override
    public boolean isConnecting() {
        return currentState == WebSocketState.CONNECTING;
    }

    @Override
    public boolean isClosing() {
        return currentState == WebSocketState.CLOSING;
    }

    @Override
    public boolean isClosed() {
        return currentState == WebSocketState.CLOSED;
    }

    @Override
    public boolean isSecure() {
        return url != null && url.startsWith("wss://");
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void addListener(WebSocketListener listener2) {
        Gdx.app.log("ARC-SOCKET", "Listener set to " + listener2);
        socket.addListener(listener2);
    }

    @Override
    public void removeListener(WebSocketListener listener2) {
        Gdx.app.log("ARC-SOCKET", "Listener " + listener2 + " has been unregistered.");
        socket.removeListener(listener2);
    }

    @Override
    public void sendKeepAlivePacket() throws WebSocketException {
        try {
            Gdx.app.log("ARC-SOCKET", "Sending keep-alive packet.");
            send("PING");
        }catch (WebSocketException e) {
            Gdx.app.error("ARC-SOCKET", "Didn't receive response from KAP");
        }
    }

    @Override
    public void setSerializer(Serializer serializer) {
        Gdx.app.log("ARC-SOCKET", "Serializer set to " + serializer);
        socket.setSerializer(serializer);
    }

    @Override
    public Serializer getSerializer() {
        return socket.getSerializer();
    }

    @Override
    public void setSerializeAsString(boolean asString) {
        Gdx.app.log("ARC-SOCKET", "Serialize as String? " + asString);
        socket.setSerializeAsString(asString);
    }

    @Override
    public void setSendGracefully(boolean sendGracefully) {
        Gdx.app.log("ARC-SOCKET", sendGracefully ? "Packets will be sent gracefully." : "Packets will be sent immediately.");
        socket.setSendGracefully(sendGracefully);
    }

    @Override
    public void setUseTcpNoDelay(boolean tcpNoDelay) {
        Gdx.app.log("ARC-SOCKET", "TcpNoDelay set to " + tcpNoDelay);
        socket.setUseTcpNoDelay(tcpNoDelay);
    }

    @Override
    public void setVerifyHostname(boolean verifyHostname) {
        Gdx.app.log("ARC-SOCKET", "Verify hostname set to " + verifyHostname);
        socket.setVerifyHostname(verifyHostname);
    }

    @Override
    public void send(Object packet) throws WebSocketException {
        if (isOpen()) {
            try {
                if (packet instanceof String) {
                    socket.send((String) packet);
                } else if (packet instanceof byte[]) {
                    socket.send((byte[]) packet);
                } else if (getSerializer() != null) {
                    byte[] serialized = getSerializer().serialize(packet);
                    socket.send(serialized);
                } else {
                    throw new WebSocketException("Unsupported data type: " + packet.getClass());
                }
            } catch (WebSocketException e) {
                Gdx.app.error("ARC-SOCKET", "Failed to send packet: " + e.getMessage());
                throw e;
            }
        } else {
            Gdx.app.error("ARC-SOCKET", "Cannot send packet; WebSocket is not open.");
        }
    }

    @Override
    public void send(String packet) throws WebSocketException {
        if (isOpen()) {
            Gdx.app.log("ARC-SOCKET", "Sending packet: " + packet);
            socket.send(packet);
        } else {
            Gdx.app.error("ARC-SOCKET", "Cannot send packet; WebSocket is not open.");
        }
    }

    @Override
    public void send(byte[] packet) throws WebSocketException {
        if (isOpen()) {
            Gdx.app.log("ARC-SOCKET", "Sending byte array packet.");
            socket.send(packet);
        } else {
            Gdx.app.error("ARC-SOCKET", "Cannot send packet; WebSocket is not open.");
        }
    }

    @Override
    public boolean isSupported() {
        try {
            Gdx.app.log("ARC-SOCKET", "Checking if server is supported.");
            connect();
            send("I'm about to join");
            return true;
        } catch (Exception e) {
            Gdx.app.error("ARC-SOCKET", "Unsupported server version. Aborting.");
            return false;
        }
    }

    @Override
    public void close() throws WebSocketException {
        if (isOpen()) {
            Gdx.app.debug("ARC-SOCKET", "Closing connection.");
            socket.close();
            currentState = WebSocketState.CLOSED;
        } else {
            Gdx.app.debug("ARC-SOCKET", "Connection is already closed.");
        }
    }

    @Override
    public void close(String reason) throws WebSocketException {
        if (isOpen()) {
            Gdx.app.debug("ARC-SOCKET", "Closing connection. Reason: " + reason);
            socket.close();
            currentState = WebSocketState.CLOSED;
        } else {
            Gdx.app.debug("ARC-SOCKET", "Connection is already closed.");
        }
    }

    @Override
    public void close(int closeCode) throws WebSocketException {
        if (isOpen()) {
            Gdx.app.log("ARC-SOCKET", "Server connection closed with code: " + closeCode);
            socket.close();
            currentState = WebSocketState.CLOSED;
        } else {
            Gdx.app.debug("ARC-SOCKET", "Connection is already closed.");
        }
    }

    @Override
    public void close(int closeCode, String reason) throws WebSocketException {
        if (isOpen()) {
            Gdx.app.debug("ARC-SOCKET", "Closing connection. Reason: " + reason + " [" + closeCode + "]");
            socket.close();
            currentState = WebSocketState.CLOSED;
        } else {
            Gdx.app.debug("ARC-SOCKET", "Connection is already closed.");
        }
    }
}
