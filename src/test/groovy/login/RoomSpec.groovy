package login

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class RoomSpec extends Specification implements DomainUnitTest<Room> {

     void "test domain constraints"() {
        when:
        Room domain = new Room()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
