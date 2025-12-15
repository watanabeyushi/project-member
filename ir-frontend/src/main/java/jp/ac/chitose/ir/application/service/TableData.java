package jp.ac.chitose.ir.application.service;

import java.util.List;

public record TableData<T>(
        List<T> data

) {
}
