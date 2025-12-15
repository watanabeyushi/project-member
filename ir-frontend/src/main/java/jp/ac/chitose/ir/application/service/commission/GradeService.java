package jp.ac.chitose.ir.application.service.commission;

import jp.ac.chitose.ir.application.service.TableData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/grade", accept = "application/json", contentType = "application/json")
public interface GradeService {
    @GetExchange("/gpa_graph/{school_year}")
    TableData<GradeGpaGraph> getGradeGpaGraph(@PathVariable String school_year);

    @GetExchange("/gpa_stat/{school_year}")
    TableData<GradeGpaStat> getGradeGpaStat(@PathVariable String school_year);

}
