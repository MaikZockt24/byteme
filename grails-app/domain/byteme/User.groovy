package byteme


class User {
    String email
    String passwordHash
    String username
    Date registeredAt = new Date()

    static hasMany = [games: Game, moves: Move, chatMessages: ChatMessage]

    static constraints = {
        email blank: false, unique: true, email: true
        passwordHash blank: false
        username nullable: true, blank: true
    }
    static mapping = {
        table 'ttt_user' 
        id column: 'id'
        email column: 'email'
        passwordHash column: 'password_hash'
        username column: 'username'
        registeredAt column: 'registered_at'
    }
}
