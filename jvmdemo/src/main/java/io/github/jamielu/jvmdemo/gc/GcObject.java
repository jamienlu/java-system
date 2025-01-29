package io.github.jamielu.jvmdemo.gc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jamieLu
 * @create 2025-01-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GcObject {
    private Integer age;
    private String name;
    private String desc;
}
