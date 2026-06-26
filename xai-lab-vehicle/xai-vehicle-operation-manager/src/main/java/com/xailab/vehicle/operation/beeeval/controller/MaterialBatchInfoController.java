package com.xailab.vehicle.operation.beeeval.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xailab.vehicle.operation.beeeval.entity.MaterialBatchInfoEntity;
import com.xailab.vehicle.operation.beeeval.service.MaterialBatchInfoService;



/**
 * 
 *
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-07-10 21:29:52
 */
@RestController
@RequestMapping("beeeval/materialbatchinfo")
public class MaterialBatchInfoController {
    @Autowired
    private MaterialBatchInfoService materialBatchInfoService;

}
