package byteme

class UrlMappings {
    static mappings = {
        // JSON-Only Endpoints (POST)
        post "/login"      (controller: "login",          action: "authenticate", parseRequest: true)
        post "/register"   (controller: "register",       action: "save",         parseRequest: true)
        post "/forgot"     (controller: "forgotPassword", action: "update",       parseRequest: true)
        post "/username"   (controller: "user",           action: "setUsername",  parseRequest: true)

        post "/room/create"(controller: "room",           action: "create",       parseRequest: true)
        post "/room/join"  (controller: "room",           action: "join",         parseRequest: true)
        post "/room/reset" (controller: "room",           action: "reset",        parseRequest: true)

        post "/chat/list"  (controller: "chat",           action: "list",         parseRequest: true)
        post "/chat/post"  (controller: "chat",           action: "post",         parseRequest: true)

        // Fehler-Seiten (statisch)
        "500"(view: "/error.html")
        "404"(view: "/404.html")
    }
}
