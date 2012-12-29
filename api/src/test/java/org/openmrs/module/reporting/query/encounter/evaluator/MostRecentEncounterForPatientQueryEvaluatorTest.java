/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.reporting.query.encounter.evaluator;

import java.util.Collections;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.Encounter;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.query.encounter.EncounterQueryResult;
import org.openmrs.module.reporting.query.encounter.definition.EncounterQuery;
import org.openmrs.module.reporting.query.encounter.definition.MostRecentEncounterForPatientQuery;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class MostRecentEncounterForPatientQueryEvaluatorTest extends BaseModuleContextSensitiveTest {
	
	private final Integer patientId = 7;
	
	private EncounterService es;
	
	@Before
	public void setup() {
		es = Context.getEncounterService();
	}
	
	private Encounter buildEncounter() {
		Encounter enc = new Encounter();
		enc.setLocation(Context.getLocationService().getLocation(1));
		enc.setEncounterType(es.getEncounterType(1));
		enc.setPatient(Context.getPatientService().getPatient(patientId));
		return enc;
	}
	
	/**
	 * @see {@link MostRecentEncounterForPatientQueryEvaluator#evaluate(EncounterQuery,EvaluationContext)}
	 */
	@Test
	@Verifies(value = "should include the encounter at the start of the specified onOrAfter date", method = "evaluate(EncounterQuery,EvaluationContext)")
	public void evaluate_shouldIncludeTheEncounterAtTheStartOfTheSpecifiedOnOrAfterDate() throws Exception {
		Encounter enc = buildEncounter();
		Date date = new Date();
		enc.setEncounterDatetime(DateUtil.getStartOfDay(date));
		es.saveEncounter(enc);
		
		MostRecentEncounterForPatientQuery query = new MostRecentEncounterForPatientQuery();
		query.setOnOrAfter(date);
		Cohort cohort = new Cohort(Collections.singleton(patientId));
		EvaluationContext context = new EvaluationContext();
		context.setBaseCohort(cohort);
		EncounterQueryResult result = new MostRecentEncounterForPatientQueryEvaluator().evaluate(query, context);
		Assert.assertEquals(enc.getEncounterId(), result.getMemberIds().iterator().next());
	}
	
	/**
	 * @see {@link MostRecentEncounterForPatientQueryEvaluator#evaluate(EncounterQuery,EvaluationContext)}
	 */
	@Test
	@Verifies(value = "should include the encounter at the end of the specified onOrAfter date", method = "evaluate(EncounterQuery,EvaluationContext)")
	public void evaluate_shouldIncludeTheEncounterAtTheEndOfTheSpecifiedOnOrAfterDate() throws Exception {
		Encounter enc = buildEncounter();
		Date date = new Date();
		enc.setEncounterDatetime(DateUtil.getEndOfDay(date));
		es.saveEncounter(enc);
		
		MostRecentEncounterForPatientQuery query = new MostRecentEncounterForPatientQuery();
		query.setOnOrAfter(date);
		Cohort cohort = new Cohort(Collections.singleton(patientId));
		EvaluationContext context = new EvaluationContext();
		context.setBaseCohort(cohort);
		EncounterQueryResult result = new MostRecentEncounterForPatientQueryEvaluator().evaluate(query, context);
		Assert.assertEquals(enc.getEncounterId(), result.getMemberIds().iterator().next());
	}
	
	/**
	 * @see {@link MostRecentEncounterForPatientQueryEvaluator#evaluate(EncounterQuery,EvaluationContext)}
	 */
	@Test
	@Verifies(value = "should include the encounter at the start of the specified onOrBefore date", method = "evaluate(EncounterQuery,EvaluationContext)")
	public void evaluate_shouldIncludeTheEncounterAtTheStartOfTheSpecifiedOnOrBeforeDate() throws Exception {
		Encounter enc = buildEncounter();
		Date date = new Date();
		enc.setEncounterDatetime(DateUtil.getStartOfDay(date));
		es.saveEncounter(enc);
		
		MostRecentEncounterForPatientQuery query = new MostRecentEncounterForPatientQuery();
		query.setOnOrBefore(date);
		Cohort cohort = new Cohort(Collections.singleton(patientId));
		EvaluationContext context = new EvaluationContext();
		context.setBaseCohort(cohort);
		EncounterQueryResult result = new MostRecentEncounterForPatientQueryEvaluator().evaluate(query, context);
		Assert.assertEquals(enc.getEncounterId(), result.getMemberIds().iterator().next());
	}
	
	/**
	 * @see {@link MostRecentEncounterForPatientQueryEvaluator#evaluate(EncounterQuery,EvaluationContext)}
	 */
	@Test
	@Verifies(value = "should include the encounter at the end of the specified onOrBefore date", method = "evaluate(EncounterQuery,EvaluationContext)")
	public void evaluate_shouldIncludeTheEncounterAtTheEndOfTheSpecifiedOnOrBeforeDate() throws Exception {
		Encounter enc = buildEncounter();
		Date date = new Date();
		enc.setEncounterDatetime(DateUtil.getEndOfDay(date));
		es.saveEncounter(enc);
		
		MostRecentEncounterForPatientQuery query = new MostRecentEncounterForPatientQuery();
		query.setOnOrBefore(date);
		Cohort cohort = new Cohort(Collections.singleton(patientId));
		EvaluationContext context = new EvaluationContext();
		context.setBaseCohort(cohort);
		EncounterQueryResult result = new MostRecentEncounterForPatientQueryEvaluator().evaluate(query, context);
		Assert.assertEquals(enc.getEncounterId(), result.getMemberIds().iterator().next());
	}
}
