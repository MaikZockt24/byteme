package byteme

class Move {
    Long    id
    Room    room
    User    player
    Integer row, col
    String  symbol    // 'X' or 'O'

    Date    dateCreated

    static mapping = {
        table 'move'
        id column:'move_id'
        room column:'room_id'
        player column:'player_id'
        version false
    }

    static constraints = {
        row    range:0..2
        col    range:0..2
        symbol inList:['X','O']
    }
}