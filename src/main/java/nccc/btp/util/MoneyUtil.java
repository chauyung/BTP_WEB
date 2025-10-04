package nccc.btp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyUtil {

    // 外幣格式（保留兩位小數）
    public static String formatForeignCurrency(BigDecimal amount) {
        DecimalFormat foreignFormat = new DecimalFormat("#,##0.00");
        return foreignFormat.format(amount);
    }

    // 台幣格式（正整數）
    public static String formatTWD(BigDecimal amount) {
        DecimalFormat twdFormat = new DecimalFormat("#,##0");
        return twdFormat.format(amount);
    }

}
