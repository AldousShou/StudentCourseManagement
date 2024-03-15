package com.shoumh.core.common.course;

public enum CourseStatus {
    /**
     * 当一个课程被成功判定为可以被选择（满足所有先决条件），在等待被写入数据库时，他的状态是 Prechosen
     */
    PRECHOSEN,
    /**
     * 一个课程一般的状态为 normal，意为这个课程有效
     */
    NORMAL,
    /**
     * 这个课程已经修完，表示结束
     */
    ENDED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}