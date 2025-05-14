package byteme

class Room {
    Long    id
    String  roomName
    String  roomPassword  // hashed

    static mapping = {
        table       'rooms'
        id column:'room_id'
        roomName column:'roomname'
        roomPassword column:'room_password'
        version     false
    }

    static hasMany = [players: User, chatMessages: Chat, moves: Move, statuses: Status]

    static constraints = {
        roomName     blank:false, unique:true
        roomPassword blank:false, size:4..12
    }
}