package byteme

class Chat {
    Long    id
    Room    room
    User    author
    String  message

    Date    dateCreated

    static mapping = {
        table 'chat'
        id column:'chat_id'
        room column:'room_id'
        author column:'autor_id'
        version false
    }

    static constraints = {
        author  nullable:false
        room    nullable:false
        message blank:false, maxSize:1024
    }
}