package byteme

class ChatMessage {
    Game game
    User user
    String text
    Date createdAt = new Date()

    static constraints = {
        game nullable: false
        user nullable: false
        text blank: false, maxSize: 1000
    }
    static mapping = {
        user column: 'user_id'
        game column: 'game_id'
    }
}
