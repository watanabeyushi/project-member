package jp.ac.chitose.ir.application.config;

import jp.ac.chitose.ir.application.exception.APIClientErrorException;
import jp.ac.chitose.ir.application.exception.APIServerErrorException;
import jp.ac.chitose.ir.application.service.helloworld.HelloService;
import jp.ac.chitose.ir.application.service.class_select.ClassSelect;
import jp.ac.chitose.ir.application.service.commission.GradeService;
import jp.ac.chitose.ir.application.service.commission.UniversityService;
import jp.ac.chitose.ir.application.service.management.UserManagementService;
import jp.ac.chitose.ir.application.service.questionnaire.QuestionnaireService;
import jp.ac.chitose.ir.application.service.sample.SampleService;
import jp.ac.chitose.ir.application.service.student.StudentGradeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class HttpClientConfiguration {

    @Value("${fastapi.url}")
    private String baseUrl;

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient.Builder builder) {
        final WebClient webClient = builder.baseUrl(baseUrl).
                defaultStatusHandler(HttpStatusCode::is5xxServerError, clientResponse -> Mono.just(new APIServerErrorException("API側で内部エラーが発生しました。"))).
                defaultStatusHandler(HttpStatusCode::is4xxClientError, clientResponse -> Mono.just(new APIClientErrorException("エラーが発生しました。"))).build();
        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
    }

    @Bean
    public HelloService helloService(HttpServiceProxyFactory factory) {
        return factory.createClient(HelloService.class);
    }

    @Bean
    public SampleService sampleService(HttpServiceProxyFactory factory) {
        return factory.createClient(SampleService.class);
    }

    @Bean
    public ClassSelect classSelect(HttpServiceProxyFactory factory) {
        return factory.createClient(ClassSelect.class);
    }

    @Bean
    public QuestionnaireService questionnaireService(HttpServiceProxyFactory factory){
        return factory.createClient(QuestionnaireService.class);
    }

    @Bean
    public UserManagementService userManagementService(HttpServiceProxyFactory factory) {
        return factory.createClient(UserManagementService.class);
    }

    @Bean
    public UniversityService universityService(HttpServiceProxyFactory factory) {
        return factory.createClient(UniversityService.class);
    }

    @Bean
    public StudentGradeService studentGradeService(HttpServiceProxyFactory factory){
        return factory.createClient(StudentGradeService.class);
    }

    @Bean
    public GradeService gradeService(HttpServiceProxyFactory factory){
        return factory.createClient(GradeService.class);
    }
}