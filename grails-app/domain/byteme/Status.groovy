package byteme

class Status {
    Long    id
    Room    room
    User    player
    String  state      // e.g. IN_ROOM, READY, PLAYING

    static mapping = {
        table 'status'
        id column:'status_id'
        room column:'game_id'
        player column:'player_id'
        version false
    }

    static constraints = {
        state inList:['IN_ROOM','READY','PLAYING','LEFT']
    }
}