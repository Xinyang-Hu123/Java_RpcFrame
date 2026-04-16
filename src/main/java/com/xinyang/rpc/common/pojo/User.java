package com.xinyang.rpc.common.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor



public class User implements Serializable {
    private String userName;
    private Integer id;
    private Boolean sex;
}
