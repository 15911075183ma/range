package com.feimatu.springbootdemo.demos.services;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class VideoConversionService {

    private final ByteArrayOutputStream mpdBuffer = new ByteArrayOutputStream();
    private final Map<String, ByteArrayOutputStream> m4sBuffers = new HashMap<>();

    public void updateMpdData(byte[] data) throws IOException {
        mpdBuffer.reset();
        mpdBuffer.write(data);
    }

    public void appendM4sData(String segment, byte[] data) throws IOException {
        if (!m4sBuffers.containsKey(segment)) {
            m4sBuffers.put(segment, new ByteArrayOutputStream());
        }
        m4sBuffers.get(segment).write(data);
    }

    public byte[] getMpdData() {
        return mpdBuffer.toByteArray();
    }

    public byte[] getM4sData(String segment) {
        return m4sBuffers.getOrDefault(segment, new ByteArrayOutputStream()).toByteArray();
    }

    public void convertToHLSAndStream(String videoFilePath, HttpServletResponse response) {
        try {
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFilePath);
            grabber.start();

            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
                    response.getOutputStream(),
                    grabber.getImageWidth(), grabber.getImageHeight(),
                    grabber.getAudioChannels());
            recorder.setFormat("hls");
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
            recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);

            recorder.start();

            Frame frame;
            while ((frame = grabber.grab()) != null) {
                recorder.record(frame);
                response.flushBuffer();
            }

            grabber.stop();
            recorder.stop();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().print("Error during conversion: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}
