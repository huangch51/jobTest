package executor.tt;


import lombok.Data;

import static executor.tt.DateUtils.*;



/**
 * 整体经营分析、营收分析的VO
 */
@Data
public class SimpleJobVO extends SimpleJobRequest {

    public String getDateTypeFormat() {
        switch (dateType) {
            case DATE_TYPE_MONTH: return DB_FORMAT_MONTH;
            case DATE_TYPE_WEEK: return DB_FORMAT_WEEK;
            default: return DB_FORMAT_DATE;
        }
    }

}