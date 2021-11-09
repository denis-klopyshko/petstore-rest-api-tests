package io.petstore.dto;

import groovyjarjarpicocli.CommandLine;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private long id;
    private String name;
}
