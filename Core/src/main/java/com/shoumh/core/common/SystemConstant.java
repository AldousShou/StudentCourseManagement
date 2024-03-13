package com.shoumh.core.common;

public final class SystemConstant {

    private SystemConstant() {}

    public static final Integer YEAR = 2024;

    public static final Integer SEMESTER = 1;

    @Deprecated
    public static final String DEFAULT_EXCHANGE = "shoumh.fanout";
    @Deprecated
    public static final String DEFAULT_QUEUE = "fanout.default_queue";

    public static final String CHOICE_BEFORE_EXCHANGE = "shoumh.choice_before_check_exchange";
    public static final String CHOICE_BEFORE_CHECK_QUEUE_LOG = "fanout.choice_before_check_queue_log";
    public static final String CHOICE_BEFORE_CHECK_QUEUE_CHECK = "fanout.choice_before_check_queue_check";

    public static final String CHOICE_CHECKED_EXCHANGE = "shoumh.choice_checked_exchange";
    public static final String CHOICE_CHECKED_QUEUE_DB = "fanout.choice_checked_queue_db";
    public static final String CHOICE_CHECKED_QUEUE_LOG = "fanout.choice_checked_queue_log";

    public static final String LOG_EXCHANGE = "shoumh.log";
    public static final String LOG_QUEUE = "fanout.log";


}
