package executor.com.tt.one;

import lombok.Data;
import org.springframework.stereotype.Repository;

/**
 * @author huangcaihuan
 * @Since 2020/8/31
 */
@Data
@Repository
public class BoundBerry {

    private static final Integer DEFAULT_PAGE_SIZE = Integer.valueOf(10);
    private static final Integer DEFAULT_PAGE_NUM = Integer.valueOf(1);
    private static final Integer DEFAULT_OFFSET = Integer.valueOf(0);


    private Integer offset = DEFAULT_OFFSET;


    private Integer limit = DEFAULT_PAGE_SIZE;

    //    @ApiModelProperty(value = "是否统计总数,默认true")
    private Boolean count = true;

    private String sort;


    private String order;

}
