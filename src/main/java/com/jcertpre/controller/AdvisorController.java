package com.jcertpre.controller;

import com.jcertpre.model.AdvisorStudyPlan;
import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.services.AdvisorStudyPlanService;
import com.jcertpre.services.ConsultationScheduleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advisor")
public class AdvisorController {
    private final AdvisorStudyPlanService studyPlanService;
    private final ConsultationScheduleService consultationService;

    public AdvisorController(AdvisorStudyPlanService studyPlanService, ConsultationScheduleService consultationService) {
        this.studyPlanService = studyPlanService;
        this.consultationService = consultationService;
    }

    @PostMapping("/study-plan")
    public AdvisorStudyPlan createPlan(@RequestBody AdvisorStudyPlan plan) {
        return studyPlanService.create(plan);
    }

    @PostMapping("/consultation")
    public ConsultationSchedule createConsultation(@RequestBody ConsultationSchedule cs) {
        return consultationService.create(cs);
    }

    @PutMapping("/consultation/{id}/approve")
    public void approveConsultation(@PathVariable Long id) {
        consultationService.approve(id);
    }

}
