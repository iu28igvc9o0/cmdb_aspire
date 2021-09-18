package com.migu.tsg.microservice.atomicservice;

import lombok.extern.slf4j.Slf4j;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.composite.CompositeServiceApplication;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/6/11 15:46
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CompositeServiceApplication.class })
public class ApplicationTest {

}
