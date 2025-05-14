package byteme

class User {
    Long    id               // PK user_id
    String  sessionName      // sessionname
    String  email            // email
    String  password         // passwort (hashed)

    static mapping = {
        table       'users'
        id column:'user_id'
        sessionName column:'sessionname'
        password    column:'passwort'
        version     false
    }

    static constraints = {
        sessionName blank:false, unique:true, size:3..30
        email       blank:false, email:true, unique:true
        password    blank:false, size:8..64
    }
}