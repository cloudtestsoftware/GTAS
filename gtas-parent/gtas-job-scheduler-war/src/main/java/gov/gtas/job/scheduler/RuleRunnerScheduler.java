/*
 * All GTAS code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.job.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.ErrorDetailInfo;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.services.ErrorPersistenceService;
import gov.gtas.svc.TargetingService;

/**
 * Rule Runner Scheduler class. Using Spring's Scheduled annotation for
 * scheduling tasks. The class reads configuration values from an external file.
 *
 */
@Component
public class RuleRunnerScheduler {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(RuleRunnerScheduler.class);

	/** The targeting service. */
	private TargetingService targetingService;

	/** The error persistence service. */
	private ErrorPersistenceService errorPersistenceService;

	/**
	 * Instantiates a new rule runner scheduler.
	 *
	 * @param targetingService
	 *            the targeting service
	 * @param errorPersistenceService
	 *            the error persistence service
	 */
	@Autowired
	public RuleRunnerScheduler(TargetingService targetingService, ErrorPersistenceService errorPersistenceService) {
		this.targetingService = targetingService;
		this.errorPersistenceService = errorPersistenceService;
	}

	/**
	 * Job scheduling.
	 */
	@Scheduled(fixedDelayString = "${ruleRunner.fixedDelay.in.milliseconds}", initialDelayString = "${ruleRunner.initialDelay.in.milliseconds}")
	public void jobScheduling() {
		logger.info("entering jobScheduling()");
		try {
			targetingService.preProcessing();
			targetingService.runningRuleEngine();
		} catch (Exception exception) {
			logger.error(exception.getCause().getMessage());
			ErrorDetailInfo errInfo = ErrorHandlerFactory
					.createErrorDetails(RuleServiceConstants.RULE_ENGINE_RUNNER_ERROR_CODE, exception);
			errorPersistenceService.create(errInfo);
		}
		logger.info("exiting jobScheduling()");
	}

}
