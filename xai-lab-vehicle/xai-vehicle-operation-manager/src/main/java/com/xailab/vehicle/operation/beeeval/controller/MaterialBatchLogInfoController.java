package com.xailab.vehicle.operation.beeeval.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.xailab.vehicle.operation.beeeval.service.MaterialBatchLogInfoService;



/**
 * 
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-07-10 17:14:20
 */
@RestController
@RequestMapping("beeeval/materialbatchloginfo")
public class MaterialBatchLogInfoController {
    @Autowired
    private MaterialBatchLogInfoService materialBatchLogInfoService;

}
