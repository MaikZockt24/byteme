class UrlMappings {
    static mappings = {
        // GET-Requests leiten direkt auf die statischen HTMLs weiter
        "/"               (uri: "/login.html")
        "/login"          (uri: "/login.html")
        "/register"       (uri: "/register.html")
        "/forgot"         (uri: "/forgot.html")
        "/username"       (uri: "/username.html")
        "/lobby"          (uri: "/lobby.html")
        "/game"           (uri: "/game.html")

        // POST-Endpoints für Form-Submits
        post "/doLogin"      (controller: "login", action: "authenticate")
        post "/doRegister"   (controller: "register", action: "save")
        post "/doForgot"     (controller: "forgotPassword", action: "update")
        post "/doUsername"   (controller: "user", action: "setUsername")
        post "/room/create"  (controller: "room", action: "create")
        post "/room/join"    (controller: "room", action: "join")
        post "/room/reset"   (controller: "room", action: "reset")

        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
