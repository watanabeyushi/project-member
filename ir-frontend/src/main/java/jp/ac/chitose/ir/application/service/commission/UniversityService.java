package jp.ac.chitose.ir.application.service.commission;

import jp.ac.chitose.ir.application.service.TableData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/university", accept = "application/json", contentType = "application/json")

public interface UniversityService {
    @GetExchange("/teacher_training/{academic_year}")
    TableData<TeacherTraining> getTeacherTraining(@PathVariable int academic_year);

    @GetExchange("/graduate_research_grants/{academic_year}")
    TableData<ResearchSupports> getResearchSupports(@PathVariable int academic_year);

    @GetExchange("/JASSO_scholarship/{academic_year}")
    TableData<ScholarShipJasso> getScholarShipJasso(@PathVariable int academic_year);

    @GetExchange("/other_scholarship/{academic_year}")
    TableData<ScholarShipOthers> getScholarShipOthers(@PathVariable int academic_year);

    @GetExchange("/required_credits_common_courses/{academic_year}")
    TableData<CommonUnits> getCommonUnits(@PathVariable int academic_year);

    @GetExchange("/graduation_minimum_credits_specialty_other/{academic_year}")
    TableData<MajorUnits> getMajorUnits(@PathVariable int academic_year);

}
