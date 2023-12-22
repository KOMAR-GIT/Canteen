package main.api;

public class LastEmployee {

    private String name;

    private Integer count;

    private String time;

    public LastEmployee() {
    }

    public LastEmployee(String name, Integer count, String time) {
        this.name = name;
        this.count = count;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
