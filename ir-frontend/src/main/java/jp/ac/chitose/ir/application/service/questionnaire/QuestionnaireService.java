package jp.ac.chitose.ir.application.service.questionnaire;
import jp.ac.chitose.ir.application.service.TableData;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(value = "/questionnaire", accept = "application/json", contentType = "application/json")
public interface QuestionnaireService {
    @GetExchange("/top/grid")
    TableData<QuestionnaireTopGrid> getQuestionnaireTopGrid();
}