package com.ogz.user.socket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Component;

@Component
public class SocketService {

    private final SocketIOServer socketIOServer;

    public SocketService(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;

        socketIOServer.addConnectListener((socketIOClient)->{});
        socketIOServer.addDisconnectListener((socketIOClient)->{});

    }
}
