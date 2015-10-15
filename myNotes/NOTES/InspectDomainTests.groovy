package com.pmi.product.domain

import com.pmi.ancillary.util.InspectDomainUtils
import grails.test.GrailsUnitTestCase
import groovy.sql.Sql
import org.junit.Test
import com.pmi.ancillary.util.InspectJavaObjectsUtils
import com.pmi.ancillary.util.InspectDomainExceptions

@Mixin(InspectDomainUtils)
class InspectDomainTests extends GrailsUnitTestCase {
	
	def grailsApplication
    def sessionFactory
	def dataSource

	/*
	 * You better have a very good reason for putting something in the exceptions!!
	 */
  
	
	def exceptions =  InspectDomainExceptions.ProductCatalogExceptions     

    @Test
	void allDomainClassesUseASequenceAsTheirGenerator() {
        def errors = doAllDomainClassesNotInExceptionsListUseASequenceAsTheirGenerator()
        assert errors.isEmpty() : "One or more classes don't use a sequence as their generator. Check the log for errors."
	}
	
    @Test
	void allDomainClassesThatUseASequenceAreCorrect() {
        def errors = doAllSequencesHaveAHigherNextValThanMaxValueInUnderlyingTable()
        assert errors.isEmpty() : "One or more Sequences are out of sync with the max value from the table using it. Check the log for errors."
    }

    @Test
    void allJavaDomainClassesThatUseASequenceAreCorrect() {
        def sql = new Sql(this.dataSource)
        def inspectJavaObjectsUtils = new InspectJavaObjectsUtils(grailsApplication: grailsApplication, sql:sql)
        def errors = inspectJavaObjectsUtils.doAllJavaDomainClassSequencesHaveAHigherNextValThanMaxValueInUnderlyingTable(exceptions)
        assert errors.isEmpty() : "One or more Sequences are out of sync with the max value from the table using it. Check the log for errors."
    }

    @Test
    void allSequencesHaveAPublicSynonym() {
        def errors = doAllSequencesHaveAPublicSynonym()
        assert errors.isEmpty() : "One or more Sequences don't have a public synonym. Check the log for errors."
    }
    
    @Test
    void allMappedTablesAndColumnsExistInTheDatabase() {
        def errors = doAllMappedTablesAndColumnsExistInTheDatabaseForGrailsObjects()
        assert errors.isEmpty(): "One or more Grails objects are mapped to a table or column that does not exist.  Check the log for errors."
    }

}
