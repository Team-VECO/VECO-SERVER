package com.project.mt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TumblerSize {
    SMALL(355),
    LARGE(473);
    int ml;
}
