package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class DisciplineServiceSpec extends Specification {

    DisciplineService disciplineService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Discipline(...).save(flush: true, failOnError: true)
        //new Discipline(...).save(flush: true, failOnError: true)
        //Discipline discipline = new Discipline(...).save(flush: true, failOnError: true)
        //new Discipline(...).save(flush: true, failOnError: true)
        //new Discipline(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //discipline.id
    }

    void "test get"() {
        setupData()

        expect:
        disciplineService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Discipline> disciplineList = disciplineService.list(max: 2, offset: 2)

        then:
        disciplineList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        disciplineService.count() == 5
    }

    void "test delete"() {
        Long disciplineId = setupData()

        expect:
        disciplineService.count() == 5

        when:
        disciplineService.delete(disciplineId)
        sessionFactory.currentSession.flush()

        then:
        disciplineService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Discipline discipline = new Discipline()
        disciplineService.save(discipline)

        then:
        discipline.id != null
    }
}
