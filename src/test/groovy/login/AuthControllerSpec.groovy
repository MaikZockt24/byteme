package login

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class AuthControllerSpec extends Specification implements ControllerUnitTest<AuthController> {

     void "test index action"() {
        when:
        controller.index()

        then:
        status == 200

     }
}
