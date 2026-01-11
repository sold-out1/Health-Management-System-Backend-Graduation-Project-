package com.kmbeast.utils;

import com.kmbeast.pojo.dto.QueryDto;
import com.kmbeast.pojo.vo.ChartVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 时间工具类
 */
public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM月dd日");

    /**
     * 生成从今天开始的未来N天的日期列表
     *
     * @param days 要生成的天数
     * @return 格式为"xx月xx日"的日期列表
     */
    public static List<String> generateFutureDates(int days) {
        List<String> dateList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < days; i++) {
            LocalDate futureDate = today.plusDays(i);
            dateList.add(futureDate.format(FORMATTER));
        }

        return dateList;
    }

    /**
     * 构造时间范围查询器
     *
     * @param days 查询天数范围：
     *             -1 = 查询全部（不设时间范围）
     *             0 = 今天
     *             1 = 昨天到今天
     *             n = 过去n天到现在
     * @return 包含开始时间和结束时间的QueryDto对象
     */
    public static QueryDto startAndEndTime(Integer days) {
        // 处理查询全部的情况
        if (days == -1) {
            return new QueryDto(); // 返回无时间限制的查询
        }

        // 验证参数有效性
        if (days < 0) {
            throw new IllegalArgumentException("天数参数不能为负数（除-1外）");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime;

        if (days == 0) {
            // 今天的情况
            startTime = now.with(LocalTime.MIN); // 当天00:00:00
        } else {
            // 计算n天前的00:00:00
            startTime = now.minusDays(days).with(LocalTime.MIN);
        }

        return QueryDto.builder()
                .startTime(startTime)
                .endTime(now)
                .build();
    }

    /**
     * 统计指定天数内的记录数
     *
     * @param dayRange 往前查多少天
     * @param dates    数据源
     * @return Map<String, Integer>
     */
    public static List<ChartVO> countDatesWithinRange(Integer dayRange, List<LocalDateTime> dates) {
        LocalDate startDate = LocalDate.now().minusDays(dayRange);
        List<ChartVO> chartVOS = new ArrayList<>();
        for (int offset = 0; offset <= dayRange; offset++) {
            LocalDate currentDate = startDate.plusDays(offset);
            String dateKey = String.format("%02d-%02d", currentDate.getMonthValue(), currentDate.getDayOfMonth());
            int count = (int) dates.stream()
                    .filter(dateTime -> dateTime.toLocalDate().equals(currentDate))
                    .count();
            if (count != 0) {
                // 为了数据更好看，在原始的数据之上，添加一个随机数
//                chartVOS.add(new ChartVO(dateKey, count * new Random().nextInt(10000)));
                chartVOS.add(new ChartVO(dateKey, count)); //这是原始的数据，不想要上述模拟效果，只需要将上一行代码注释掉，这一行放开即可
            }
        }
        return chartVOS;
    }

    /**
     * 统计指定天数内的记录数
     *
     * @param dayRange 往前查多少天
     * @param dates    数据源
     * @return 包含日期和计数的ChartVO列表
     */
    public static List<ChartVO> countDatesRange(Integer dayRange, List<LocalDateTime> dates) {
        // 定义日期格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

        // 获取起始日期（当前日期往前推dayRange天）
        LocalDate startDate = LocalDate.now().minusDays(dayRange);
        List<ChartVO> chartVOS = new ArrayList<>();

        // 遍历每一天
        for (int offset = 0; offset <= dayRange; offset++) {
            LocalDate currentDate = startDate.plusDays(offset);

            // 格式化为"YYYY年MM月DD日"
            String dateKey = currentDate.format(formatter);

            // 统计当天记录数
            int count = (int) dates.stream()
                    .filter(dateTime -> dateTime.toLocalDate().equals(currentDate))
                    .count();

            if (count != 0) {
                chartVOS.add(new ChartVO(dateKey, count));
            }
        }

        return chartVOS;
    }

}
