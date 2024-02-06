package com.mySpring.demo.Models;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class ViewUpdate implements Serializable {
    private Long id;
    private Long views;

    public ViewUpdate() {
    }

    public ViewUpdate(Long id, Long views) {
        this.id = id;
        this.views = views;
    }

}