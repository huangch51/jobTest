package executor.com.tt.invocation;

public class Engine implements People {
    @Override
    public String work() {
        System.out.println("Engine");
        return "success";
    }

    @Override
    public People job(String workName) {
        System.out.println("details:"+workName);
        return this;
    }
}
