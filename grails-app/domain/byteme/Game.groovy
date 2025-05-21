package byteme

class Game {
    String passwordHash   // optionales Passwort (gehasht)
    String status = "waiting" // z.B. "waiting", "ongoing", "finished"
    Date createdAt = new Date()

    static hasMany = [players: User, moves: Move, chatMessages: ChatMessage]
    // Mapping: Viele-zu-Viele-Beziehung zu User
    static mapping = {
        players joinTable: [name: "game_players", key: 'game_id', column: 'user_id']
        passwordHash column: 'password_hash'
    }
    static constraints = {
        passwordHash nullable: true, blank: true
        status inList: ["waiting", "ongoing", "finished"]
    }
}
