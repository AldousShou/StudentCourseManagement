package com.shoumh.core.service;

import com.shoumh.core.common.ChoiceStatus;
import com.shoumh.core.pojo.ChoiceSheetResult;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseSheet;
import com.shoumh.core.pojo.Student;
import org.jetbrains.annotations.NotNull;

public interface CourseAsyncService {

    /* --------------------------
        before_check 队列处理
     -------------------------- */

    /**
     * 检查并发送表单中各项课程的合法性。
     * 表单状态：processing, success;
     * 课程状态：ChoiceStatus
     */
    public void checkAndSendChoiceSheetLegality(@NotNull CourseSheet sheet);

    /**
     * 检查课程的合法性
     * @return ChoiceStatus，表示课程的状态，如果成功则为 SUCCESS
     */
    public ChoiceStatus checkChoiceLegality(@NotNull Student student, @NotNull Course course) ;

    /**
     * 记录表单信息，往数据库写入：包括表单的状态信息以及其中课程的状态信息
     * @param sheet
     */
    public void logSheetStatus(@NotNull CourseSheet sheet);



    /* --------------------------
        checked 队列处理
     -------------------------- */

    /**
     * 更新表单状态，往数据库中更新新的表单状态
     */
    public void updateSheetStatus(@NotNull ChoiceSheetResult sheet);

    /**
     * 将学生选课的内容持久化进入数据库
     */
    public void writeChoiceSheet(@NotNull ChoiceSheetResult sheetResult);
}
