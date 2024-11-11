package family.excitation.service

import org.bytedeco.javacv.FFmpegFrameGrabber
import org.bytedeco.javacv.Java2DFrameConverter

import javax.imageio.ImageIO

class VideoUtilsWithJavaCV {
    static File getVideoCover(String videoPath, String outputCoverPath) {
        try {
            def ffmpeg = new FFmpegFrameGrabber(videoPath)
            ffmpeg.start()
            def frame = ffmpeg.grabImage()
            if (frame) {
                def converter = new Java2DFrameConverter()
                def image = converter.convert(frame)
                def outputFile = new File(outputCoverPath)
                ImageIO.write(image, 'jpg', outputFile)
                ffmpeg.stop()
                return outputFile
            } else {
                ffmpeg.stop()
                return null
            }
        } catch (e) {
            println "getVideoCover error: ${e.message}"
            return null
        }
    }
}
