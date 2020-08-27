package executor.invocation;

public class Student implements User {
    @Override
    public User info(String name) {

        System.out.println("my name is "+name);
        return this;
    }
}
