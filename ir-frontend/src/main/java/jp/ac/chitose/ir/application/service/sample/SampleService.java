package jp.ac.chitose.ir.application.service.sample;

import jp.ac.chitose.ir.application.service.TableData;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/sample", accept = "application/json", contentType = "application/json")
public interface SampleService {

    @GetExchange("/one")
    TableData<SampleOne> getSampleOne();

    @GetExchange("/two")
    TableData<SampleTwo> getSampleTwo();

    @GetExchange("/three")
    TableData<SampleThree> getSampleThrees();

    @GetExchange("/gpa")
    TableData<SapmleGpa> getSampleGpa();

    @GetExchange("/gpa2")
    TableData<SampleGpa2>getSampleGpa2();
}
