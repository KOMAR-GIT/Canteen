package com.moika22.canteen.api;

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

    public String getTime() {
        return time;
    }
}
