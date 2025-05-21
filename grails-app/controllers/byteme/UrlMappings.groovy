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
        "/api/delete"(controller: "user", action: "delete")

        // Fehlerseiten (optional)
        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')

    }
}

