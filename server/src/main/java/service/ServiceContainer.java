package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import server.WebSocketSessionsManager;

public class ServiceContainer {
    public final ClearDataService clearService;
    public final CreateGameService createService;
    public final JoinGameService joinService;
    public final ListGameService listService;
    public final LoginService loginService;
    public final LogoutService logoutService;
    public final RegisterService registerService;
    public final WebSocketService webSocketService;

    public ServiceContainer(DataAccessContainer dataAccessContainer, WebSocketSessionsManager webSocketManager) {
        this.createService = new CreateGameService(dataAccessContainer);
        this.clearService = new ClearDataService(dataAccessContainer);
        this.joinService = new JoinGameService(dataAccessContainer);
        this.listService = new ListGameService(dataAccessContainer);
        this.loginService = new LoginService(dataAccessContainer);
        this.logoutService = new LogoutService(dataAccessContainer);
        this.registerService = new RegisterService(dataAccessContainer);
        this.webSocketService = new WebSocketService(webSocketManager);

        // Initialize other services here
    }
}
