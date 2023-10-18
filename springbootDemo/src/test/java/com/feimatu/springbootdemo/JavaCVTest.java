package com.feimatu.springbootdemo;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.junit.jupiter.api.Test;

/**
 * @author mazepeng
 * @date 2023/10/10 12:21
 */
public class JavaCVTest {
    @Test
    public void generateM3U8() throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception {

        // 输入视频文件路径
        String inputVideoPath = "/Users/mazepeng/IdeaProjects/range/springbootDemo/src/main/resources/mp4/big_buck_bunny.mp4";

        // 输出目录
        String outputDirectory = "/Users/mazepeng/IdeaProjects/range/springbootDemo/src/main/resources/mp4/";
        String outputM3U8Path = outputDirectory + "output.m3u8";

        // 设置 FFmpegFrameGrabber
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideoPath);
        try {
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

        // 设置 FFmpegFrameRecorder
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputM3U8Path, grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setFormat("hls");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFrameRate(grabber.getFrameRate());
        recorder.setVideoBitrate(grabber.getVideoBitrate());
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setAudioChannels(grabber.getAudioChannels());
        recorder.setOption("hls_base_url","/ts/");

        try {
            recorder.start();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        }

        // 实时抓取帧并编码
        Frame frame;
        long startTime = System.currentTimeMillis();
        int frameCount = 0;

        try {
            while ((frame = grabber.grab()) != null) {
                recorder.record(frame);

                // 在控制台输出一些信息
                if (++frameCount % 100 == 0) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Frame: " + frameCount + ", Elapsed Time: " + elapsedTime + " ms");
                }
            }
        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
            e.printStackTrace();
        }

        // 停止录制和抓取
        try {
            grabber.stop();
            recorder.stop();
        } catch (FrameGrabber.Exception | FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void generateDASH() throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception {

        // 输入视频文件路径
        String inputVideoPath = "/Users/mazepeng/IdeaProjects/range/springbootDemo/src/main/resources/mp4/big_buck_bunny.mp4";

        // 输出目录
        String outputDirectory = "http://localhost:8080/receiveTheStream/12.mpd";

        try (FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(inputVideoPath)){
            fFmpegFrameGrabber.start();
            FFmpegFrameRecorder fFmpegFrameRecorder = new FFmpegFrameRecorder(outputDirectory,
                    fFmpegFrameGrabber.getImageWidth(), fFmpegFrameGrabber.getImageHeight(), fFmpegFrameGrabber.getAudioChannels());
            fFmpegFrameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            fFmpegFrameRecorder.setFormat("dash");
            fFmpegFrameRecorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            fFmpegFrameRecorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            fFmpegFrameRecorder.setAudioChannels(fFmpegFrameGrabber.getAudioChannels());
            fFmpegFrameRecorder.start();
            Frame frame = null;
            while ((frame = fFmpegFrameGrabber.grabFrame()) != null) {
                fFmpegFrameRecorder.record(frame);
            }
            fFmpegFrameRecorder.stop();
            fFmpegFrameRecorder.release();
        } catch (FrameRecorder.Exception e) {
           e.printStackTrace();
        }


    }



    public void generateMP4() throws FrameGrabber.Exception, FFmpegFrameRecorder.Exception {

        // 输入视频文件路径
        String inputVideoPath = "/Users/mazepeng/IdeaProjects/range/springbootDemo/src/main/resources/mp4/big_buck_bunny.mp4";

        // 输出目录
        String outputDirectory = "http://localhost:8080/receiveTheStreamMp4/12.mp4";

        try (FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(inputVideoPath)){
            fFmpegFrameGrabber.start();
            FFmpegFrameRecorder fFmpegFrameRecorder = new FFmpegFrameRecorder(outputDirectory,
                    fFmpegFrameGrabber.getImageWidth(), fFmpegFrameGrabber.getImageHeight(), fFmpegFrameGrabber.getAudioChannels());
            fFmpegFrameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            fFmpegFrameRecorder.setFormat("mp4");
            fFmpegFrameRecorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
            fFmpegFrameRecorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            fFmpegFrameRecorder.setAudioChannels(fFmpegFrameGrabber.getAudioChannels());
            fFmpegFrameRecorder.start();
            Frame frame = null;
            while ((frame = fFmpegFrameGrabber.grabFrame()) != null) {
                fFmpegFrameRecorder.record(frame);
            }
            fFmpegFrameRecorder.stop();
            fFmpegFrameRecorder.release();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        }


    }


}
