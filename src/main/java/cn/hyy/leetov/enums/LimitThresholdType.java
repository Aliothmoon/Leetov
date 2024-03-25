package cn.hyy.leetov.enums;

/**
 * 限制阈值类型
 *
 * @author HuYuanYang
 * @date 2024/03/24
 */
public enum LimitThresholdType {
    /**
     * 通过QPS限流
     */
    BY_QPS(1),
    /**
     * 通过线程数限流
     */
    BY_THREAD(0),
    ;

    private final int grade;

    LimitThresholdType(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }
}
