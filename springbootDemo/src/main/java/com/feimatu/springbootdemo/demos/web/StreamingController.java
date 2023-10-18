package com.feimatu.springbootdemo.demos.web;

import com.feimatu.springbootdemo.demos.services.VideoConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author mazepeng
 * @date 2023/10/12 10:31
 */
@Controller
@CrossOrigin(origins = "*")
public class StreamingController {

    @Resource
    VideoConversionService videoConversionService;

    @RequestMapping("/receiveTheStream/{stream_id}")
    public void getDASH(@PathVariable("stream_id") String streamId, @RequestBody byte[] data) {
        System.out.println(streamId);

        try {
            if (streamId.endsWith(".mpd")) {
                videoConversionService.updateMpdData(data);

            } else {
                videoConversionService.appendM4sData(streamId, data);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }


    @RequestMapping("/play/{stream_id}")
    public ResponseEntity<byte []> getDASH(@PathVariable("stream_id") String streamId) {
        System.out.println(streamId);

        ResponseEntity.BodyBuilder ok = ResponseEntity.ok();


            if (streamId.endsWith(".mpd")) {

                return ok.header("Content-Type", "application/dash+xml")
                        .body(videoConversionService.getMpdData());
            } else {

                return ok.body(videoConversionService.getM4sData(streamId));
            }
    }


    @RequestMapping("/receiveTheStream/{stream_id}")
    public void SaveMP4(@PathVariable("stream_id") String streamId, @RequestBody byte[] data) {
        System.out.println(streamId);

        try {
            if (streamId.endsWith(".mpd")) {
                videoConversionService.updateMpdData(data);

            } else {
                videoConversionService.appendM4sData(streamId, data);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

}
