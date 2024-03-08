package com.shoumh.core.common;

import java.util.HashMap;

public enum ChoiceStatus {
    NOT_RUBBED, // 没有抢到课程
    CHOICE_CONFICTED, // 与现有课程冲突
    CHOICE_DUPLICATED, // 已经选择该课程
    PREDECESSOR_UNCHOSEN, // 没有选择先导课
    MAJOR_UNFULFILLED,  // 专业不符
    PROCESSING, // 正在处理，一般不出现在过程中，仅出现在数据库的状态中
    SUCCESS  // 成功
    ;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public String readableComment(ChoiceStatus status) {
        switch (status) {
            case NOT_RUBBED -> {
                return "课程未被抢到";
            }
            case CHOICE_CONFICTED -> {
                return "课程冲突";
            }
            case CHOICE_DUPLICATED -> {
                return "课程被多次抢选";
            }
            case PREDECESSOR_UNCHOSEN -> {
                return "课程先导课未被选择";
            }
            case MAJOR_UNFULFILLED -> {
                return "课程专业不符";
            }
            case PROCESSING -> {
                return "正在处理专业";
            }
            case SUCCESS -> {
                return "成功";
            }
            default -> {
                return null;
            }
        }
    }
}
