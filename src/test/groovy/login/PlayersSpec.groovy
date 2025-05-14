package login

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PlayersSpec extends Specification implements DomainUnitTest<Players> {

     void "test domain constraints"() {
        when:
        Players domain = new Players()
        //TODO: Set domain props here

        then:
        domain.validate()
     }
}
