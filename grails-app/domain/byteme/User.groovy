package byteme


class User {
    String email
    String passwordHash
    String username
    Date registeredAt = new Date()

    static hasMany = [moves: Move, chatMessages: ChatMessage]
    static belongsTo = [ game: Game ]
    Game game

    static constraints = {
        email blank: false, unique: true, email: true
        passwordHash blank: false
        username nullable: true, blank: true
        game nullable: true 
    }
    static mapping = {
        table 'ttt_user' 
        id column: 'id'
        email column: 'email'
        passwordHash column: 'password_hash'
        username column: 'username'
        registeredAt column: 'registered_at'
        game column: 'game_id'
    }
}
