package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class ResponseWrapperAds {
    private int count;
    private Collection<Ads> results;
}
