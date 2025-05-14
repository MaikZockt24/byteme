package login

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class MoveSpec extends Specification implements DomainUnitTest<Move> {

     void "test domain constraints"() {
        when:
        Move domain = new Move()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
