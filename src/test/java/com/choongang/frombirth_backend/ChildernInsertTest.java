package com.choongang.frombirth_backend;

import com.choongang.frombirth_backend.model.dto.ChildrenDTO;
import com.choongang.frombirth_backend.service.ChildrenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
public class ChildernInsertTest {

    @Autowired
    ChildrenService childrenService;

    @Test
    public void childernInsert() {

//        ChildrenDTO children = ChildrenDTO.builder()
//                .userId(19)
//                .name("백길동")
//                .birthDate(LocalDate.of(2024, 9, 11))
//                .gender("M")
//                .bloodType("a")
//                .birthWeight(2)
//                .birthTime(LocalTime.of(14, 30))
//                .birthHeight(20)
//                .build();
//
//        childrenService.addChild(children);
//
//        ChildrenDTO result = childrenService.getChildById(32);
//        System.out.println(result);

    }
}
