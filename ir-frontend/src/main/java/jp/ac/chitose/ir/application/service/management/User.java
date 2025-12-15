package jp.ac.chitose.ir.application.service.management;

public record User(
        long id,
        String login_id,
        String name,
        boolean is_available
) {
}
