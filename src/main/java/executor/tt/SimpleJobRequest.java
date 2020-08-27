package executor.tt;


import lombok.Data;

@Data
public class SimpleJobRequest {

    protected String dateType = "d,w,m";

    protected String startDate;

    protected String endDate;

}
