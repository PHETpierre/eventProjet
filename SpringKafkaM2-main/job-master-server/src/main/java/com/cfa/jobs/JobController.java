package com.cfa.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to launch jobs from an API call
 */
@RestController
@Slf4j
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/v1/jobcontroller")
@RequiredArgsConstructor
public class JobController {

  private final JobLauncher jobLauncher;
  private final Job simpleJob;
  private final Job simpleJobLettre;
  private final Job remoteJobLettre;



  @RequestMapping("/example")
  public void simpleJob(@RequestParam(value = "label") final String label) {
    runJobB(this.simpleJob, label);
  }

  @RequestMapping("/csv-import")
  public void simpleJobLettre() {
    runJobB(this.simpleJobLettre, "simpleJobLettre");
  }

  @RequestMapping("/start")
  public void remoteJobLettre() {
    runJobB(this.remoteJobLettre, "remoteJobLettre");
  }

  private void runJobB(final Job parJob, final String label) {
    final JobParameters locParamJobParameters = new JobParametersBuilder()
      .addParameter("value", new JobParameter(label))
      .addParameter("time", new JobParameter(System.currentTimeMillis()))
      .toJobParameters();

    try {
      log.info("[Job] running . . .");
      jobLauncher.run(parJob, locParamJobParameters);
    } catch (Exception ex) {
      log.error("[RUN JOB ERROR] : " + ex.getMessage());
    }
  }

}