package com.toyproject.book.springboot;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println(Optional.ofNullable(null).orElseGet(()->""));
    }
}
