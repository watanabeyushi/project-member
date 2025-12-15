package jp.ac.chitose.ir.application.service.class_select;

import jp.ac.chitose.ir.application.service.TableData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/review", accept = "application/json", contentType = "application/json")
public interface ClassSelect {

    @GetExchange("/graph/{subject_id}")
    TableData<ClassQPOJFICHKVJB> getClassQPOJFICHKVJB(@PathVariable String subject_id);

    @GetExchange("/description/{subject_id}")
    TableData<ReviewQPOJFICHKVJBDescription> getReviewQPOJFICHKVJBDescription(@PathVariable String subject_id);

    @GetExchange("/title/{subject_id}")
    TableData<ReviewTitle> getReviewTitle(@PathVariable String subject_id);//質問事項

    @GetExchange("/describe/{subject_id}")
    TableData<ReviewDescribe> getReviewDescribe(@PathVariable String subject_id);

}

