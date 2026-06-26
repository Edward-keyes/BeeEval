package com.xailab.vehicle.xaivehicledata.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.xailab.vehicle.xaicommon.utils.M3u8ToMp4Converter;
import com.xailab.vehicle.xaicommon.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ws.schild.jave.EncoderException;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/downloadVideo")
@RequiredArgsConstructor
public class DownloadVideoController {

    @GetMapping("/download")
    public Result<String> downloadVideo(@RequestParam String url){
        Object loginId = StpUtil.getTokenInfo().getLoginId();
        try {
            M3u8ToMp4Converter.convertM3u8ToMp4(url,"/mydata/BeeEval_FrontEnd-build/public/videocache/"+loginId+"output.mp4");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Result.ok();

    }
}
