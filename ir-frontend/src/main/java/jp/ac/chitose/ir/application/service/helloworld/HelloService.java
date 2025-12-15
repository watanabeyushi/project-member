package jp.ac.chitose.ir.application.service.helloworld;

import jp.ac.chitose.ir.application.service.TableData;
import jp.ac.chitose.ir.application.service.student.GradeCount;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/grade", accept = "application/json", contentType = "application/json")
public interface HelloService {
    @GetExchange("/helloworld")
    TableData<HelloworldGrade> getGradeGraph();
}
