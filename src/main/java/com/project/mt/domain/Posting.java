package com.project.mt.domain;

import com.project.mt.enums.Color;
import com.project.mt.enums.DrinkType;
import com.project.mt.enums.TumblerSize;
import com.project.mt.enums.TumblerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Posting {
    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    private String url;

    @Enumerated(EnumType.STRING)
    private TumblerType tumblerType;

    @Enumerated(EnumType.STRING)
    private DrinkType drinkType;

    @Enumerated(EnumType.STRING)
    private TumblerSize tumblerSize;

    @Enumerated(EnumType.STRING)
    private Color color;
}
