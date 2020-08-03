package executor.com.tt.invocation;

public class Teacher implements People {
    @Override
    public String work() {
        System.out.println("teaching..");
        return "success";
    }

    @Override
    public People job(String workName) {
        return null;
    }
}
