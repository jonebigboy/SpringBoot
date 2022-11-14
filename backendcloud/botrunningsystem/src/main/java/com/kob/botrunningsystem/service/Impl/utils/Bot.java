package com.kob.botrunningsystem.service.Impl.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    private Integer useId;
    private String botCode;
    private String input;
}
