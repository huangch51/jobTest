package executor.com.tt.tag;

import lombok.Data;

/**
 * @author huangcaihuan
 * @Since 2020/9/25
 */
@Data
public class UserTagPro {

    @NumberFormat(point=2)
   private int point;

    @NumberFormat()
   private String profile;

    @NumberFormat()
    private boolean isPrint;
}
