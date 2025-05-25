package byteme

class Game {
    String password
    String status    = "waiting"
    Date   createdAt = new Date()

    static hasMany = [ players: User, moves: Move, chatMessages: ChatMessage ]

    static constraints = {
        password nullable: true, blank: true
        status   inList: ["waiting", "ongoing", "finished"]
    }
}