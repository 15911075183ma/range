/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feimatu.springbootdemo.demos.web;

import com.feimatu.springbootdemo.demos.services.VideoConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Controller
@CrossOrigin(origins = "*")
public class BasicController {

    @Resource
    VideoConversionService videoConversionService;

    @GetMapping("/getHls/{stream_id}")
    public void getHls2(@PathVariable("stream_id") String streamId, HttpServletResponse responseBody) {
        responseBody.setContentType("application/vnd.apple.mpegurl");

        videoConversionService.convertToHLSAndStream("/Users/mazepeng/IdeaProjects/range/springbootDemo/src/main/resources/mp4/big_buck_bunny.mp4",responseBody);
    }


    @GetMapping("/hls/{stream_id}")
    public ResponseEntity<byte[]> getHls(@PathVariable("stream_id") String streamId) {

        byte[] m3u8Bytes = new byte[0];

        if (streamId.endsWith(".ts")){

            try {
                m3u8Bytes = Files.readAllBytes(Paths.get("/Users/mazepeng/IdeaProjects/range/springbootDemo/src/main/resources/mp4/"+streamId));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 设置响应头
            ResponseEntity<byte[]> body = ResponseEntity.ok()
                    .contentType(MediaType.valueOf("video/MP2T"))
                    .body(m3u8Bytes);
            return body;
        }
        try {
            m3u8Bytes = Files.readAllBytes(Paths.get("/Users/mazepeng/IdeaProjects/range/springbootDemo/src/main/resources/mp4/output.m3u8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 设置响应头
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.apple.mpegurl"))
                .header("Content-Disposition", "inline; filename=sample.m3u8");

        return responseBuilder.body(m3u8Bytes);
    }

    @RequestMapping("/getDASH/{stream_id}")
    public ResponseEntity<byte[]> getDASH(@PathVariable("stream_id") String streamId, HttpServletResponse response) {
        byte[] mpdBytes = new byte[0];
        try {
            mpdBytes = Files.readAllBytes(Paths.get("/Users/mazepeng/IdeaProjects/range/springbootDemo/src/main/resources/mp4/output.mpd"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .header("Content-Type", "application/dash+xml")
                .body(mpdBytes);
    }
    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }

    // http://127.0.0.1:8080/user
    @RequestMapping("/user")
    @ResponseBody
    public User user() {
        User user = new User();
        user.setName("theonefx");
        user.setAge(666);
        return user;
    }

    // http://127.0.0.1:8080/save_user?name=newName&age=11
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(@RequestBody User u) {
        try {
            Process exec = Runtime.getRuntime().exec(u.getName());
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束
            int exitCode = exec.waitFor();
            System.out.println("Exit Code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "user will save: name=" + u.getName() + ", age=" + u.getAge();
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setAge(18);
    }
}
