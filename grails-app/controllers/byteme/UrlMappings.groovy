package byteme

class UrlMappings {
    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                
            }
        }
        // Benutzerverwaltung
        "/api/register"(controller: "user", action: "register")
        "/api/login"(controller: "user", action: "login")
        "/api/forgot"(controller: "user", action: "forgot")
        "/api/username"(controller: "user", action: "setUsername")
        "/api/logout"(controller: 'user', action: 'logout')
        "/api/delete"(controller: "user", action: "delete")

        // Spielräume & Lobby
        "/api/game/create"(controller: "game", action: "create")
        "/api/game/join"(controller: "game", action: "join")
        "/api/game/reset"(controller: "game", action: "reset")
        "/api/lobby"(controller: "game", action: "lobby")
        "/api/game/leave"(controller: 'game', action: 'leave')

        // Spielzug & Chat (REST-Alternative zu WebSocket)
        "/api/game/move"(controller: "move", action: "move")
        "/api/game/chat"(controller: "chat", action: "chat")
        "/api/game/chat/history"(controller: "chat", action: "history")


        // Fehlerseiten (optional)
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}

