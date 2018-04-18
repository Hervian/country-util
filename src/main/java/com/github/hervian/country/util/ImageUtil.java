package com.github.hervian.country.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import org.apache.batik.transcoder.TranscoderInput;

public class ImageUtil {

    /**
     * Explanation of the surpringly complicated logic: "ImageIO.read(*...) will only load these image types GIF, PNG, JPEG, BMP, and WBMP". See https://stackoverflow.com/a/15726292/6095334
     * See also {@link BufferedImageTranscoder}
     * @param iso3166CountryCode
     * @return
     */
    static BufferedImage getImageFromCountryCode(String iso3166CountryCode) {
        BufferedImage image = null;

        InputStream is = getFlagFile(iso3166CountryCode);
        if (is != null) {
            try {
                TranscoderInput transIn;
                transIn = new TranscoderInput(is);
                BufferedImageTranscoder trans = new BufferedImageTranscoder();
                trans.transcode(transIn, null);
                image = trans.getBufferedImage();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return image;

    }

    private static InputStream getFlagFile(String iso3166CountryCode) {
        return GenerateCountryEnumProcessor.class.getClassLoader().getResourceAsStream("svg/" + iso3166CountryCode.toLowerCase() + ".svg");
    }

}
